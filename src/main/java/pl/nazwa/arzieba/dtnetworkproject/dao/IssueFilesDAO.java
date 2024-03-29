package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;

import java.util.List;

@Repository
public interface IssueFilesDAO extends PagingAndSortingRepository<IssueFiles,Long> {

    IssueFiles findByIssueFilesId(Long id);
    IssueFiles save(IssueFiles issueFiles);
    List<IssueFiles> findAllByIssueDocument_IssueSignature(String signature);
    List<IssueFiles> findAllByIssueDocument_IssueId(Integer id);
}
