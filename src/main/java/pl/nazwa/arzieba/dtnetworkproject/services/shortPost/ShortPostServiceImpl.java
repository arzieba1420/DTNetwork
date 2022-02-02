package pl.nazwa.arzieba.dtnetworkproject.services.shortPost;

import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.mail.EmailConfiguration;
import pl.nazwa.arzieba.dtnetworkproject.utils.shortPost.ShortPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShortPostServiceImpl implements ShortPostService {

    private ShortPostDAO postDAO;
    private DeviceDAO deviceDAO;
    private EmailConfiguration email;

    @Autowired
    public ShortPostServiceImpl(ShortPostDAO postDAO, DeviceDAO deviceDAO, EmailConfiguration email) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
        this.email = email;
    }

    @Override
    public List<ShortPostDTO> findAll() {
        return postDAO.findAll().stream()
                .map(ShortPostMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public ShortPostDTO findById(Integer id) {
        return ShortPostMapper.map(postDAO.findByPostId(id));
    }

    @Override
    public List<ShortPostDTO> findByAuthor(Author author) {
        return postDAO.findAllByAuthor(author).stream()
                .map(ShortPostMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortPostDTO> findByDevice(String inventNumber) {
        return postDAO.findAllByDevice_InventNumber(inventNumber).stream()
                .map(ShortPostMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortPostDTO> find5ByDevice(String inventNumber) {
        return postDAO.findTop10ByDevice_InventNumberOrderByDateDesc(inventNumber)
                .stream()
                .map(ShortPostMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortPostDTO> findLast5() {
        return postDAO.findTop10ByOrderByDateDesc().stream()
                .map(ShortPostMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<ShortPostDTO> findAll(int items, int size) {
        return null;
    }

    @Override
    public List<ShortPost> findAllByYear(int year) {
        Calendar startDate = new GregorianCalendar(year,0,1);
        Calendar endDateDate = new GregorianCalendar(year,11,31);


        return postDAO.findAllByPostDateBetween(startDate,endDateDate);
    }

    @Override
    public Map<Integer, ShortPostDTO> findAllByDevice(String number, int page, int size) {
        DeviceDTO deviceDTO = DeviceMapper.map(deviceDAO.findByInventNumber(number));
        Map<Integer,ShortPostDTO> mapa = new LinkedHashMap<>();

        Page<ShortPost> page1 = postDAO.findAllByDevice_InventNumber(number,PageRequest.of(page,size, Sort.Direction.DESC,"postDate"));
        Set<Integer> keys = new LinkedHashSet<>();

        for (ShortPost post:page1) {
            keys.add(post.getPostId());
        }

        for (Integer key: keys) {
            mapa.put(key,findById(key));
        }

        return mapa;
    }

    @Override
    public int numberByYear(int year){
       long l= postDAO.findAll().stream()
                .filter(d->d.getPostDate().get(Calendar.YEAR)==year)
                .count();

        return (int)l;

    }

    @Override
    public int numberByDevice(String inventNumber) {
        long l= postDAO.findAllByDevice_InventNumber(inventNumber).stream().count();
        return (int)l;
    }

    @Override
    public Map<Integer,ShortPostDTO> findAll(int year, int page, int size) {


        Calendar startDate = new GregorianCalendar(year,0,1);
        Calendar endDateDate = new GregorianCalendar(year,11,31);

        Map<Integer,ShortPostDTO> mapa = new LinkedHashMap<>();
        Page<ShortPost> page1 = postDAO.findAllByPostDateBetween(PageRequest.of(page,size, Sort.Direction.DESC,"postDate"),startDate,endDateDate);

        Set<Integer> keys = new LinkedHashSet<>();

        for (ShortPost post: page1) {
            if(post.getPostDate().get(Calendar.YEAR)==year){
                keys.add(post.getPostId());
            }
        }

        for (Integer key: keys) {
            mapa.put(key,findById(key));
        }
        return mapa;
    }


    @Override
    public ShortPostDTO create(ShortPostDTO dto) {
        postDAO.save(ShortPostMapper.map(dto,deviceDAO));


        if (!dto.getContent().contains("[SYSTEM]"))   {
            String[] receivers=new String[] {"d.trela@cyfronet.pl", "enarkadiuszzieba@gmail.com"};
            email.sendMail(receivers,deviceDAO.findByInventNumber(dto.getInventNumber()).getDeviceDescription()+ " w: "+deviceDAO.findByInventNumber(dto.getInventNumber()).getRoom().name(),dto.getContent()
                     ,dto.getAuthor().name());

        }

        return dto;
    }

    @Override
    public ShortPostDTO update(ShortPostDTO dto) {
        //not required by business logic
        return null;
    }

    @Override
    public ShortPostDTO remove(Integer id) {
        if(!postDAO.existsById(id)){
            throw new RuntimeException();
        } else {
            ShortPostDTO removed = findById(id);
            postDAO.removeByPostId(id);
            removed.setContent("[REMOVED]");
            return removed;
        }
    }

    @Override
    public Map<Integer, ShortPostDTO> searchContent(String phrase) {

        List<ShortPost> list = postDAO.findByContentContainingIgnoreCaseOrderByPostDateDesc(phrase);
        Map<Integer,ShortPostDTO> mapa = new LinkedHashMap<>();
        Set<Integer> keys = new LinkedHashSet<>();
        for (ShortPost post:list) {
            keys.add(post.getPostId());
        }

        for (Integer id:keys) {
            mapa.put(id, ShortPostMapper.map(postDAO.findByPostId(id)));
        }

        return mapa;
    }

    @Override
    public Set<Integer> setOfYears(){

        List<Integer> yearsList= postDAO.findAll().stream()
                .map(d->d.getPostDate().get( Calendar.YEAR))
                .collect(Collectors.toList());
        Set<Integer> years =yearsList.stream().collect(Collectors.toSet());
        TreeSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(years);
        return sortedSet.descendingSet();
    }

    public Set<Integer> subSet(){
        if(setOfYears().size()<=5) return setOfYears();
        List<Integer> yearsList= postDAO.findAll().stream()
                .map(d->d.getPostDate().get( Calendar.YEAR))
                .collect(Collectors.toList());
        TreeSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(yearsList);

        List<Integer> sortedList = new LinkedList<>();
        sortedList.addAll(sortedSet.descendingSet());

        TreeSet<Integer> sortedSubSet = new TreeSet<>();



        for (int i = 0; i <5 ; i++) {
            sortedSubSet.add(sortedList.get(i));
        }

        return sortedSubSet.descendingSet();
    }


    public boolean areAllForDamage(int year){
        Calendar startDate = new GregorianCalendar(year,0,1);
        Calendar endDateDate = new GregorianCalendar(year,11,31);
        List<ShortPost> allPosts = postDAO.findAllByPostDateBetween(startDate,endDateDate);
        boolean allForDamage =true;

        for (ShortPost post: allPosts) {

            if (!post.getContent().contains("kliknięciu")){
                allForDamage = false;
            }

        }

        return allForDamage;
    }

}
