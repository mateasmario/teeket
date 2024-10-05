package dev.mateas.teeket.type;

public enum EventType {
    STANDARD("STANDARD"), EXTENDED("EXTENDED"), OVERCROWDED("OVERCROWDED");

    private final String value;

    EventType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
