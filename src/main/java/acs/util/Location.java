package acs.util;

public class Location {
	
	private Double lat;
	private Double lng;
	
	public Location() {
		// TODO Auto-generated constructor stub
	}

	public Location(Double d, Double e) {
		super();
		this.lat = d;
		this.lng = e;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}
	
	
	
}
