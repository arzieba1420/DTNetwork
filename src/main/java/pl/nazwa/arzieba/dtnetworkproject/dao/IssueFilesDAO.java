package pl.nazwa.arzieba.dtnetworkproject.dao;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;

@Repository
public interface IssueFilesDAO extends PagingAndSortingRepository<IssueFiles,Long> {

    IssueFiles save(IssueFiles issueFiles);

}
