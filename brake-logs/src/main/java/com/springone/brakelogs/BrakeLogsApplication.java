package com.springone.brakelogs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.function.Consumer;

@SpringBootApplication
public class BrakeLogsApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrakeLogsApplication.class, args);
	}

	@Bean
	public Consumer<Object> log() {
		return result -> System.out.println(result);
	}
}

