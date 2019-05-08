package pl.nazwa.arzieba.dtnetworkproject.services.issueDocument;

import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.IssueDocument;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.IssueDocumentNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
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
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public IssueDocumentDTO findBySignature(String signature) {
        if(issueDocumentDAO.existsByIssueSignature(signature))
        return IssueDocMapper.map(issueDocumentDAO.findByIssueSignature(signature));
        else throw new IssueDocumentNotFoundException("Issue document not found");
    }

    @Override
    public List<IssueDocumentDTO> findByInventNumber(String inventNumber) {
        return issueDocumentDAO.findByInventNumber(inventNumber)
                .stream()
                .map(IssueDocMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<IssueDocumentDTO> findByYear(int year) {




        List<IssueDocumentDTO> list= issueDocumentDAO.findAll().stream()
                .filter(d->d.getIssueDate().get(Calendar.YEAR) ==year)
                .map(d->IssueDocMapper.map(d))
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public List<IssueDocumentDTO> findByDamageId(Integer id) {
        return issueDocumentDAO.findByDamage_DamageId(id).stream()
                .map(IssueDocMapper::map)
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
        if (!issueDocumentDAO.existsByIssueSignature(signature)) {
            throw new IssueDocumentNotFoundException("Issue Document not found!");
        } else {
            IssueDocumentDTO removed = findBySignature(signature);
            issueDocumentDAO.delete(issueDocumentDAO.findByIssueSignature(signature));
            removed.setIssueTittle("Removed " + removed.getIssueTittle());
            return removed;

        }
    }

    @Override
        public Set<Integer> setOfYears(){

            List<Integer> yearsList= issueDocumentDAO.findAll().stream()
                    .map(d->d.getIssueDate().get( Calendar.YEAR))
                    .collect(Collectors.toList());
            Set<Integer> years =yearsList.stream().collect(Collectors.toSet());
            TreeSet<Integer> sortedSet = new TreeSet<>();
            sortedSet.addAll(years);
            //test

            return sortedSet;
        }



}