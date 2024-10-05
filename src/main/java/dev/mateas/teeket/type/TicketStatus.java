package dev.mateas.teeket.type;

public enum TicketStatus {
    VALID("VALID"), MANUALLY_INVALIDATED("MANUALLY_INVALIDATED"), INVALID("INVALID");

    private final String value;

    TicketStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
