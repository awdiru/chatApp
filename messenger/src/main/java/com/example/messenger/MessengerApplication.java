package com.example.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.template.model"})
public class MessengerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessengerApplication.class, args);
	}
}
