package dev.mateas.teeket.entity;

import dev.mateas.teeket.type.EventType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document
public class Event {
    @Id
    private String id;
    private String owner;
    private String name;
    private EventType eventType;
    private LocalDateTime creationDate;
    private String moderationCode;

    public Event(String owner, String name, EventType eventType, LocalDateTime creationDate, String moderationCode) {
        this.owner = owner;
        this.name = name;
        this.eventType = eventType;
        this.creationDate = creationDate;
        this.moderationCode = moderationCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public String getModerationCode() {
        return moderationCode;
    }

    public void setModerationCode(String moderationCode) {
        this.moderationCode = moderationCode;
    }
}
