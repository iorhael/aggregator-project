package com.senla.aggregator.config;

import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.util.Random;

@Configuration
public class CommonConfig {

    @Bean
    public Faker faker() { return new Faker(); }

    @Bean
    public Random random() { return new Random(); }

    @Bean
    public RestTemplate restTemplate() { return new RestTemplate(); }
}
