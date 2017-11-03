package utils.owm.data;

import java.util.Date;

public class Forecast {
	private Date date;
	private MainData main;
	private Weather weather;
	private Clouds coulds;
	private Wind wind;
	
	public Forecast(Date date, MainData main, Weather weather, Clouds coulds, Wind wind) {
		super();
		this.date = date;
		this.main = main;
		this.weather = weather;
		this.coulds = coulds;
		this.wind = wind;
	}

	public Date getDate() {
		return date;
	}

	public MainData getMain() {
		return main;
	}

	public Weather getWeather() {
		return weather;
	}

	public Clouds getCoulds() {
		return coulds;
	}

	public Wind getWind() {
		return wind;
	}
	
	
	
	
}
