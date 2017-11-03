package utils.owm;

import java.io.IOException;
import java.net.MalformedURLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import utils.owm.config.TemperatureFormat;
import utils.owm.data.CurrentWeather;
import utils.owm.data.FiveDaysForcast;
import utils.webapi.HttpException;
import utils.webapi.WebApi;

public class OWM {
	private final static String API_URL = "api.openweathermap.org/";
	private final static String BASE_PATH = "data/2.5/";
	
	private final static String CURRENT_PATH = "weather";
	private final static String FORECAST_PATH = "forecast";
	
	
	private WebApi webApi;
	private String key;
	
	public OWM(String key) {
		this.key = key;
		
		try {
			webApi = new WebApi("http", API_URL, 80, BASE_PATH);
		} catch (MalformedURLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Will send a request to owm. This method will automatically use your api key.
	 * @param service
	 * 		Requested service.
	 * @param format
	 * @param args
	 * @return
	 * 		Raw answer.
	 * @throws HttpException 
	 * @throws IOException 
	 */
	public String sendRequest(String service, TemperatureFormat format, Map<String, String> args) throws IOException, HttpException {
		if (format.equals(TemperatureFormat.METRIC)) {
			args.put("units", "metric");
		} else {
			args.put("units", "imperial");
		}
		
		args.put("appid", key);
		
		return webApi.sendGetRequest(service, args);
	}
	
	public CurrentWeather getCurrentWeather(String city) throws IOException, HttpException {
		Map<String, String> args = new HashMap<>();
		
		args.put("q", city);
		
		
		JSONObject rawData = new JSONObject(sendRequest(CURRENT_PATH, TemperatureFormat.METRIC, args));
		
		return DataFactory.newCurrentWeatherFromJSONObject(rawData);
	}
	
	public FiveDaysForcast getFiveDaysForecastWeather(String city) throws IOException, HttpException {
		Map<String, String> args = new HashMap<>();
		
		args.put("q", city);
		
		JSONObject rawData = new JSONObject(sendRequest(FORECAST_PATH, TemperatureFormat.METRIC, args));

		return DataFactory.newFiveDaysForecastFromJSONObject(rawData);
	}
	
	public static void main(String[] args) {
		OWM owm = new OWM("eff456cdb0b08998dc93401e3b72e54c");

		try {
			owm.getFiveDaysForecastWeather("Paris").getForecasts().forEach(fc -> {
				System.out.println(fc.getDate());
			});
		} catch (IOException | HttpException e) {
			e.printStackTrace();
		}
		
	}
	
}
