package pl.nazwa.arzieba.dtnetworkproject.dao;

import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.ShortPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface ShortPostDAO extends PagingAndSortingRepository<ShortPost,Integer> {

    List<ShortPost> findAll();
    Page<ShortPost> findAll(Pageable pageRequest);

    Page<ShortPost> findAllByPostDateBetween (Pageable pageRequest, Calendar start, Calendar end);
    List<ShortPost> findAllByPostDateBetween ( Calendar start, Calendar end);
    ShortPost findByPostId(Integer id);
    List<ShortPost> findTop10ByOrderByDateDesc();
    List<ShortPost> findAllByAuthor(Author author);
    List<ShortPost> findAllByDevice_InventNumber(String number);
    Page<ShortPost> findAllByDevice_InventNumber(String inventNumber, Pageable pageable);
    List<ShortPost> findAllByDevice_InventNumberOrderByDateDesc(String number);
    List<ShortPost> findTop10ByDevice_InventNumberOrderByDateDesc(String number);
    List<ShortPost> findByContentContainingIgnoreCaseOrderByPostDateDesc(String phrase);
    void removeByPostId(Integer id);
}
