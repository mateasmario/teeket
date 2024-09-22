package dev.mateas.teeket.service;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.entity.Ticket;
import dev.mateas.teeket.exception.event.EventDoesNotBelongToRequesterException;
import dev.mateas.teeket.exception.event.EventWithSpecifiedIdDoesNotExistException;
import dev.mateas.teeket.exception.ticket.TicketWithSpecifiedIdDoesNotExist;
import dev.mateas.teeket.repository.EventRepository;
import dev.mateas.teeket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private EventRepository eventRepository;

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

    public void deleteTicket(String username, String ticketId) throws TicketWithSpecifiedIdDoesNotExist, EventDoesNotBelongToRequesterException {
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (!ticketOptional.isPresent()) {
            throw new TicketWithSpecifiedIdDoesNotExist();
        }

        Ticket ticket = ticketOptional.get();

        Optional<Event> eventOptional = eventRepository.findById(ticket.getEvent());

        if (!eventOptional.get().getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        ticketRepository.delete(ticket);
    }

    public long getAvailableTicketCount(String username, String eventId) throws TicketWithSpecifiedIdDoesNotExist, EventDoesNotBelongToRequesterException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

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
}
