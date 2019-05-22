package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Calendar;
import java.util.List;

@Repository
public interface IssueDocumentDAO extends PagingAndSortingRepository<IssueDocument,String> {

    Page<IssueDocument> findAllByIssueDateBetween(Pageable pageable, Calendar start,Calendar end);
    Page<IssueDocument> findAllByInventNumber(String string, Pageable pageable);
    List<IssueDocument> findAll();
    IssueDocument findByIssueSignature(String signature);
    List<IssueDocument> findByDamage_DamageId(Integer id);
    List<IssueDocument> findByInventNumber(String inventNumber);
    boolean existsByIssueSignature(String signature);




}
