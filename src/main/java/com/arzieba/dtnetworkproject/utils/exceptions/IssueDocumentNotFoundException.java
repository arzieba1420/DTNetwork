package com.arzieba.dtnetworkproject.utils.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class IssueDocumentNotFoundException extends RuntimeException {
    public IssueDocumentNotFoundException() {
    }

    public IssueDocumentNotFoundException(String message) {
        super(message);
    }
}