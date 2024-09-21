package dev.mateas.teeket.type;

public enum EventType {
    STANDARD("STANDARD"), EXTENDED("EXTENDED"), OVERCROWDED("OVERCROWDED");

    private String value;

    private EventType(String value) {
        this.value = value;
    }
}
