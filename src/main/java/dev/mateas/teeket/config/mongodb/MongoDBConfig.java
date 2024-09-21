package dev.mateas.teeket.config.mongodb;

import dev.mateas.teeket.config.mongodb.converter.LocalDateTimeReadConverter;
import dev.mateas.teeket.config.mongodb.converter.LocalDateTimeWriteConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class MongoDBConfig {
    public MongoCustomConversions mongoCustomConversions() {
        List<Converter<?, ?>> converterList = new ArrayList<>();
        converterList.add(new LocalDateTimeReadConverter());
        converterList.add(new LocalDateTimeWriteConverter());
        return new MongoCustomConversions(converterList);
    }
}
