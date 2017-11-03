package utils.owm.data;

public class Coordinates {
	private double lat, lon;
	
	
	public Coordinates(double lat, double lon) {
		this.lat = lat;
		this.lon = lon;
	}
	
	
	public double getLat() {
		return lat;
	}
	
	public double getLon() {
		return lon;
	}
}
