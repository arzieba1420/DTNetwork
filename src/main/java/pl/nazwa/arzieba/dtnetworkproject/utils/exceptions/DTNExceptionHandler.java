package pl.nazwa.arzieba.dtnetworkproject.utils.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.mail.SendFailedException;

@ControllerAdvice
@Slf4j
public class DTNExceptionHandler {

    @ExceptionHandler(MailSenderException.class)
    public void mailSenderExcHandler(MailSenderException mailSenderException){
        log.warn(mailSenderException.getMessage());
    }

    @ExceptionHandler(MailSendException.class)
    public void mailSendExceptionHandler(MailSendException mailSendException){
        log.warn(mailSendException.getMessage());
    }

    @ExceptionHandler(SendFailedException.class)
    public void sendFailedExceptionHandler(SendFailedException sendFailedException){
        log.warn(sendFailedException.getMessage());
    }
}
