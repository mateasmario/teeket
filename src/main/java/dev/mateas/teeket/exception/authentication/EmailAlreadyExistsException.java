package dev.mateas.teeket.exception.authentication;

public class EmailAlreadyExistsException extends RegisterException {
    @Override
    public String getAuthenticationMessage() {
        return "The specified email address already exists.";
    }
}
