package pl.nazwa.arzieba.dtnetworkproject.utils.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class EmailConfiguration  {

    private final JavaMailSender mailSender;

    @Autowired
    EmailConfiguration(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendMail(String[] to, String subject, String message, String author){
        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom("DTNetwork");
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(message+ "\n\n\ndodany przez: "+author);
        mailSender.send(mailMessage);
    }
}
