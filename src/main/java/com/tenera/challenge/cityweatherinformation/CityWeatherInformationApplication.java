package com.tenera.challenge.cityweatherinformation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class CityWeatherInformationApplication {

	public static void main(String[] args) {
		SpringApplication.run(CityWeatherInformationApplication.class, args);
	}

}
