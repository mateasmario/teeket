package dev.mateas.teeket.exception.ticket;

import dev.mateas.teeket.exception.GenericException;

public class CouldNotGenerateTicketException extends GenericException {
    @Override
    public String getAdditionalMessage() {
        return "Could not generate ticket.";
    }
}
