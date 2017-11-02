package utils.owm.data;

public class CurrentWeather {
	private int id;
	private String cityName;
	private Coordinates coord;
	private Weather weather;
	private MainData main;
	private Wind wind;
	private Clouds clouds;
	
	public CurrentWeather(int id, String cityName, Coordinates coord, Weather weather, MainData main, Wind wind,
			Clouds clouds) {
		super();
		this.id = id;
		this.cityName = cityName;
		this.coord = coord;
		this.weather = weather;
		this.main = main;
		this.wind = wind;
		this.clouds = clouds;
	}

	public int getId() {
		return id;
	}

	public String getCityName() {
		return cityName;
	}

	public Coordinates getCoord() {
		return coord;
	}

	public Weather getWeather() {
		return weather;
	}

	public MainData getMain() {
		return main;
	}

	public Wind getWind() {
		return wind;
	}

	public Clouds getClouds() {
		return clouds;
	}

	
	
}
