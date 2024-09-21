package dev.mateas.teeket.repository;

import dev.mateas.teeket.entity.Ticket;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TicketRepository extends MongoRepository<Ticket, String> {
}
