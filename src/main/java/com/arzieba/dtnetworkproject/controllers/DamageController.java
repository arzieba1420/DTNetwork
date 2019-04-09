package com.arzieba.dtnetworkproject.controllers;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dto.DamageDTO;
import com.arzieba.dtnetworkproject.model.Damage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/damage")
public class DamageController {

    private DamageDAO damageDAO;
    private DeviceDAO deviceDAO;

    @Autowired
    public DamageController(DamageDAO damageDAO, DeviceDAO deviceDAO) {
        this.damageDAO = damageDAO;
        this.deviceDAO = deviceDAO;
    }

    @PostMapping
    public ResponseEntity<DamageDTO> add(@RequestBody DamageDTO damageDTO){

          damageDAO.save(damageDTO.mapper(damageDTO,deviceDAO));
         return ResponseEntity.ok(damageDTO);
    };

    @GetMapping("/getAll")
    public ResponseEntity<List<DamageDTO>> getAll(){

      List<Damage> damages = damageDAO.findAll();
      List<DamageDTO> dtos = new ArrayList<>();

        for (Damage damage: damages  ) {
            dtos.add(Damage.mapper(damage));
        }

      return ResponseEntity.ok(dtos);
    };
}
