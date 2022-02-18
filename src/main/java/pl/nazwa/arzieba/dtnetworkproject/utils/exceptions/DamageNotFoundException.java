package pl.nazwa.arzieba.dtnetworkproject.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class DamageNotFoundException extends RuntimeException{

    public DamageNotFoundException() {
    }

    public DamageNotFoundException(String message) {
        super(message);
    }
}
