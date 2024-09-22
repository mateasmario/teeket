package dev.mateas.teeket.exception.ticket;

import dev.mateas.teeket.exception.GenericException;

public class TicketWithSpecifiedIdDoesNotExist extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Ticket with specified ID does not exist.";
    }
}
