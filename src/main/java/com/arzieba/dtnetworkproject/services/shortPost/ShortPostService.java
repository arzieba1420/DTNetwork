package com.arzieba.dtnetworkproject.services.shortPost;

import com.arzieba.dtnetworkproject.dto.ShortPostDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.ShortPost;

import java.util.List;

public interface ShortPostService {
    List<ShortPostDTO> findAll();
    ShortPostDTO findById(Integer id);
    List<ShortPostDTO> findByAuthor(Author author);
    List<ShortPostDTO> findByDevice(String inventNumber);
    List<ShortPostDTO> find5ByDevice(String inventNumber);
    List<ShortPostDTO> findLast5();

    ShortPostDTO create(ShortPostDTO dto);
    ShortPostDTO update(ShortPostDTO dto);
    ShortPostDTO remove(Integer id);


}
