package scheduled;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.DBMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import scheduled.datastructs.City;
import scheduled.datastructs.EventType;
import scheduled.datastructs.WeatherEvent;
import utils.Debug;
import utils.owm.OWM;
import utils.owm.data.FiveDaysForcast;
import utils.owm.data.Forecast;
import utils.webapi.HttpException;

public class EventGenerator implements Runnable {
	private final static String GET_CITIES = "SELECT * FROM cities;";
	private final static String GET_CITY_ID = "SELECT * FROM cities WHERE idcity = ?;";
	private final static String ADD_EVENT = "INSERT INTO events (idcity, eventtype, date, lastmodif) VALUES (?, ?, ?, ?);";
	private final static String GET_EVENT_TIME_CITY = "SELECT * FROM events WHERE idcity = ? AND date = ?;";
	private final static String UPDATE_EVENT = "UPDATE events SET eventtype = ?, lastmodif = ? WHERE idcity = ? AND date = ?;";


	private OWM owm;


	public EventGenerator() {
		this.owm = new OWM("eff456cdb0b08998dc93401e3b72e54c");
	}

	private List<WeatherEvent> getNewEvents(City city) throws IOException, HttpException {
		List<WeatherEvent> result = new ArrayList<>();

		FiveDaysForcast fdf = owm.getFiveDaysForecastWeather(city.getName());

		fdf.getForecasts().forEach((Forecast forecast) -> {
			String mainWeather = forecast.getWeather().getMain();

			for (EventType et : EventType.values()) {
				if (et.getName().equals(mainWeather)) {
					WeatherEvent event = new WeatherEvent(-1, city, et, forecast.getDate());

					result.add(event);
				}
			}


		}); 

		return result;
	}

	public List<City> getMonitoredCities() throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		List<City> result = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(GET_CITIES, QueryType.SELECT);

		while (rs.next()) {
			result.add(new City(rs.getInt("idcity"), rs.getString("name")));
		}

		return result;
	}

	private WeatherEvent getWeatherEventFromTimeAndCity(long timestamp, int idCity) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs = DBMapper.executeQuery(GET_EVENT_TIME_CITY, QueryType.SELECT, idCity, timestamp);

		if (!rs.next())
			return null;

		WeatherEvent event = new WeatherEvent(rs.getInt("idevent"), getCityFromId(rs.getInt("idcity")),
				EventType.getTypeFromId(rs.getInt("eventtype")), new Date(rs.getLong("date")));

		return event;
	}

	public City getCityFromId(int id) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs = DBMapper.executeQuery(GET_CITY_ID, QueryType.SELECT, id);

		if (!rs.next())
			return null;

		return new City(id, rs.getString("name"));
	}

	private void addEventToDatabase(WeatherEvent event) throws CannotConnectToDatabaseException, QueryFailedException {
		System.out.println("insert");
		DBMapper.executeQuery(ADD_EVENT, QueryType.INSERT, event.getCity().getId(), EventType.RAIN.getId(),
				event.getDate().getTime(), System.currentTimeMillis());
	}

	private void updateEventOnDatabase(WeatherEvent event) throws CannotConnectToDatabaseException, QueryFailedException {
		DBMapper.executeQuery(UPDATE_EVENT, QueryType.UPDATE, event.getEventType().getId(), System.currentTimeMillis(), event.getCity().getId(),
				event.getDate().getTime());
	}

	@Override
	public void run() {
		Debug.display_notice("Refreshing events.");
		
		try {
			List<City> cities = getMonitoredCities();


			for (City c : cities) {
				for (WeatherEvent we : getNewEvents(c)) {
					WeatherEvent dbEvent = getWeatherEventFromTimeAndCity(we.getDate().getTime(), we.getCity().getId());
					if (dbEvent == null) {
						addEventToDatabase(we);
					} else if (we.getEventType() != dbEvent.getEventType()) {
						updateEventOnDatabase(we);
					}
				}

			}

		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException | IOException | HttpException e) {
			e.printStackTrace();
		}

	}
	
	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
	}

}
