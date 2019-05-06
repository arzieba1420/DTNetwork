package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.ShortPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortPostDAO extends PagingAndSortingRepository<ShortPost,Integer> {

    List<ShortPost> findAll();
    Page<ShortPost> findAll(Pageable pageRequest);
    ShortPost findByPostId(Integer id);
    List<ShortPost> findTop10ByOrderByDateDesc();
    List<ShortPost> findAllByAuthor(Author author);
    List<ShortPost> findAllByDevice_InventNumber(String number);
    List<ShortPost> findAllByDevice_InventNumberOrderByDateDesc(String number);
    List<ShortPost> findTop10ByDevice_InventNumberOrderByDateDesc(String number);
    void removeByPostId(Integer id);
}
