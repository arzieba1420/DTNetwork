package pl.nazwa.arzieba.dtnetworkproject.services.damage;


import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import pl.nazwa.arzieba.dtnetworkproject.dto.DamageDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.GeneratorTestDTO;
import pl.nazwa.arzieba.dtnetworkproject.dto.IssueDocumentDTO;
import org.springframework.stereotype.Service;
import pl.nazwa.arzieba.dtnetworkproject.model.Damage;
import java.util.List;

@Service
public interface DamageService {

    List<DamageDTO> findAll();
    DamageDTO findById(Integer id);
    int numberOfDamagesByDevice(String inventNumber);
    List<Damage> findByDeviceInventNumber(int page, int size, String inventNumber);
    List<DamageDTO> findByDateBefore(String date);
    List<DamageDTO> findByDateAfter(String date);
    List<DamageDTO> findByAuthor(String author);
    DamageDTO create(DamageDTO damageDTO);
    DamageDTO update(DamageDTO damageDTO);
    DamageDTO remove(Integer id);
    List<IssueDocumentDTO> getIssueDocuments(Integer id);
    String addAsModel(DamageDTO damageDTO, BindingResult bindingResult, Model model);
    String createDamageErr(Model model,String inventNumber, DamageDTO damageDTO);
    String getAllForDevice(String inventNumber, Model model, int page);
    String createDamage(Model model,String inventNumber);
    String editDamage(Model model,Integer id);
    String saveEditedDamage(DamageDTO damageDTO, BindingResult bindingResult,Integer id, Model model);
    String addGeneratorActivity(GeneratorTestDTO testDTO, BindingResult bindingResult, Model model );
}

