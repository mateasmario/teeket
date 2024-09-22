package dev.mateas.teeket.entity;

import dev.mateas.teeket.type.TicketStatus;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Ticket {
    @Id
    private String id;
    private String code;
    private LocalDateTime creationDate;
    private TicketStatus status;
    private String event;

    public Ticket(String code, LocalDateTime creationDate, TicketStatus status, String event) {
        this.code = code;
        this.creationDate = creationDate;
        this.status = status;
        this.event = event;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }
}
