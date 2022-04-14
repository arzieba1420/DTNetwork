package pl.nazwa.arzieba.dtnetworkproject.services.issueDocument;

import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueFiles;


import javax.servlet.http.HttpServletRequest;
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
    String showDocsforDevice(String inventNumber, Model model,int page);
    List<IssueFiles> getFilesForDoc(String signature);
    String addFormDevice(String inventNumber, Model model);
    String addFormDamage(Integer damageId, Model model);
    String createDocumentForDamage(IssueDocumentDTO issueDocumentDTO, BindingResult bindingResult, Model model, HttpServletRequest request);

    String createDocumentForDevice(IssueDocumentDTO issueDocumentDTO, BindingResult bindingResult, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes);
}
