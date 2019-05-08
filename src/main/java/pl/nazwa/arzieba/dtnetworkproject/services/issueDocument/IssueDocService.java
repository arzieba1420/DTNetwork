package pl.nazwa.arzieba.dtnetworkproject.services.issueDocument;

import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Set;

@Service
public interface IssueDocService {
    List<IssueDocumentDTO> findAll();
    IssueDocumentDTO findBySignature(String signature);
    List<IssueDocumentDTO> findByInventNumber(String inventNumber);
    List<IssueDocumentDTO> findByYear(int year);
    List<IssueDocumentDTO> findByDamageId(Integer id);
    IssueDocumentDTO create(IssueDocumentDTO documentDTO);
    IssueDocumentDTO update(IssueDocumentDTO documentDTO);
    IssueDocumentDTO remove(String signature);
    Set<Integer> setOfYears();

}
