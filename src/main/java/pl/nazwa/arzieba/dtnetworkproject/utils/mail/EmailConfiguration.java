package pl.nazwa.arzieba.dtnetworkproject.utils.mail;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.nazwa.arzieba.dtnetworkproject.controllers.DownloadController;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@Component
@Slf4j
public class EmailConfiguration  {

    private  JavaMailSender mailSender;
    private String mailSet;
    private String receivers;

    @Autowired
    EmailConfiguration(JavaMailSenderImpl mailSender) {
    this.mailSender = prepareSender(mailSender);

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
                mailMessage.setText(message + "\n\n\ndodany przez: " + author);
                mailSender.send(mailMessage);
            } else log.info("MailSender is off. Should be: my.mailSet = mail");
        } catch (MailSendException | MailParseException |NullPointerException e){
            log.warn("MailSender is not configured properly. Should be: my.mailSet = mail and my.mailReceivers = example1@mail.com, example2@mail.com");
        }
    }

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



}
