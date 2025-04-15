package com.example.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EntityScan(basePackages = {"com.example.template.model"})
@ComponentScan(basePackages = {"com.example.template.config", "com.example.messenger"})
public class MessengerApplication {
	public static void main(String[] args) {
		SpringApplication.run(MessengerApplication.class, args);
	}
}
