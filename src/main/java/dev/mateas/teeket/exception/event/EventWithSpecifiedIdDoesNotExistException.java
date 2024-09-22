package dev.mateas.teeket.exception.event;

import dev.mateas.teeket.exception.GenericException;

public class EventWithSpecifiedIdDoesNotExistException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Event with specified ID does not exist.";
    }
}
