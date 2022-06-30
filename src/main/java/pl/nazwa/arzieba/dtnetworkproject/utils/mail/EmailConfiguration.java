package pl.nazwa.arzieba.dtnetworkproject.utils.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;

import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.configuration.SecurityConfig;
import pl.nazwa.arzieba.dtnetworkproject.controllers.DownloadController;
import pl.nazwa.arzieba.dtnetworkproject.dao.UserDAO;
import pl.nazwa.arzieba.dtnetworkproject.dto.LeaveApplyPreparer;
import pl.nazwa.arzieba.dtnetworkproject.services.load.LoadServiceImpl;


import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@Slf4j
public class EmailConfiguration  {

    private JavaMailSender mailSender;
    private JavaMailSenderImpl mailAckSender;
    private String mailSet;
    private String receivers;
    private UserDAO userDAO;






    @Autowired
    EmailConfiguration(JavaMailSenderImpl mailSender, JavaMailSenderImpl mailAckSender, UserDAO userDAO) {
    this.mailSender = prepareSender(mailSender);
    this.mailAckSender = prepareAckSender(mailSender);
        this.userDAO = userDAO;



        try {
            mailSet = loadProperty("my.mailSet");
            log.info(mailSet + "----------------------------------------");
            receivers = loadProperty("my.mailReceivers");
            log.info(receivers + "----------------------------------------");
        } catch (IOException e) {
            log.warn("An error occurred while loading properties from config.property file! No such property or file does not exists!");
        }
    }

    @Async
    public void sendMail(String subject, String message, String author){
        try {
            if (mailSet.equalsIgnoreCase("mail")) {
                SimpleMailMessage mailMessage = new SimpleMailMessage();
                mailMessage.setFrom("DTNetwork");
                mailMessage.setTo(receivers.split(","));
                mailMessage.setSubject(subject);
                mailMessage.setText(message + "\n\ndodany przez: " + author);
                mailSender.send(mailMessage);
            } else log.info("MailSender is off. Should be: my.mailSet = mail");
        } catch (MailSendException | MailParseException |NullPointerException e){
            log.warn("MailSender is not configured properly. Should be: my.mailSet = mail and my.mailReceivers = example1@mail.com, example2@mail.com");
        }
    }

    @Async
    public void sendAckMail(LeaveApplyPreparer applyPreparer){
        String username = SecurityConfig.username;
        mailAckSender.setUsername(userDAO.findByUsername(applyPreparer.getUsername()).getMailUsername());
        mailAckSender.setPassword(userDAO.findByUsername(applyPreparer.getUsername()).getMailPassword());
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(mailAckSender.getUsername()+"@cyf-kr.edu.pl");
        mailMessage.setSubject("Wniosek o urlop wypoczynkowy");
        mailMessage.setText(applyPreparer.getText()+"\n\n"+userDAO.findByUsername(applyPreparer.getUsername()).getSigning());
        mailMessage.setTo("enarkadiuszzieba@gmail.com");
        mailMessage.setCc("luzak021@gmail.com");

        mailAckSender.send(mailMessage);
    };

    private String loadProperty (String property) throws IOException {
        Properties mailProperties = new Properties();
        FileInputStream file;
        String path = DownloadController.STORAGE_PATH+"/config.properties.txt";
        log.warn(path + "--------------------------------");
        file = new FileInputStream(path);
        mailProperties.load(file);
        file.close();
        return mailProperties.getProperty(property);
    }

    private JavaMailSenderImpl prepareSender(JavaMailSenderImpl mailSender){
        try {
            mailSender.setPassword(loadProperty("my.password"));
            mailSender.setUsername(loadProperty("my.username"));
        } catch (IOException e) {
            log.warn("An error occurred while loading properties from config.property file! No such property or file does not exists!");
        } finally {
            return mailSender;
        }
    }

    private JavaMailSenderImpl prepareAckSender(JavaMailSenderImpl mailSender){

        mailSender.setUsername("");
        mailSender.setPassword("");
        mailSender.setPort(587);
        mailSender.setHost("kinga.cyf-kr.edu.pl");
        mailSender.setProtocol("smtp");
        return mailSender;
    }

    private String fetchUsername(){

        String loggedUserName= LoadServiceImpl.getUser();
        System.out.println();
        return userDAO.findByUsername(loggedUserName).getMailUsername();
    }

    private String fetchPassword(){
        String loggedUserName=LoadServiceImpl.getUser();
        return userDAO.findByUsername(loggedUserName).getMailPassword();
    }
}
