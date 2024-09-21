package dev.mateas.teeket.validator;

public class PasswordValidator {
    private static final String LETTERS_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LETTERS_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";
    private static final String SYMBOLS = " ,./\\?!@#$%^&*()_+-=`~'\"<>;[]{}";

    public boolean validate(String password) {
        return checkLength(password)
                && checkContainsAtLeastOne(LETTERS_UPPERCASE, password)
                && checkContainsAtLeastOne(LETTERS_LOWERCASE, password)
                && checkContainsAtLeastOne(DIGITS, password)
                && checkContainsAtLeastOne(SYMBOLS, password);
    }

    private boolean checkLength(String password) {
        return password.length() >= 8;
    }

    private boolean checkContainsAtLeastOne(String container, String password) {
        for (int i = 0; i < password.length(); i++) {
            if (container.contains(String.valueOf(password.charAt(i)))) {
                return true;
            }
        }
        return false;
    }
}
