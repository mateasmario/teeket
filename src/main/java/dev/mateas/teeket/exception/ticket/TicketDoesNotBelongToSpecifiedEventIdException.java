package dev.mateas.teeket.exception.ticket;

import dev.mateas.teeket.exception.GenericException;

public class TicketDoesNotBelongToSpecifiedEventIdException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Ticket does not belong to specified event ID.";
    }
}
