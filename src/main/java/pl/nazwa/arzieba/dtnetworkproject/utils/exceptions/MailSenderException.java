package pl.nazwa.arzieba.dtnetworkproject.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MailSenderException extends RuntimeException {

    public MailSenderException() {
    }

    public MailSenderException(String message) {
        super(message);
    }
}
