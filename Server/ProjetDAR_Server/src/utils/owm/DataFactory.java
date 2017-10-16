package utils.owm;

import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.owm.data.City;
import utils.owm.data.Clouds;
import utils.owm.data.Coordinates;
import utils.owm.data.CurrentWeather;
import utils.owm.data.FiveDaysForcast;
import utils.owm.data.Forecast;
import utils.owm.data.MainData;
import utils.owm.data.Weather;
import utils.owm.data.Wind;

public class DataFactory {
	public static City newCityFromJSONObject(JSONObject rawData) {
		int id = rawData.getInt("id");
		String name = rawData.getString("name");
		Coordinates coord = newCoordinatesFromJSONObject(rawData.getJSONObject("coord"));
		String country = rawData.getString("country");


		City result = new City(id, name, coord, country);

		return result;
	}


	public static Coordinates newCoordinatesFromJSONObject(JSONObject rawData) {
		double lon = rawData.getDouble("lon");
		double lat = rawData.getDouble("lat");


		return new Coordinates(lat, lon);
	}

	public static Clouds newCloudsFromJSONObject(JSONObject rawData) {
		int all = rawData.getInt("all");
		
		return new Clouds(all);
	}

	public static CurrentWeather newCurrentWeatherFromJSONObject(JSONObject rawData) {
		int id = rawData.getInt("id");
		String cityName = rawData.getString("name");
		Coordinates coord = newCoordinatesFromJSONObject(rawData.getJSONObject("coord"));
		Weather weather = newWeatherFromJSONObject(rawData.getJSONArray("weather"));		
		MainData main = newMainDataFromJSONObject(rawData.getJSONObject("main"));
		Wind wind = newWindFromJSONObject(rawData.getJSONObject("wind"));
		Clouds clouds = newCloudsFromJSONObject(rawData.getJSONObject("clouds"));
		
		return new CurrentWeather(id, cityName, coord, weather, main, wind, clouds);
	}

	public static Forecast newForecastFromJSONObject(JSONObject rawData) {
		Date date = new Date(rawData.getLong("dt"));
		MainData main = newMainDataFromJSONObject(rawData.getJSONObject("main"));
		Weather weather = newWeatherFromJSONObject(rawData.getJSONArray("weather"));
		Clouds clouds = newCloudsFromJSONObject(rawData.getJSONObject("clouds"));
		Wind wind = newWindFromJSONObject(rawData.getJSONObject("wind"));
		
		return new Forecast(date, main, weather, clouds, wind);
	}

	public static MainData newMainDataFromJSONObject(JSONObject rawData) {
		double temp = rawData.getDouble("temp");
		double tempMin = rawData.getDouble("temp_min");
		double tempMax = rawData.getDouble("temp_max");
		double pressure = rawData.getDouble("pressure");
		double humidity = rawData.getDouble("humidity");
		
		
		return new MainData(temp, tempMin, tempMax, pressure, humidity);
	}

	public static Weather newWeatherFromJSONObject(JSONArray rawArray) {
		JSONObject rawData = rawArray.getJSONObject(0);
		
		
		int id = rawData.getInt("id");
		String main = rawData.getString("main");
		String description = rawData.getString("description");
		String icon = rawData.getString("icon");
		
		
		
		return new Weather(id, main, description, icon);
	}

	public static Wind newWindFromJSONObject(JSONObject rawData) {
		double speed = rawData.getDouble("speed");
		double deg = rawData.getDouble("deg");
		
		return new Wind(speed, deg);
	}
	
	public static FiveDaysForcast newFiveDaysForecastFromJSONObject(JSONObject rawData) {
		City city = newCityFromJSONObject(rawData.getJSONObject("city"));
		
		JSONArray rawForecasts = rawData.getJSONArray("list");
		
		ArrayList<Forecast> forecasts = new ArrayList<>();
		
		rawForecasts.forEach(e -> {
			JSONObject o = (JSONObject) e;
			
			forecasts.add(newForecastFromJSONObject(o));
		});
		
		
		return new FiveDaysForcast(city, forecasts);
	}
	
	
	

}
