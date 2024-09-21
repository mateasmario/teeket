package dev.mateas.teeket.config.mongodb.converter;


import org.springframework.core.convert.converter.Converter;

import java.time.LocalDateTime;

public class LocalDateTimeWriteConverter implements Converter<LocalDateTime, String> {
    @Override
    public String convert(LocalDateTime s) {
        return s.toString();
    }
}
