package pl.nazwa.arzieba.dtnetworkproject.services.damage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.nazwa.arzieba.dtnetworkproject.dao.DamageDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.DeviceDAO;
import pl.nazwa.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import pl.nazwa.arzieba.dtnetworkproject.model.Author;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import pl.nazwa.arzieba.dtnetworkproject.utils.calendar.CalendarUtil;
import pl.nazwa.arzieba.dtnetworkproject.utils.damage.DamageMapper;
import pl.nazwa.arzieba.dtnetworkproject.utils.exceptions.DamageNotFoundException;
import pl.nazwa.arzieba.dtnetworkproject.utils.issueDocument.IssueDocMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public DamageDTO findById(Integer id) {
        return damageDAO.findById(id)
                .map(DamageMapper::map).orElse(null);

    }

    @Override
    public int numberOfDamagesByDevice(String inventNumber){

        return damageDAO.findByDevice_InventNumber(inventNumber).size();
    }



    @Override
    public List<Damage> findByDeviceInventNumber(int page, int size,String inventNumber) {

        List<Damage> damagePage = damageDAO.findByDevice_InventNumber(PageRequest.of(page, size, Sort.Direction.DESC,"damageDate"), inventNumber)
                                    .get()
                                    .collect(Collectors.toList());

        return damagePage;


    }

    @Override
    public List<DamageDTO> findByDateBefore(String date) {
        return damageDAO.findByDamageDateBefore(CalendarUtil.string2cal(date))
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByDateAfter(String date) {
        return damageDAO.findByDamageDateAfter(CalendarUtil.string2cal(date))
                .stream()
                .map(DamageMapper::map)
                .collect(Collectors.toList());
    }

    @Override
    public List<DamageDTO> findByAuthor(String author) {
        return damageDAO.findByAuthor(Author.valueOf(author)).stream()
                .map(DamageMapper::map)
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
                .map(DamageDTO::getDamageId)
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


            return DamageMapper.map(toBeUpdated);
        }
    }

    @Override
    public DamageDTO remove(Integer id) {
        if(!findAll().stream()
        .map(DamageDTO::getDamageId)
            .collect(Collectors.toList())
            .contains(id)) throw new DamageNotFoundException("Damage not found");
        else{
            DamageDTO removed = findById(id);
            damageDAO.deleteById(id);
            removed.setDescription("Removed "+removed.getDamageId());
            return removed;
        }
    }

    @Override
    public List<IssueDocumentDTO> getIssueDocuments(Integer id) {
        return issueDocumentDAO.findAll().stream()
                .filter(d->d.getDamage().getDamageId().equals(id))
            .map(IssueDocMapper::map)
                .collect(Collectors.toList());
}



}
