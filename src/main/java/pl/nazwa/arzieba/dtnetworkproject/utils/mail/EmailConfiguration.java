package pl.nazwa.arzieba.dtnetworkproject.utils.mail;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class EmailConfiguration  {
    @Autowired
    private JavaMailSender mailSender;

    public void sendMail(String[] to, String subject, String message, String author){

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom("DTNetwork");
        mailMessage.setTo(to);
        mailMessage.setSubject("Nowy wpis dla: "+subject);
        mailMessage.setText(message+ "\n\n\ndodany przez: "+author);
        mailSender.send(mailMessage);

    }


}
