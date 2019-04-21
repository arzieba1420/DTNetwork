package com.arzieba.dtnetworkproject.services.issueDocument;

import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public interface IssueDocService {
    List<IssueDocumentDTO> findAll();
    IssueDocumentDTO findBySignature(String signature);
    List<IssueDocumentDTO> findByInventNumber(String inventNumber);
    List<IssueDocumentDTO> findByDamageId(Integer id);
    IssueDocumentDTO create(IssueDocumentDTO documentDTO);
    IssueDocumentDTO update(IssueDocumentDTO documentDTO);
    IssueDocumentDTO remove(String signature);

}