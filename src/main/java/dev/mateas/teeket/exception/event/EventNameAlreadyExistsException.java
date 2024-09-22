package dev.mateas.teeket.exception.event;

import dev.mateas.teeket.exception.GenericException;

public class EventNameAlreadyExistsException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Event name already exists.";
    }
}
