package dev.mateas.teeket.exception.ticket;

import dev.mateas.teeket.exception.GenericException;

public class EventCodeIsInvalidException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Event moderation code is invalid.";
    }
}
