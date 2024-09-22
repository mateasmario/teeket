package dev.mateas.teeket.type;

public enum TicketStatus {
    VALID("VALID"), MANUALLY_INVALIDATED("MANUALLY_INVALIDATED"), INVALID("INVALID");

    private String value;

    private TicketStatus(String value) {
        this.value = value;
    }
}
