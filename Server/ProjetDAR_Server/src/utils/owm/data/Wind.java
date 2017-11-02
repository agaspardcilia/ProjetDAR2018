package utils.owm.data;

public class Wind {
	private double speed;
	private double deg; // Wind direction
	
	public Wind(double speed, double deg) {
		this.speed = speed;
		this.deg = deg;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public double getDeg() {
		return deg;
	}
	
}
