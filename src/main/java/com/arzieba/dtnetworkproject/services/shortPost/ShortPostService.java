package com.arzieba.dtnetworkproject.services.shortPost;

import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.ShortPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ShortPostService {
    List<ShortPostDTO> findAll();
    ShortPostDTO findById(Integer id);
    List<ShortPostDTO> findByAuthor(Author author);
    List<ShortPostDTO> findByDevice(String inventNumber);
    List<ShortPostDTO> find5ByDevice(String inventNumber);
    List<ShortPostDTO> findLast5();
    List<ShortPostDTO> findAll(int items,int size);

    int numberByYear(int year);

    Map<Integer,ShortPostDTO> findAll(int year, int page, int size);

    ShortPostDTO create(ShortPostDTO dto);
    ShortPostDTO update(ShortPostDTO dto);
    ShortPostDTO remove(Integer id);


    Set<Integer> setOfYears();
}
