package com.springone.braketemperature;

public class Truck {

	String id;

	Float acceleration;

	Float velocity;

	Float brakeTemperature;

	Float internalTemperature;

	Float externalTemperature;

	public Truck() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Float getAcceleration() {
		return acceleration;
	}

	public void setAcceleration(Float acceleration) {
		this.acceleration = acceleration;
	}

	public Float getVelocity() {
		return velocity;
	}

	public void setVelocity(Float velocity) {
		this.velocity = velocity;
	}

	public Float getBrakeTemperature() {
		return brakeTemperature;
	}

	public void setBrakeTemperature(Float brakeTemperature) {
		this.brakeTemperature = brakeTemperature;
	}

	public Float getInternalTemperature() {
		return internalTemperature;
	}

	public void setInternalTemperature(Float internalTemperature) {
		this.internalTemperature = internalTemperature;
	}

	public Float getExternalTemperature() {
		return externalTemperature;
	}

	public void setExternalTemperature(Float externalTemperature) {
		this.externalTemperature = externalTemperature;
	}

	@Override public String toString() {
		return "Truck{" +
				"id='" + id + '\'' +
				", acceleration=" + acceleration +
				", velocity=" + velocity +
				", brakeTemperature=" + brakeTemperature +
				", internalTemperature=" + internalTemperature +
				", externalTemperature=" + externalTemperature +
				'}';
	}
}
