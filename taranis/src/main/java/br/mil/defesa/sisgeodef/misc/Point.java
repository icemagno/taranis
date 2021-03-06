package br.mil.defesa.sisgeodef.misc;

import java.io.Serializable;

public class Point implements Serializable {
	private static final long serialVersionUID = 1L;
	private Double lat;
	private Double lon;
	private Integer altitude;
	private Double value;
	private String color;
	
	public Point(Double lat, Double lon, Integer altitude, Double value, String color) {
		this.lat = lat;
		this.lon = lon;
		this.altitude = altitude;
		this.value = value;
		this.color = color;
	}

	public Double getLat() {
		return lat;
	}
	
	public Double getLon() {
		return lon;
	}
	
	public Integer getAltitude() {
		return altitude;
	}
	
	public Double getValue() {
		return value;
	}
	
	public String getColor() {
		return color;
	}

}
