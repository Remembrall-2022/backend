package com.stella.rememberall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class RememberallApplication {

	public static void main(String[] args) {
		SpringApplication.run(RememberallApplication.class, args);
	}

}
