package com.arzieba.dtnetworkproject.services.issueDocument;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import com.arzieba.dtnetworkproject.model.IssueDocument;
import com.arzieba.dtnetworkproject.services.device.DeviceServiceImpl;
import com.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class IssueDocServiceImpl implements IssueDocService {
    @Autowired
    public IssueDocServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
    }

    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;



    @Override
    public List<IssueDocumentDTO> findAll() {
        return issueDocumentDAO.findAll().stream()
                .map(d-> IssueDocMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public IssueDocumentDTO findBySignature(String signature) {
        if(issueDocumentDAO.existsByIssueSignature(signature))
        return IssueDocMapper.map(issueDocumentDAO.findByIssueSignature(signature));
        else throw new IssueDocumentNotFound("Issue document not found");
    }

    @Override
    public List<IssueDocumentDTO> findByInventNumber(String inventNumber) {
        return issueDocumentDAO.findByInventNumber(inventNumber)
                .stream()
                .map(d->IssueDocMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDocumentDTO> findByDamageId(Integer id) {
        return issueDocumentDAO.findByDamage_DamageId(id).stream()
                .map(d->IssueDocMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public IssueDocumentDTO create(IssueDocumentDTO documentDTO) {
        IssueDocument saved = issueDocumentDAO.save(IssueDocMapper.map(documentDTO,damageDAO));
        return IssueDocMapper.map(saved);
    }

    @Override
    public IssueDocumentDTO update(IssueDocumentDTO documentDTO) {
        if(!issueDocumentDAO.existsByIssueSignature(documentDTO.getIssueSignature())){
            return  create(documentDTO);

        } else{
            IssueDocument toBeUpdated = issueDocumentDAO.findByIssueSignature(documentDTO.getIssueSignature());
            toBeUpdated.setIssueDetails(documentDTO.getIssueDetails());
            toBeUpdated.setValue(documentDTO.getValue());
            return IssueDocMapper.map(toBeUpdated);
        }
    }

    @Override
    public IssueDocumentDTO remove(String signature) {
        if(!issueDocumentDAO.existsByIssueSignature(signature)){
            throw new IssueDocumentNotFound("Issue Document not found!");
        } else{
            IssueDocumentDTO removed = findBySignature(signature);
            issueDocumentDAO.delete(issueDocumentDAO.findByIssueSignature(signature));
            removed.setIssueTittle("Removed "+removed.getIssueTittle());
            return removed;

        }
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    private class IssueDocumentNotFound extends RuntimeException {
        public IssueDocumentNotFound() {
        }

        public IssueDocumentNotFound(String message) {
            super(message);
        }
    }
}
