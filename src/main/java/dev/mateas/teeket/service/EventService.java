package dev.mateas.teeket.service;

import dev.mateas.teeket.dto.EventDto;
import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.exception.event.EventNameAlreadyExistsException;
import dev.mateas.teeket.exception.event.EventNameTooShortException;
import dev.mateas.teeket.exception.event.EventDoesNotBelongToRequesterException;
import dev.mateas.teeket.exception.event.EventWithSpecifiedIdDoesNotExistException;
import dev.mateas.teeket.repository.EventRepository;
import dev.mateas.teeket.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private TicketRepository ticketRepository;

    public List<Event> getEvents(String username) {
        List<Event> eventList = eventRepository.findByOwner(username);
        return eventList;
    }

    public void createEvent(String username, EventDto eventDto) throws EventNameAlreadyExistsException, EventNameTooShortException {
        List<Event> eventList = eventRepository.findByOwner(username);

        String eventName = eventDto.getName().trim();

        if (eventName.length() < 8) {
            throw new EventNameTooShortException();
        }

        if (eventList.size() > 0) {
            for (Event event :
                    eventList) {
                if (eventDto.getName().equals(event.getName())) {
                    throw new EventNameAlreadyExistsException();
                }
            }
        }

        Event event = new Event(username, eventDto.getName(), eventDto.getEventType(), LocalDateTime.now());
        eventRepository.insert(event);
    }

    public void deleteEvent(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (!eventOptional.isPresent()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        Event event = eventOptional.get();
        if (!event.getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        eventRepository.delete(event);

        ticketRepository.deleteByEvent(eventId);
    }
}
