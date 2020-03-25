package demo;

public class Location {
	
	private Long lat;
	private Long lng;
	
	public Location() {
		// TODO Auto-generated constructor stub
	}

	public Location(long lat, long lng) {
		super();
		this.lat = lat;
		this.lng = lng;
	}

	public long getLat() {
		return lat;
	}

	public void setLat(long lat) {
		this.lat = lat;
	}

	public long getLng() {
		return lng;
	}

	public void setLng(long lng) {
		this.lng = lng;
	}
	
	
	
}
