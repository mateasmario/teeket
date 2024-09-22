package dev.mateas.teeket.config;

import dev.mateas.teeket.util.QRCodeGenerator;
import dev.mateas.teeket.util.StringGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DefaultConfig {
    @Bean
    public StringGenerator stringGenerator() {
        return new StringGenerator();
    }

    @Bean
    public QRCodeGenerator qrCodeGenerator() {
        return new QRCodeGenerator();
    }
}
