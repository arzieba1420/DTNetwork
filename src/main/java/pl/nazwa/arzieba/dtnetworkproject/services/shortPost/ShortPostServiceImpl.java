package pl.nazwa.arzieba.dtnetworkproject.services.shortPost;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.mail.MailSendException;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.ShortPostDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DeviceDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Device;
import pl.nazwa.arzieba.dtnetworkproject.model.PostLevel;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.device.DeviceMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.enums.ListOfEnumValues;
import pl.nazwa.arzieba.dtnetworkproject.utils.mail.EmailConfiguration;
import pl.nazwa.arzieba.dtnetworkproject.utils.shortPost.ShortPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ShortPostServiceImpl implements ShortPostService {

    //------------------------------------------------------------LOCAL VARIABLES---------------------------------------------------------------------------------------------

    Logger logger = LoggerFactory.getLogger(this.getClass());
    private ShortPostDAO postDAO;
    private DeviceDAO deviceDAO;
    private EmailConfiguration email;
    private ApplicationArguments applicationArguments;
    @Value("${my.pagesize}")
    int pagesize;
    private EmailConfiguration emailConfiguration;

    //------------------------------------------------------------CONSTRUCTOR---------------------------------------------------------------------------------------------

    @Autowired
    public ShortPostServiceImpl(ShortPostDAO postDAO, DeviceDAO deviceDAO, EmailConfiguration email, ApplicationArguments applicationArguments, EmailConfiguration emailConfiguration) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
        this.email = email;
        this.applicationArguments = applicationArguments;
        this.emailConfiguration = emailConfiguration;
    }

    //------------------------------------------------------------DAO METHODS---------------------------------------------------------------------------------------------

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

            if (!dto.getPostLevel().equals(PostLevel.INFO)){
                email.sendMail("Nowy post dla: "+deviceDAO.findByInventNumber(dto.getInventNumber()).getDeviceDescription()+ " w: "+deviceDAO.findByInventNumber(dto.getInventNumber()).getRoom().name(),dto.getContent()
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

    //------------------------------------------------------------CONTROLLER METHODS---------------------------------------------------------------------------------------------

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

    @Override
    public String getAllPostsForDevice(String inv, Model model, int page) {
        DeviceDTO dto = DeviceMapper.map(deviceDAO.findByInventNumber(inv));
        List<ShortPost> all = postDAO.findAllByDevice_InventNumber(inv);
        Map<Integer, ShortPostDTO> mapa = findAllByDevice(inv, page - 1, pagesize);
        int amount = numberByDevice(inv);
        int numberOfPages = (numberByDevice(inv)) / pagesize + 1;
        List<Integer> morePages = new LinkedList<>();
        int i = 2;
        int lastPage = 1;

        if (numberByDevice(inv) % pagesize == 0) {
            numberOfPages--;
        }

        while (i <= numberOfPages) {
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("amount", amount);
        model.addAttribute("posts", mapa);
        model.addAttribute("dto", dto);
        model.addAttribute("classActiveSettings", "active");
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("pages", morePages);
        model.addAttribute("currentPage", page);
        model.addAttribute("inv", inv);
        return "posts/allPostsForDevice";
    }

    @Override
    public String createShortPost(Model model, ShortPostDTO shortPostDTO, BindingResult result, HttpServletRequest request) {

        if (result.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = result.getFieldErrors();
            model.addAttribute("bindingResult", result);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormErr(model, shortPostDTO);
        }

        create(shortPostDTO);
        return "redirect:/dtnetwork";
    }

    @Override
    public String createShortPostAndStay(ShortPostDTO shortPostDTO, BindingResult bindingResult, Model model, HttpServletRequest request) {

        if (bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return addFormErr(model, shortPostDTO.getInventNumber(), shortPostDTO);
        }

        create(shortPostDTO);
        return "redirect:/posts/devices/" + shortPostDTO.getInventNumber()+"/1";
    }

    @Override
    public String addForm(Model model) {
        ShortPostDTO dto = new ShortPostDTO();
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance() ));
        model.addAttribute("devices", mapa);
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        return "posts/addPostForm";
    }

    @Override
    public String addPostFormForDevice(Model model, String inventNumber) {
        ShortPostDTO dto = new ShortPostDTO();
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                + " " + deviceDAO.findByInventNumber(inventNumber).getRoom();
        dto.setDate(CalendarUtil.cal2string(Calendar.getInstance() ));
        model.addAttribute("text", text);
        model.addAttribute("shortPostDTO", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);
        return "posts/addPostFormInv";
    }

    @Override
    public String allPostsByYear(Model model, int year, int page) {
        Map<Integer, ShortPostDTO> mapa = findAll(year, page - 1, 10);
        int numberOfPages = (numberByYear(year)) / 10 + 1;
        List<Integer> morePages = new LinkedList<>();
        List<ShortPost> allPosts = findAllByYear(year);
        int amount = numberByYear(year);
        int i = 2;
        int lastPage = 1;

        if (numberByYear(year) % 10 == 0) {
            numberOfPages--;
        }

        for (ShortPost post : allPosts) {

            if (post.getPostLevel().equals(PostLevel.DAMAGE)) {
                amount--;
            }
        }

        while (i <= numberOfPages) {
            morePages.add(i);
            i++;
            lastPage++;
        }

        model.addAttribute("amount", amount);
        model.addAttribute("classActiveSettings", "active");
        model.addAttribute("posts", mapa);
        model.addAttribute("pages", morePages);
        model.addAttribute("currentPage", page);
        model.addAttribute("year", year);
        model.addAttribute("lastPage", lastPage);
        return "posts/allByYear";
    }

    @Override
    public String searchByPhrase(Model model, String phrase) {
        Map<Integer,ShortPostDTO> mapa= searchContent(phrase);
        int amount = mapa.keySet().size();
        model.addAttribute("mapa", mapa);
        model.addAttribute("keys", mapa.keySet());
        model.addAttribute("amount", amount);
        model.addAttribute("phrase",phrase);
        return "posts/searchResults";
    }

    @Override
    public String editPostForm(Model model, Integer id) {
        ShortPostDTO dto = findById(id);
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("devices", mapa);
        model.addAttribute("id", id);
        return "posts/editPostForm";
    }

    @Override
    @Transactional
    public String removeSystemPosts() {
        List<ShortPost> posts = postDAO.findAll();

        for (ShortPost post: posts) {

            if (post.getPostLevel().equals(PostLevel.GENERAL)) postDAO.delete(post);
        }

        ShortPostDTO shortPostDTO = new ShortPostDTO(Author.DTN,"Usunięto posty systemowe! [SYSTEM]",
                CalendarUtil.cal2string(Calendar.getInstance()),"DTN",false,PostLevel.INFO);
        create(shortPostDTO);
        return "redirect:/dtnetwork";
    }

    @Override
    public String saveEditedPost(ShortPostDTO shortPostDTO, BindingResult bindingResult, Model model, Integer id) {
        ShortPost postToEdit = postDAO.findByPostId(id);
        Calendar calendar= CalendarUtil.string2cal(shortPostDTO.getDate());

        if (bindingResult.hasFieldErrors()) {
            List<FieldError> allErrors;
            allErrors = bindingResult.getFieldErrors();
            model.addAttribute("bindingResult", bindingResult);
            model.addAttribute("errors", allErrors);
            model.addAttribute("errorsAmount", allErrors.size());
            return editFormErr(model, shortPostDTO.getInventNumber(), shortPostDTO, id);
        }

        postToEdit.setAuthor(shortPostDTO.getAuthor());
        postToEdit.setContent(shortPostDTO.getContent());
        postToEdit.setPostDate(CalendarUtil.string2cal(shortPostDTO.getDate()));
        postToEdit.setDevice(deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()));
        postToEdit.setDate(calendar.getTime());
        postDAO.save(postToEdit);

            if ((!shortPostDTO.getPostLevel().equals(PostLevel.INFO))){
                emailConfiguration.sendMail("Wpis dla: "+deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()).getDeviceDescription()+ " w: "+deviceDAO.findByInventNumber(shortPostDTO.getInventNumber()).getRoom().name()+" [UPDATE]",shortPostDTO.getContent()
                        ,shortPostDTO.getAuthor().name());
            }
        return "redirect:/dtnetwork";
    }

    public Set<Integer> subSet(){

        if(setOfYears().size()<=5) return setOfYears();

        List<Integer> yearsList= postDAO.findAll().stream()
                .map(d->d.getPostDate().get( Calendar.YEAR))
                .collect(Collectors.toList());
        TreeSet<Integer> sortedSet = new TreeSet<>();
        List<Integer> sortedList = new LinkedList<>();
        TreeSet<Integer> sortedSubSet = new TreeSet<>();
        sortedSet.addAll(yearsList);
        sortedList.addAll(sortedSet.descendingSet());

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

            if (!post.getPostLevel().equals(PostLevel.DAMAGE)){
                allForDamage = false;
            }
        }
        return allForDamage;
    }

    public String addFormErr(Model model, ShortPostDTO dto) {
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        model.addAttribute("devices", mapa);
        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        return "posts/addPostForm";
    }

    public String addFormErr(Model model, @PathVariable String inventNumber, ShortPostDTO shortPostDTO) {
        String text = deviceDAO.findByInventNumber(inventNumber).getDeviceDescription()
                + " " + deviceDAO.findByInventNumber(inventNumber).getRoom();
        model.addAttribute("shortPostDTO", shortPostDTO);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("inventNumber", inventNumber);
        model.addAttribute("text", text);
        return "posts/addPostFormInv";
    }

    public String editFormErr(Model model, String inventNumber, ShortPostDTO dto, Integer id) {
        Map<String, String> mapa = new HashMap<>();
        List<String> keys = deviceDAO.findAll().stream().map(d -> d.getInventNumber()).collect(Collectors.toList());

        for (String key : keys) {
            Device device = deviceDAO.findByInventNumber(key);
            mapa.put(key, device.getDeviceDescription());
        }

        model.addAttribute("newPost", dto);
        model.addAttribute("authors", ListOfEnumValues.authors);
        model.addAttribute("devices", mapa);
        model.addAttribute("id", id);
        return "posts/editPostForm";
    }
}
