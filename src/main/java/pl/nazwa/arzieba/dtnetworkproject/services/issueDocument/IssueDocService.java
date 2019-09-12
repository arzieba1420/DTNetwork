package pl.nazwa.arzieba.dtnetworkproject.services.issueDocument;

import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;


import java.util.List;
import java.util.Set;

@Service
public interface IssueDocService {
    List<IssueDocumentDTO> findAll();
    IssueDocumentDTO findBySignature(String signature);
    List<IssueDocumentDTO> findByInventNumber(String inventNumber);
    List<IssueDocumentDTO> findByYear(int year, int page, int size);
    List<IssueDocumentDTO> findByDamageId(Integer id);
    IssueDocumentDTO create(IssueDocumentDTO documentDTO);



    IssueDocumentDTO createWithFiles(IssueDocumentDTO documentDTO);

    IssueDocumentDTO update(IssueDocumentDTO documentDTO);
    IssueDocumentDTO remove(String signature);
    List<IssueDocumentDTO> findByDevice(String inv, int page, int size);
    Set<Integer> setOfYears();

    Set<Integer> subSet();

    int numberByDevice(String inv);
    int numberByYear(int year);

    List<IssueFiles> getFilesForDoc(String signature);
}
