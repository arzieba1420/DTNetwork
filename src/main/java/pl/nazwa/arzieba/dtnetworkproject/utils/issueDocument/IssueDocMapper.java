package pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument;

import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import org.springframework.stereotype.Component;

@Component
public class IssueDocMapper {

    public static  IssueDocument map (IssueDocumentDTO documentDTO,DamageDAO damageDAO){
        IssueDocument document = new IssueDocument();
        if (documentDTO.getDamageId()==null){
            document.setDamage(null);
        } else document.setDamage(damageDAO.findById(documentDTO.getDamageId()).orElse(null));
        document.setDelivererName(documentDTO.getDelivererName());
        document.setDelivererNIP(documentDTO.getDelivererNIP());
        document.setIssueDate(CalendarUtil.string2cal(documentDTO.getIssueDate()));
        document.setIssueDetails(documentDTO.getIssueDetails());
        document.setIssueSignature(documentDTO.getIssueSignature());
        document.setIssueTittle(documentDTO.getIssueTittle());
        if(documentDTO.getDamageId()==null)
        document.setInventNumber(documentDTO.getInventNumber());
        else document.setInventNumber(damageDAO.findById(documentDTO.getDamageId()).orElse(null).getDevice().getInventNumber());
        document.setValue(documentDTO.getValue());
        return document;
    }

    public static IssueDocumentDTO map(IssueDocument document){
        IssueDocumentDTO documentDTO = new IssueDocumentDTO();
        if(document.getDamage()==null){
            documentDTO.setDamageId(null);
        }else
        documentDTO.setDamageId(document.getDamage().getDamageId());
        documentDTO.setDelivererName(document.getDelivererName());
        documentDTO.setDelivererNIP(document.getDelivererNIP());
        documentDTO.setInventNumber(document.getInventNumber());
        documentDTO.setIssueTittle(document.getIssueTittle());
        documentDTO.setIssueDate(CalendarUtil.cal2string(document.getIssueDate()));
        documentDTO.setIssueSignature(document.getIssueSignature());
        documentDTO.setIssueDetails(document.getIssueDetails());
        documentDTO.setValue(document.getValue());
        return documentDTO;
    }

}
