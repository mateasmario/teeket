package dev.mateas.teeket.exception.authentication;

public class PasswordsDoNotMatchException extends RegisterException {
    @Override
    public String getAuthenticationMessage() {
        return "The two passwords do not match.";
    }
}
