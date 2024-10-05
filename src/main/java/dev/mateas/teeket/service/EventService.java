package dev.mateas.teeket.service;

import dev.mateas.teeket.dto.EventDto;
import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.exception.event.EventNameAlreadyExistsException;
import dev.mateas.teeket.exception.event.EventNameTooShortException;
import dev.mateas.teeket.exception.event.EventDoesNotBelongToRequesterException;
import dev.mateas.teeket.exception.event.EventWithSpecifiedIdDoesNotExistException;
import dev.mateas.teeket.repository.EventRepository;
import dev.mateas.teeket.repository.TicketRepository;
import dev.mateas.teeket.util.StringGenerator;
import dev.mateas.teeket.util.type.StringType;
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
    @Autowired
    private StringGenerator stringGenerator;

    public List<Event> getEvents(String username) {
        return eventRepository.findByOwner(username);
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

        String moderationCode = stringGenerator.generateString(StringType.NUMERIC, 4);
        Event event = new Event(username, eventDto.getName(), eventDto.getEventType(), LocalDateTime.now(), moderationCode);
        eventRepository.insert(event);
    }

    public void deleteEvent(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        Event event = eventOptional.get();
        if (!event.getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        eventRepository.delete(event);

        ticketRepository.deleteByEvent(eventId);
    }

    public void regenerateModerationCode(String username, String eventId) throws EventWithSpecifiedIdDoesNotExistException, EventDoesNotBelongToRequesterException {
        Optional<Event> eventOptional = eventRepository.findById(eventId);

        if (eventOptional.isEmpty()) {
            throw new EventWithSpecifiedIdDoesNotExistException();
        }

        Event event = eventOptional.get();
        if (!event.getOwner().equals(username)) {
            throw new EventDoesNotBelongToRequesterException();
        }

        String moderationCode = stringGenerator.generateString(StringType.NUMERIC,4);
        event.setModerationCode(moderationCode);
        eventRepository.save(event);
    }
}
