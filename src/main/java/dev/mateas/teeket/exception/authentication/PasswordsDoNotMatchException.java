package dev.mateas.teeket.exception.authentication;

import dev.mateas.teeket.exception.GenericException;

public class PasswordsDoNotMatchException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "The two passwords do not match.";
    }
}
