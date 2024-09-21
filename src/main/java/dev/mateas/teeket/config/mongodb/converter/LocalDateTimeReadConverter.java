package dev.mateas.teeket.config.mongodb.converter;


import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class LocalDateTimeReadConverter implements Converter<String, LocalDateTime> {
    @Override
    public LocalDateTime convert(String s) {
        return LocalDateTime.parse(s);
    }
}
