package demo;

public class ParkingBoundary {
	
	private String lat;
	private String lon;
	private String city;
	private String street;
	private String parkingDomain;
	private String parkingId;

	
	public ParkingBoundary() {
	
	}
	
	
	public ParkingBoundary(String lat, String lon, String city, String street,String parkingDomain, String parkingId) {
		super();
		
		this.lat = lat;
		this.lon = lon;
		this.city = city;
		this.street = street;
		this.parkingDomain = parkingDomain;
		this.parkingId = parkingId;
	}
	
	public String getLat() {
		return lat;
	}
	
	public void setLat(String lat) {
		this.lat = lat;
	}
	
	public String getLon() {
		return lon;
	}
	
	public void setLon(String lon) {
		this.lon = lon;
	}
	
	public String getCity() {
		return city;
	}
	
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getStreet() {
		return street;
	}
	
	public String getParkingDomain() {
		return parkingDomain;
	}
	
	public String getParkingId() {
		return parkingId;
	}
	


	public void setParkingDomain(String parkingDomain) {
		this.parkingDomain = parkingDomain;
	}
	

	public void setParkingId(String parkingId) {
		this.parkingId = parkingId;
	}


	public void setStreet(String street) {
		this.street = street;
	}
	
	
	
	

}