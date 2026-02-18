package com.assign.TuneTribe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TuneTribeApplication {

	public static void main(String[] args) {
		SpringApplication.run(TuneTribeApplication.class, args);
	}

}
