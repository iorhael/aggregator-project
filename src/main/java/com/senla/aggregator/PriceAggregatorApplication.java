package com.senla.aggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
@ConfigurationPropertiesScan
public class PriceAggregatorApplication {

    public static void main(String[] args) {
        SpringApplication.run(PriceAggregatorApplication.class, args);
    }

}
