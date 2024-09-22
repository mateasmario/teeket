package dev.mateas.teeket.exception.event;

import dev.mateas.teeket.exception.GenericException;

public class EventDoesNotBelongToRequesterException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Event with specified ID does not belong to you.";
    }
}
