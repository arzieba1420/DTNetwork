package pl.nazwa.arzieba.dtnetworkproject.services.damage;

import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface DamageService {

    List<DamageDTO> findAll();
    DamageDTO findById(Integer id);
    List<DamageDTO> findByDeviceInventNumber(String inventNumber);
    List<DamageDTO> findByDateBefore(String date);
    List<DamageDTO> findByDateAfter(String date);
    List<DamageDTO> findByAuthor(String author);
    DamageDTO create(DamageDTO damageDTO);
    DamageDTO update(DamageDTO damageDTO);
    DamageDTO remove(Integer id);

    List<IssueDocumentDTO> getIssueDocuments(Integer id);

}

