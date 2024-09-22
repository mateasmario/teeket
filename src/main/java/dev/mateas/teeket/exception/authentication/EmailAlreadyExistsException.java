package dev.mateas.teeket.exception.authentication;

import dev.mateas.teeket.exception.GenericException;

public class EmailAlreadyExistsException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "The specified email address already exists.";
    }
}
