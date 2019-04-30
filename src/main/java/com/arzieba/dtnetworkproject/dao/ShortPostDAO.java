package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.ShortPost;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShortPostDAO extends CrudRepository<ShortPost,Integer> {

    List<ShortPost> findAll();
    ShortPost findByPostId(Integer id);
    List<ShortPost> findTop5ByOrderByDateDesc();
    List<ShortPost> findAllByAuthor(Author author);
    List<ShortPost> findAllByDevice_InventNumber(String number);
    List<ShortPost> findAllByDevice_InventNumberOrderByDateDesc(String number);
    List<ShortPost> findTop5ByDevice_InventNumberOrderByDateDesc(String number);
    void removeByPostId(Integer id);
}
