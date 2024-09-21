package dev.mateas.teeket.exception.authentication;

public abstract class RegisterException extends Exception{
    public abstract String getAuthenticationMessage();
}
