package com.know_wave.comma.comma_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CommaBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommaBackendApplication.class, args);
	}

}
