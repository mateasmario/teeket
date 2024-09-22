package dev.mateas.teeket.exception.event;

import dev.mateas.teeket.exception.GenericException;

public class EventNameTooShortException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "The name of the event must be of at least 8 characters.";
    }
}
