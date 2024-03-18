package com.casumo.races;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EntityScan
@EnableTransactionManagement
@ConfigurationPropertiesScan
@EnableConfigurationProperties
public class RacesApplication {

	public static void main(String[] args) {
		SpringApplication.run(RacesApplication.class, args);
	}

}
