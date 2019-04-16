package com.arzieba.dtnetworkproject.services.damage;

import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.dto.DeviceDTO;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.model.IssueDocument;
import org.springframework.stereotype.Service;

import java.util.Calendar;
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

    List<IssueDocument> getIssueDocuments(Integer id);

}

