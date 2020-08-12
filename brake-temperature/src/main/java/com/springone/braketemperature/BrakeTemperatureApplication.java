package com.springone.braketemperature;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.util.function.Function;

@SpringBootApplication
public class BrakeTemperatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrakeTemperatureApplication.class, args);
	}

	@Bean
	public Function<KStream<Object, Truck>, KStream<String, Long>> process() {

		return input -> input
				.map((k, v) -> new KeyValue<>(((Truck) v).getId(), v))
				.groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Truck.class)))
				//				.windowedBy(TimeWindows.of(10000))
				.count(Materialized.as("foo"))
				.toStream();

	}

	class Accumulator {

		private int count;

		private int totalValue;

		public Accumulator(int count, int totalValue) {
			this.count = count;
			this.totalValue = totalValue;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public int getTotalValue() {
			return totalValue;
		}

		public void setTotalValue(int totalValue) {
			this.totalValue = totalValue;
		}
	}
}
