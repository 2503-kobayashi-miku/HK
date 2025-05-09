package com.example.HK;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class HkApplication {

	public static void main(String[] args) {
		SpringApplication.run(HkApplication.class, args);
	}

}
