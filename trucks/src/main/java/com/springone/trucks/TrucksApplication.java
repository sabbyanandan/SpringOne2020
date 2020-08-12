package com.springone.trucks;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;

@SpringBootApplication
public class TrucksApplication {

	private Random random = new Random();

	static List<String> VIN_LIST = Arrays
			.asList("JH4DB1660LS017594", "JH4DB7550SS005262", "5GZEV337X7J141405", "JH4KA8170MC002642",
					"WP1AB29P99LA40680",
					"JH4DB1660LS017594", "JH4DB7550SS005262", "5GZEV337X7J141405", "JH4KA8170MC002642",
					"WP1AB29P99LA40680",
					"JH4DB1660LS017594", "JH4DB7550SS005262", "5GZEV337X7J141405", "JH4KA8170MC002642",
					"WP1AB29P99LA40680",
					"JH4DB1660LS017594", "JH4DB7550SS005262", "5GZEV337X7J141405", "JH4KA8170MC002642",
					"WP1AB29P99LA40680",
					"JH4DB1660LS017594", "JH4DB7550SS005262", "5GZEV337X7J141405", "JH4KA8170MC002642",
					"WP1AB29P99LA40680");

	public static void main(String[] args) {
		SpringApplication.run(TrucksApplication.class, args);
	}

	@Bean
	@Scheduled(fixedRate = 1000L)
	public Supplier<Truck> generateTruck() {
		return () -> randomTruck();
	}

	private Truck randomTruck() {
		Truck truck = new Truck();
		truck.setId(VIN_LIST.get(random.nextInt(VIN_LIST.size())));
		truck.setAcceleration(random.nextFloat() * 10);
		truck.setVelocity(random.nextFloat() * 100);
		truck.setBrakeTemperature(random.nextFloat() * 50);
		truck.setInternalTemperature(random.nextFloat() * 50);
		truck.setExternalTemperature(random.nextFloat() * 50);
		return truck;
	}
}
