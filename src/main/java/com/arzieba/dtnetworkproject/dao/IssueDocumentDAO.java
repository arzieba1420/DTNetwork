package com.arzieba.dtnetworkproject.dao;

import com.arzieba.dtnetworkproject.model.IssueDocument;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IssueDocumentDAO extends PagingAndSortingRepository<IssueDocument,String> {
    List<IssueDocument> findAll();
    IssueDocument findByIssueSignature(String signature);
    List<IssueDocument> findByDamage_DamageId(Integer id);
    List<IssueDocument> findByInventNumber(String inventNumber);
    boolean existsByIssueSignature(String signature);




}
