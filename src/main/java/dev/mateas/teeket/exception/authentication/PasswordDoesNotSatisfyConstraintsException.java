package dev.mateas.teeket.exception.authentication;

import dev.mateas.teeket.exception.GenericException;

public class PasswordDoesNotSatisfyConstraintsException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "The password must contain at least 8 characters, an uppercase letter, a lowercase letter, a digit and a special symbol.";
    }
}
