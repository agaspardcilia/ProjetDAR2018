package services.event;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.naming.NamingException;

import org.bson.Document;
import org.json.JSONObject;

import com.mongodb.client.FindIterable;

import database.DBMapper;
import database.MongoMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import scheduled.datastructs.City;
import scheduled.datastructs.EventType;
import scheduled.datastructs.WeatherEvent;
import services.ServicesTools;
import services.datastructs.SearchResult;
import services.bet.Bet;
import services.bet.datastruct.BetStruct;

public class EventUtils {

  

	private final static String QUERY_LIST_EVENT=	"SELECT * FROM events WHERE idcity=? AND date=? ORDER BY date LIMIT ? OFFSET ?;";
	private final static String QUERY_GET_EVENT="SELECT * FROM events WHERE idevent=?;";
	private final static String QUERY_GET_CITY="SELECT idcity,name FROM cities WHERE idcity=?;";

	private final static String QUERY_ADD_EVENT = "INSERT INTO events (idcity, eventtype, date, lastmodif) VALUES (?, ?, ?, ?);";
	private final static String QUERY_GET_EVENT_TIME_CITY = "SELECT * FROM events WHERE idcity = ? AND date = ?;";
	private final static String QUERY_UPDATE_EVENT = "UPDATE events SET eventtype = ?, lastmodif = ?,status = ?, odd = ? WHERE idcity = ? AND date = ?;";
	private final static String QUERY_LIST_FUTUR_EVENT= "SELECT * FROM events WHERE date > ? ORDER BY date;";
	private final static String QUERY_LIST_PAST_EVENT= "SELECT * FROM events WHERE date < ? ORDER BY date;";
	private final static String QUERY_LIST_WAIT_EVENT= "SELECT * FROM events WHERE date < ? AND status = 'wait' ORDER BY date;";
	private final static String QUERY_GET_CITIES = 			"SELECT * FROM cities;";
	
	
	/**
	 * Returns the cities
	 * @param 
	 * @return
	 */
	public static JSONObject getAvailableCities() {
		JSONObject answer;
		
		try {
			List<City> cities = getCitiesFromDatabase();
			
			SearchResult sr = new SearchResult(0, cities.size(), cities);
			
			answer = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(answer, "cities", sr);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}
		
		return answer;
	}
	
	/**
	 * Returns a list of events associate to a city and a date
	 * @param idCity, date, page, pageSize
	 * @return
	 */
	public static JSONObject getEventsListJSON(int idcity, Date date, int page, int pageSize) {		
		JSONObject answer= new JSONObject();
		try {
			List<WeatherEvent> events = getEventsList(idcity, date, 1,  page, pageSize);
			EventResult er = new EventResult(page, pageSize, events);

			answer = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(answer, "eventResult", er);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;
	}
	
	/**
	 * Returns the last 10 events.
	 * @param idCity
	 * @return
	 */
	public static JSONObject getLastEvents(int idCity) { // warning 
		return getEventsListJSON(idCity, new Date(System.currentTimeMillis()), 0, 10);
	}



	/**
	 * Returns a list of events associate to a city and a date
	 * @param idCity, date, page, pageSize
	 * @return
	 */
	public static List<WeatherEvent> getEventsList(int idcity, Date date, int eventtype, int page, int pageSize) 
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();
		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_EVENT, QueryType.SELECT, idcity, date, page, page*pageSize);
		while(rs.next()) {
			City city = getCityFromId(idcity);
			EventType eType=EventType.getTypeFromId(eventtype) ;
			Date d = new Date(rs.getLong("date"));
			String status = rs.getString("status");
			double odd = rs.getDouble("odd");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd,  status));
		}
		
		return events;
	}

	/**
	 * Returns true if idevent exist in database and false if idevent is not in the database
	 * @param idevent
	 * @return
	 */
	public static boolean doesEventExists(int idevent) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet result = DBMapper.executeQuery(QUERY_GET_EVENT, QueryType.SELECT, idevent);
		return result.next();
	}

	/**
	 * Returns a list of events associate to a city and a date
	 * @param idCity
	 * @return
	 */
	public static City getCityFromId(int idcity) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs= DBMapper.executeQuery(QUERY_GET_CITY, QueryType.SELECT, idcity);
		rs.next();
		City city = new City(rs.getInt("idcity"),rs.getString("name"));
		return city;	
	}


	public static WeatherEvent getWeatherEventFromTimeAndCity(long timestamp, int idCity) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs = DBMapper.executeQuery(QUERY_GET_EVENT_TIME_CITY, QueryType.SELECT, idCity, timestamp);

		if (!rs.next())
			return null;

		WeatherEvent event = new WeatherEvent(rs.getInt("idevent"), getCityFromId(rs.getInt("idcity")),
				EventType.getTypeFromId(rs.getInt("eventtype")), new Date(rs.getLong("date")), rs.getInt("odd"), "wait");

		return event;
	}

	/**
	 * create a new event in database
	 * @param event
	 * @return
	 */
	public static void addEventToDatabase(WeatherEvent event) throws CannotConnectToDatabaseException, QueryFailedException {
		System.out.println("insert");
		DBMapper.executeQuery(QUERY_ADD_EVENT, QueryType.INSERT, event.getCity().getId(), EventType.RAIN.getId(),
				event.getDate().getTime(), System.currentTimeMillis());
	}
	
	/**
	 * Update the odd of an event
	 * @param event, odd
	 * @return
	 */
	public static void updateEventOnDatabase(WeatherEvent event, double odd) throws CannotConnectToDatabaseException, QueryFailedException {
		DBMapper.executeQuery(QUERY_UPDATE_EVENT, QueryType.UPDATE, event.getEventType().getId(), System.currentTimeMillis(),event.getStatus(), odd , event.getCity().getId(),
				event.getDate().getTime());
	}

	/**
	 * Returns a list of unfinish events
	 * @param 
	 * @return
	 */
	public static List<WeatherEvent> getEventsListFutur()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_FUTUR_EVENT, QueryType.SELECT, System.currentTimeMillis());
		while(rs.next()) {
			City city = getCityFromId(rs.getInt("idcity"));
			EventType eType = EventType.getTypeFromId(rs.getInt("eventtype")) ;
			Date d= new Date(rs.getLong("date"));
			double odd = rs.getInt("odd");
			String status = rs.getString("status");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd, status));
		}
		return events;
	}

	/**
	 * Returns a list of finish events
	 * @param 
	 * @return
	 */
	public static List<WeatherEvent> getEventsPast()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_PAST_EVENT, QueryType.SELECT, System.currentTimeMillis());
		while(rs.next()) {
			City city = getCityFromId(rs.getInt("idcity"));
			EventType eType = EventType.getTypeFromId(rs.getInt("eventtype")) ;
			Date d = new Date(rs.getLong("date"));
			double odd = rs.getInt("odd");
			String status = rs.getString("status");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd, status));
		}
		return events;
	}
	/**
	 * Returns a list status="wait" events
	 * @param 
	 * @return
	 */
	public static List<WeatherEvent> getEventsListWait()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_WAIT_EVENT, QueryType.SELECT, System.currentTimeMillis());
		while(rs.next()) {
			City city = getCityFromId(rs.getInt("idcity"));
			EventType eType = EventType.getTypeFromId(rs.getInt("eventtype")) ;
			Date d = new Date(rs.getLong("date"));
			double odd = rs.getInt("odd");
			String status = rs.getString("status");
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d, odd, status));
		}
		return events;
	}

	/**
	 * Returns a list of cities
	 * @param 
	 * @return
	 */
	public static List<City> getCitiesFromDatabase() throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ArrayList<City> result = new ArrayList<>();
		
		ResultSet rs = DBMapper.executeQuery(QUERY_GET_CITIES, QueryType.SELECT);
		
		while (rs.next()) {
			result.add(new City(rs.getInt("idcity"),rs.getString("name")));
		}
		
		return result;
	}

	/**
	 * Returns a bets associate to an event
	 * @param 
	 * @return
	 */
	public static List <BetStruct> getBetsList(WeatherEvent event) 
			throws NamingException, SQLException, CannotConnectToDatabaseException, QueryFailedException{
		List<BetStruct> result = new ArrayList<>();
		FindIterable<Document> qResult;
		Document args = new Document();
		args.put(Bet.EVENT_KEY, event.getIdEvent());
		qResult = MongoMapper.executeGet("bets", args,0);

		for(Document d : qResult) {
			result.add(Bet.getBetsFromDocument(d));
		}
		return result;
	}

}
