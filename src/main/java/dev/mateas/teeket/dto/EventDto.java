package dev.mateas.teeket.dto;

import dev.mateas.teeket.type.EventType;

public class EventDto {
    private String name;
    private EventType eventType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }
}
