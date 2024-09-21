package dev.mateas.teeket.repository;

import dev.mateas.teeket.entity.Event;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventRepository extends MongoRepository<Event, String> {
}
