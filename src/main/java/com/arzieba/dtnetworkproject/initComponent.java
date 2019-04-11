package com.arzieba.dtnetworkproject;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.model.*;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.*;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class initComponent {
    private DeviceDAO deviceDAO;
    private DamageDAO damageDAO;
    private IssueDocumentDAO issueDocumentDAO;
    private DeviceCardDAO deviceCardDAO;

    @Autowired
    public initComponent(DeviceDAO deviceDAO, DamageDAO damageDAO, IssueDocumentDAO issueDocumentDAO, DeviceCardDAO deviceCardDAO) {
        this.deviceDAO = deviceDAO;
        this.damageDAO = damageDAO;
        this.issueDocumentDAO = issueDocumentDAO;
        this.deviceCardDAO = deviceCardDAO;
    }

    @PostConstruct
    public void init(){
      /*  Device device = new Device();
        Damage damage1 = new Damage();
        Damage damage2 = new Damage();
        IssueDocument issueDocument1 = new IssueDocument();
        IssueDocument issueDocument2 = new IssueDocument();



        device.setDeviceDescription("Opis");
        device.setInventNumber("S-003");
        device.setRoom(Room.G39);





        damage1.setAuthor("Arek");
        damage1.setDamageDate(new GregorianCalendar(2018,11,11));
        damage1.setDescription("Przykładowa usterka 1");
        damage1.setDevice(device);

        damage2.setAuthor("Arek2");
        damage2.setDamageDate(new GregorianCalendar(2018,12,12));
        damage2.setDescription("Przykładowa usterka 2");
        damage2.setDevice(device);

        issueDocument1.setDamage(damage1);
        issueDocument1.setIssueSignature("signature 1");
        issueDocument1.setDamage(damage1);
        issueDocument1.setDelivererName("Zeto");
        issueDocument1.setDelivererNIP("99309430");
        issueDocument1.setIssueDate(new GregorianCalendar(2000,01,01));
        issueDocument1.setIssueDetails("Detail 1");
        issueDocument1.setIssueTittle("Title1");


        issueDocument2.setDamage(damage2);
        issueDocument2.setIssueSignature("signature 2");
        issueDocument2.setDamage(damage1);
        issueDocument2.setDelivererName("Zeto");
        issueDocument2.setDelivererNIP("99309430");
        issueDocument2.setIssueDate(new GregorianCalendar(2000,01,01));
        issueDocument2.setIssueDetails("Detail 2");
        issueDocument2.setIssueTittle("Title2");

        deviceDAO.save(device);
        deviceCardDAO.save(deviceCard);
        damageDAO.save(damage1);
        damageDAO.save(damage2);
        issueDocumentDAO.save(issueDocument1);
        issueDocumentDAO.save(issueDocument2);*/

       /* deviceCardDAO.findById(1).get().getDevice();*/
      /*  System.out.println(deviceCardDAO.findAll().get(0).getDevice());*/
        /*System.out.println(deviceDAO.findAll().get(1).getDeviceCard());*/
        /*Integer i= deviceDAO.findAll().get(0).getDeviceCard().getDeviceCardID();
        System.out.println(i);
        System.out.println(deviceCardDAO.findByDeviceCardID(i).getDevice().getInventNumber());
        System.out.println(deviceDAO.findAll().get(0).getDeviceCard().getAddress());



        System.out.println(deviceDAO.findAll().get(0).getDamageList().get(0));

       deviceCardDAO.delete(deviceCard);

        try {
            Integer j= deviceDAO.findAll().get(0).getDeviceCard().getDeviceCardID();
            System.out.println(j);
        } catch (NullPointerException e) {
            System.out.println("Brak urządzeń");
        }*/


      /* List<Damage> damages = damageDAO.findByDevice(device);
        System.out.println(damages.size());
        damages.stream().forEach(d->d.setDevice(null));
        System.out.println(damage1.getDevice());
        List<Damage> damagesToRemove= new ArrayList<>();
        damagesToRemove = (List<Damage>) damageDAO.findAll().stream().filter(d->d.getDevice()==null).collect(Collectors.toList());

        damagesToRemove.stream().forEach(d->damageDAO.delete(d));
*/






    }
    @PreDestroy
    public void teardown(){}



}
