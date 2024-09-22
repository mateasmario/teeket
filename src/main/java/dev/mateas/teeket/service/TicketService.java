package dev.mateas.teeket.service;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.exception.event.EventDoesNotBelongToRequesterException;
import dev.mateas.teeket.exception.event.EventWithSpecifiedIdDoesNotExistException;
import dev.mateas.teeket.exception.ticket.CouldNotGenerateTicketException;
import dev.mateas.teeket.exception.ticket.TicketWithSpecifiedIdDoesNotExist;
import dev.mateas.teeket.repository.EventRepository;
import dev.mateas.teeket.repository.TicketRepository;
import dev.mateas.teeket.type.TicketStatus;
import dev.mateas.teeket.util.QRCodeGenerator;
import dev.mateas.teeket.util.StringGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private StringGenerator stringGenerator;
    @Autowired
    private QRCodeGenerator qrCodeGenerator;

    public List<Ticket> getTickets(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        Event event = eventOptional.get();
        if (!event.getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        List<Ticket> ticketList = ticketRepository.findByEvent(eventId);

        return ticketList;
    }

    public void deleteTicket(String username, String ticketId) throws TicketWithSpecifiedIdDoesNotExist, EventDoesNotBelongToRequesterException, EventWithSpecifiedIdDoesNotExistException {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (!ticketOptional.isPresent()) {
            throw new TicketWithSpecifiedIdDoesNotExist();
        }

        Ticket ticket = ticketOptional.get();

        Optional<Event> eventOptional = eventRepository.findById(ticket.getEvent());

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        ticketRepository.delete(ticket);
    }

    public long getAvailableTicketCount(String username, String eventId) throws TicketWithSpecifiedIdDoesNotExist, EventDoesNotBelongToRequesterException, EventWithSpecifiedIdDoesNotExistException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        Event event = eventOptional.get();
        long currentCount = ticketRepository.countByEvent(event.getId());

        return switch (event.getEventType()) {
            case STANDARD -> 50 - currentCount;
            case EXTENDED -> 100 - currentCount;
            case OVERCROWDED -> 250 - currentCount;
        };
    }

    public List<Ticket> generateTickets(String username, String eventId, int ticketCount) throws EventDoesNotBelongToRequesterException, EventWithSpecifiedIdDoesNotExistException, CouldNotGenerateTicketException {
        List<Ticket> ticketList = new ArrayList<>();
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        int currentTicketCount = 0;
        while(currentTicketCount < ticketCount) {
            try {
                Ticket ticket = generateCodeAndQR(eventId);
                ticketList.add(ticket);
                ticketRepository.insert(ticket);
            } catch (IOException e) {
                throw new CouldNotGenerateTicketException();
            }
            currentTicketCount++;
        }

        return ticketList;
    }

    private Ticket generateCodeAndQR(String eventId) throws IOException {
        String ticketId = stringGenerator.generateString(10);
        qrCodeGenerator.generateQRCode(eventId, ticketId);
        return new Ticket(ticketId, LocalDateTime.now(), TicketStatus.VALID, eventId);
    }
}
