package dev.mateas.teeket.util.type;

public enum StringType {
    ALPHANUMERIC("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"), NUMERIC("0123456789");

    private final String characters;

    StringType(String characters) {
        this.characters = characters;
    }

    public String getCharacters() {
        return characters;
    }
}
