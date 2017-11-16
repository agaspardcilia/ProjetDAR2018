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
import services.bet.datastruct.BetStruct;
import utils.Debug;
import utils.owm.OWM;
import utils.owm.data.FiveDaysForcast;
import utils.owm.data.Forecast;
import utils.owm.data.Weather;
import utils.webapi.HttpException;

public class EventGenerator implements Runnable {
	private final static String GET_CITIES = "SELECT * FROM cities;";
	private final static String GET_CITY_ID = "SELECT * FROM cities WHERE idcity = ?;";
	private final static String ADD_EVENT = "INSERT INTO events (idcity, eventtype, date, lastmodif) VALUES (?, ?, ?, ?);";
	private final static String GET_EVENT_TIME_CITY = "SELECT * FROM events WHERE idcity = ? AND date = ?;";
	private final static String UPDATE_EVENT = "UPDATE events SET eventtype = ?, lastmodif = ?, odd = ? WHERE idcity = ? AND date = ?;";
	private final static String QUERY_LIST_FUTUR_EVENT= "SELECT * FROM events WHERE date > ? ORDER BY date;";
	private final static String QUERY_LIST_WAIT_EVENT= "SELECT * FROM events WHERE date < ? AND status = 'wait' ORDER BY date;";
	private final static String QUERY_LIST_BET= "SELECT * FROM bets WHERE  idevent = ?;";



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
					WeatherEvent event = new WeatherEvent(-1, city, et, forecast.getDate(), utils.odd.Odd.computeOdd(forecast), "wait");

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
				EventType.getTypeFromId(rs.getInt("eventtype")), new Date(rs.getLong("date")), rs.getInt("odd"), "wait");

		return event;
	}

	public static City getCityFromId(int id) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
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

	private void updateEventOnDatabase(WeatherEvent event, double odd) throws CannotConnectToDatabaseException, QueryFailedException {
		DBMapper.executeQuery(UPDATE_EVENT, QueryType.UPDATE, event.getEventType().getId(), System.currentTimeMillis(), odd, event.getCity().getId(),
				event.getDate().getTime());
	}

	public static List<WeatherEvent> getEventsListFutur()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_FUTUR_EVENT, QueryType.SELECT, System.currentTimeMillis());
		while(rs.next()) {
			City city = getCityFromId(rs.getInt("city"));
			EventType eType = EventType.getTypeFromId(rs.getInt("eventtype")) ;
			Date d= new Date(rs.getLong("date"));
			double odd = rs.getInt("odd");
			String status = rs.getString("status");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd, status));
		}
		return events;
	}

	public static List<WeatherEvent> getEventsPast()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_FUTUR_EVENT, QueryType.SELECT, System.currentTimeMillis());
		while(rs.next()) {
			City city = getCityFromId(rs.getInt("city"));
			EventType eType = EventType.getTypeFromId(rs.getInt("eventtype")) ;
			Date d = new Date(rs.getLong("date"));
			double odd = rs.getInt("odd");
			String status = rs.getString("status");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd, status));
		}
		return events;
	}
	
	public static List<WeatherEvent> getEventsListWait()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_WAIT_EVENT, QueryType.SELECT, System.currentTimeMillis());
		while(rs.next()) {
			City city = getCityFromId(rs.getInt("city"));
			EventType eType = EventType.getTypeFromId(rs.getInt("eventtype")) ;
			Date d = new Date(rs.getLong("date"));
			double odd = rs.getInt("odd");
			String status = rs.getString("status");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd, status));
		}
		return events;
	}

	private static List <BetStruct> getBetsList(WeatherEvent event) 
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_BET, QueryType.SELECT, event.getIdEvent());
		List<BetStruct> bets = new ArrayList<>();

		while(rs.next()) {
			int idBet = rs.getInt("idbet");
			int idUser = rs.getInt("iduser");
			int idEvent = rs.getInt("idevent");
			double odd = rs.getDouble("odd");
			double moneyBet = rs.getDouble("moneybet");
			Date d = new Date(rs.getLong("date"));
			bets.add(new BetStruct(idBet, idUser, idEvent, odd, moneyBet, d));
		}
		return bets;
	}

	
	@Override
	public void run() { //TODO tester le run : 
		Debug.display_notice("Refreshing events.");

		try {
			List<City> cities = getMonitoredCities();


			for (City c : cities) { // ajout des event et maj de la cote
				for (WeatherEvent we : getNewEvents(c)) {
					FiveDaysForcast fdf = owm.getFiveDaysForecastWeather(we.getCity().toString());
					int index = fdf.findIndexForcast(we.getDate());
					double odd = utils.odd.Odd.computeOdd(fdf.getForecasts().get(index));
					WeatherEvent dbEvent = getWeatherEventFromTimeAndCity(we.getDate().getTime(), we.getCity().getId());
					if (dbEvent == null) {
						addEventToDatabase(we);
					} else if(dbEvent.getEventType() != we.getEventType()) {
						updateEventOnDatabase(we, odd);
					}
				}

			}
			for(WeatherEvent event : getEventsListFutur()){ //maj des cotes
				FiveDaysForcast fdf = owm.getFiveDaysForecastWeather(event.getCity().toString());
				int index = fdf.findIndexForcast(event.getDate());
				double odd = utils.odd.Odd.computeOdd(fdf.getForecasts().get(index));
				updateEventOnDatabase(event, odd);
			}
	
			for(WeatherEvent event : getEventsListWait()){
				Weather mainWeather = owm.getCurrentWeather(event.getCity().toString()).getWeather();
				String weather = mainWeather.getMain();
				if(event.getEventType().getName() == weather){
					event.setStatus("valid");
				}
				else	
					event.setStatus("invalid");
				updateEventOnDatabase(event, 1);
				
				for(BetStruct bet : getBetsList(event)){
					utils.ManagementPepet.addPepet(""+bet.getIdUser(), bet.getMoneyBet()*bet.getOdd());
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
