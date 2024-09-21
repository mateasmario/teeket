package dev.mateas.teeket.service.authentication;

import dev.mateas.teeket.entity.Event;
import dev.mateas.teeket.entity.User;
import dev.mateas.teeket.repository.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<Event> getEvents(String username) {
        List<Event> eventList = eventRepository.findByOwner(username);
        return eventList;
    }
}
