package dev.mateas.teeket.repository;

import dev.mateas.teeket.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TicketRepository extends MongoRepository<Ticket, String> {
    List<Ticket> findByEvent(String event);
    void deleteByEvent(String event);
    Long countByEvent(String event);
}
