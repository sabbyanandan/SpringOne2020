package com.springone.braketemperature;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;
import java.util.Date;
import java.util.function.Function;

@SpringBootApplication
public class BrakeTemperatureApplication {

	public static void main(String[] args) {
		SpringApplication.run(BrakeTemperatureApplication.class, args);
	}

	@Bean
	public Function<KStream<Object, Truck>, KStream<String, AverageBrakeTemperatureAccumulator>> processBrakeTemperature() {

		return input -> input
				.map((k, v) -> new KeyValue<>(v.getId(), v))
				.groupByKey(Grouped.with(Serdes.String(), new JsonSerde<>(Truck.class)))
				.windowedBy(TimeWindows.of(Duration.ofSeconds(10)))
				.aggregate(() -> new AverageBrakeTemperatureAccumulator(0, 0F),
						(k, v, agg) -> {
							agg.setCount(agg.getCount() + 1);
							agg.setTotalValue(v.getBrakeTemperature());
							agg.setAverage(agg.getTotalValue() / agg.getCount());
							agg.setId(v.getId());
							return agg;
						},
						Materialized.with(Serdes.String(), new JsonSerde<>(AverageBrakeTemperatureAccumulator.class)))
				.toStream()
				.map((k, v) -> {
					v.setStart(new Date(k.window().start()));
					v.setEnd(new Date(k.window().end()));
					return new KeyValue<>(k.key(), v);
				});
	}

	static class AverageBrakeTemperatureAccumulator {

		private int count;

		private String id;

		private Float totalValue;

		private double average;

		private Date start;

		private Date end;

		public AverageBrakeTemperatureAccumulator() {
		}

		public AverageBrakeTemperatureAccumulator(int count, Float totalValue) {
			this.count = count;
			this.totalValue = totalValue;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public Float getTotalValue() {
			return totalValue;
		}

		public void setTotalValue(Float totalValue) {
			this.totalValue = totalValue;
		}

		public double getAverage() {
			return average;
		}

		public void setAverage(double average) {
			this.average = average;
		}

		public Date getStart() {
			return start;
		}

		public void setStart(Date start) {
			this.start = start;
		}

		public Date getEnd() {
			return end;
		}

		public void setEnd(Date end) {
			this.end = end;
		}

		@Override public String toString() {
			return "Average Brake Temperature: {" +
					"count=" + count +
					", id='" + id + '\'' +
					", average=" + average +
					'}';
		}
	}
}
