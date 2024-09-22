package dev.mateas.teeket.util;

import java.util.Random;

public class StringGenerator {
    private static final String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    public String generateString(int length) {
        Random random = new Random();
        String result = "";

        while(result.length() < length) {
            int rand = random.nextInt(0, characters.length());
            result += characters.charAt(rand);
        }

        return result;
    }
}
