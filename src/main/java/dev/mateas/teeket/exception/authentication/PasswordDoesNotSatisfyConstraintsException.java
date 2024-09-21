package dev.mateas.teeket.exception.authentication;

public class PasswordDoesNotSatisfyConstraintsException extends RegisterException {
    @Override
    public String getAuthenticationMessage() {
        return "The password must contain at least 8 characters, an uppercase letter, a lowercase letter, a digit and a special symbol.";
    }
}
