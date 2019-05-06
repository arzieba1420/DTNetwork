package com.arzieba.dtnetworkproject.services.shortPost;

import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.ShortPostDAO;
import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.ShortPost;
import com.arzieba.dtnetworkproject.utils.shortPost.ShortPostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    public ShortPostServiceImpl(ShortPostDAO postDAO, DeviceDAO deviceDAO) {
        this.postDAO = postDAO;
        this.deviceDAO = deviceDAO;
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
    public int numberByYear(int year){
       long l= postDAO.findAll().stream()
                .filter(d->d.getPostDate().get(Calendar.YEAR)==year)
                .count();

        return (int)l;

    }

    @Override
    public Map<Integer,ShortPostDTO> findAll(int year, int page, int size) {
        Map<Integer,ShortPostDTO> mapa = new LinkedHashMap<>();
        Page<ShortPost> page1 = postDAO.findAll(new PageRequest(page,size, Sort.Direction.DESC,"postDate"));
        Set<Integer> keys = postDAO.findAll(new PageRequest(page,size, Sort.Direction.DESC,"postDate"))
                .stream()
                .filter(d->d.getPostDate().get(Calendar.YEAR)==year)
                .map(d->d.getPostId())
                .collect(Collectors.toSet());
        for (Integer key: keys) {
            mapa.put(key,findById(key));
        }

        return mapa;
    }


    @Override
    public ShortPostDTO create(ShortPostDTO dto) {
        postDAO.save(ShortPostMapper.map(dto,deviceDAO));
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
    public Set<Integer> setOfYears(){

        List<Integer> yearsList= postDAO.findAll().stream()
                .map(d->d.getPostDate().get( Calendar.YEAR))
                .collect(Collectors.toList());
        Set<Integer> years =yearsList.stream().collect(Collectors.toSet());
        TreeSet<Integer> sortedSet = new TreeSet<>();
        sortedSet.addAll(years);

        return sortedSet;
    }
}
