package com.arzieba.dtnetworkproject.services.damage;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.model.Author;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.IssueDocument;
import com.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import com.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class DamageServiceImpl implements DamageService {
    @Autowired
    public DamageServiceImpl(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO) {
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
    public List<DamageDTO> findAll() {
        return damageDAO.findAll()
                .stream()
                .map(d -> DamageMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public DamageDTO findById(Integer id) {
        return damageDAO.findById(id)
                .map(d -> DamageMapper.map(d)).orElse(null);

    }

    @Override
    public List<DamageDTO> findByDeviceInventNumber(String inventNumber) {
        return damageDAO.findByDevice_InventNumber(inventNumber)
                .stream()
                .map(d->DamageMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByDateBefore(String date) {
        return damageDAO.findByDamageDateBefore(CalendarUtil.string2cal(date))
                .stream()
                .map(d->DamageMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByDateAfter(String date) {
        return damageDAO.findByDamageDateAfter(CalendarUtil.string2cal(date))
                .stream()
                .map(d->DamageMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByAuthor(String author) {
        return damageDAO.findByAuthor(Author.valueOf(author)).stream()
                .map(d->DamageMapper.map(d))
                .collect(Collectors.toList());
    }

    @Override
    public DamageDTO create(DamageDTO damageDTO) {
       Damage saved = damageDAO.save(DamageMapper.map(damageDTO, deviceDAO));
        damageDTO.setDamageId(saved.getDamageId());
        return damageDTO;
    }

    @Override
    public DamageDTO update(DamageDTO damageDTO) {
        if(!    findAll().stream()
                .map(d->d.getDamageId())
                .collect(Collectors.toList())
                .contains(damageDTO.getDamageId())){
            return  create(damageDTO);

        } else{
            Damage toBeUpdated = damageDAO.findById(damageDTO.getDamageId()).orElse(null);
            String lastDescription = toBeUpdated.getDescription();
            String dateOfUpdating = damageDTO.getDamageDate();
            toBeUpdated.setDescription(lastDescription
                    +"\n\n"+"[UPDATE] "+dateOfUpdating+"\n"+ damageDTO.getDescription());
            toBeUpdated.setAuthor(damageDTO.getAuthor());
            damageDAO.save(toBeUpdated);
            System.out.println(toBeUpdated.getDescription());

            return DamageMapper.map(toBeUpdated);
        }
    }

    @Override
    public DamageDTO remove(Integer id) {
        if(!findAll().stream()
        .map(d->d.getDamageId())
            .collect(Collectors.toList())
            .contains(id)) throw new DamageNotFoundException("Damage not found");
        else{
            DamageDTO removed = findById(id);
            damageDAO.deleteById(id);
            removed.setDescription("Removed "+removed.getDamageId());
            return removed;
        }
    }
    //Todo method with issuedoc
    @Override
    public List<IssueDocument> getIssueDocuments(Integer id) {
        return null;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    public static class DamageNotFoundException extends RuntimeException{
        public DamageNotFoundException() {
        }

        public DamageNotFoundException(String message) {
            super(message);
        }
    }
}
