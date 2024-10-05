package dev.mateas.teeket.util;

import dev.mateas.teeket.util.type.StringType;

import java.util.Random;

public class StringGenerator {
    private String generateString(String characters, int length) {
        Random random = new Random();
        StringBuilder result = new StringBuilder();

        while(result.length() < length) {
            int rand = random.nextInt(0, characters.length());
            result.append(characters.charAt(rand));
        }

        return result.toString();
    }

    public String generateString(StringType stringType, int length) {
        return generateString(stringType.getCharacters(), length);
    }
}
