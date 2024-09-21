package dev.mateas.teeket.repository;

import dev.mateas.teeket.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface EventRepository extends MongoRepository<Event, String> {
    List<Event> findByOwner(String username);
}
