package pl.nazwa.arzieba.dtnetworkproject.services.shortPost;

import pl.nazwa.arzieba.dtnetworkproject.dto.ShortPostDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;

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
    List<ShortPost> findAllByYear(int year);
    Map<Integer,ShortPostDTO> findAllByDevice(String number, int page, int size);

    int numberByYear(int year);
    int numberByDevice(String inventNumber);

    Map<Integer,ShortPostDTO> findAll(int year, int page, int size);

    ShortPostDTO create(ShortPostDTO dto);
    ShortPostDTO update(ShortPostDTO dto);
    ShortPostDTO remove(Integer id);
    Map<Integer, ShortPostDTO> searchContent(String phrase);


    Set<Integer> setOfYears();
}
