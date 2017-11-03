package utils.owm.data;

public class MainData {
	private double temp;
	private double tempMin;
	private double tempMax;
	private double pressure;
	private double humidity;
	
	public MainData(double temp, double tempMin, double tempMax, double pressure,
			double humidity) {
		super();
		this.temp = temp;
		this.tempMin = tempMin;
		this.tempMax = tempMax;
		this.pressure = pressure;
		this.humidity = humidity;
	}

	public double getTemp() {
		return temp;
	}

	public double getTempMin() {
		return tempMin;
	}

	public double getTempMax() {
		return tempMax;
	}

	public double getPressure() {
		return pressure;
	}

	public double getHumidity() {
		return humidity;
	}


	
	
	

	
	
}
