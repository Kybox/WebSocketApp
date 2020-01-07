package fr.kybox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.PrintStream;

@Configuration
public class AppConfig {

    @Bean
    public PrintStream printStream() {
        return new PrintStream(System.out);
    }
}
