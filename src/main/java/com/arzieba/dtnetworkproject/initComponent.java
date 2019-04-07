package com.arzieba.dtnetworkproject;

import com.arzieba.dtnetworkproject.dao.DamageDAO;
import com.arzieba.dtnetworkproject.dao.DeviceCardDAO;
import com.arzieba.dtnetworkproject.dao.DeviceDAO;
import com.arzieba.dtnetworkproject.dao.IssueDocumentDAO;
import com.arzieba.dtnetworkproject.model.Damage;
import com.arzieba.dtnetworkproject.model.Device;
import com.arzieba.dtnetworkproject.model.DeviceCard;
import com.arzieba.dtnetworkproject.model.IssueDocument;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import lombok.*;

import javax.annotation.PostConstruct;
import java.util.Date;

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
        Device device = new Device();
        Damage damage1 = new Damage();
        Damage damage2 = new Damage();
        IssueDocument issueDocument1 = new IssueDocument();
        IssueDocument issueDocument2 = new IssueDocument();
        DeviceCard deviceCard = new DeviceCard();


        device.setDeviceDescription("Opis");
        device.setInventNumber("S-003");
        deviceCard.setDevice(device);
        deviceCardDAO.save(deviceCard);



     /*








        damageDAO.save(damage1);
        damageDAO.save(damage2);
        issueDocumentDAO.save(issueDocument1);
        issueDocumentDAO.save(issueDocument2);*/
        damage1.setAuthor("Arek");
        damage1.setDamageDate(new Date(2018,11,11));
        damage1.setDescription("Przykładowa usterka 1");

        damage1.setDevice(device);
        damage2.setAuthor("Arek2");
        damage2.setDamageDate(new Date(2018,12,12));
        damage2.setDescription("Przykładowa usterka 2");
        damage2.setDevice(device);

        issueDocument1.setIssueSignature("signature 1");
        issueDocument1.setDamage(damage1);
        issueDocument1.setDelivererName("Zeto");
        issueDocument1.setDelivererNIP("99309430");
        issueDocument1.setIssueDate(new Date(2000,01,01));
        issueDocument1.setIssueDetails("Detail 1");
        issueDocument1.setIssueTittle("Title1");
        issueDocument1.setDevice(device);

        issueDocument2.setIssueSignature("signature 2");
        issueDocument2.setDamage(damage1);
        issueDocument2.setDelivererName("Zeto");
        issueDocument2.setDelivererNIP("99309430");
        issueDocument2.setIssueDate(new Date(2000,01,01));
        issueDocument2.setIssueDetails("Detail 2");
        issueDocument2.setIssueTittle("Title2");
        issueDocument2.setDevice(device);

        damageDAO.save(damage1);
        deviceDAO.save(device);
        damageDAO.save(damage2);
        issueDocumentDAO.save(issueDocument1);
        issueDocumentDAO.save(issueDocument1);
        System.out.println();
    }

}
