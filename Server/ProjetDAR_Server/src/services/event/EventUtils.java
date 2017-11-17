package services.event;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

import com.google.common.primitives.UnsignedLong;

import database.DBMapper;
import database.DBMapper.QueryType;
import database.exceptions.CannotConnectToDatabaseException;
import database.exceptions.QueryFailedException;
import scheduled.datastructs.City;
import scheduled.datastructs.EventType;
import scheduled.datastructs.WeatherEvent;
import services.ServicesTools;
import services.bet.datastruct.BetStruct;
import utils.owm.data.FiveDaysForcast;
import utils.owm.data.Forecast;
import utils.webapi.HttpException;

/**
 * 
 * @author cb_mac
 *
 */
public class EventUtils {
	//event queries 
	//city date type
	private final static String QUERY_LIST_EVENT=	
			"SELECT * FROM events WHERE idcity=? AND date=? AND eventtype=? ORDER BY date LIMIT ? OFFSET ?;";
	private final static String QUERY_GET_EVENT="SELECT * FROM events WHERE idevent=?;";
	private final static String QUERY_GET_CITY="SELECT idcity,name FROM cities WHERE idcity=?;";
	//Add
	private final static String ADD_EVENT = "INSERT INTO events (idcity, eventtype, date, lastmodif) VALUES (?, ?, ?, ?);";
	private final static String GET_EVENT_TIME_CITY = "SELECT * FROM events WHERE idcity = ? AND date = ?;";
	private final static String UPDATE_EVENT = "UPDATE events SET eventtype = ?, lastmodif = ?, odd = ? WHERE idcity = ? AND date = ?;";
	private final static String QUERY_LIST_FUTUR_EVENT= "SELECT * FROM events WHERE date > ? ORDER BY date;";
	private final static String QUERY_LIST_WAIT_EVENT= "SELECT * FROM events WHERE date < ? AND status = 'wait' ORDER BY date;";
	private final static String QUERY_LIST_BET= "SELECT * FROM bets WHERE  idevent = ?;";

	
	public static JSONObject getEventsListJSON(int idcity,Date date,int eventtype ,int page ,int pageSize) 
	{		
		JSONObject answer= new JSONObject();
		try {
			List<WeatherEvent> events = getEventsList(idcity,date,eventtype, page, pageSize);
			EventResult er = new EventResult(page, pageSize, events);

			answer = ServicesTools.createPositiveAnswer();
			ServicesTools.addToPayload(answer, "eventResult", er);
		} catch (CannotConnectToDatabaseException | QueryFailedException | SQLException e) {
			answer = ServicesTools.createDatabaseError(e);
		}
		return answer;
	}


//date didn't use in this version
	public static List<WeatherEvent> getEventsList(int idcity,Date date,int eventtype,int page ,int pageSize) 
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();
//		if(!EventUtils.doesEventExists(idevent))
//			return events;
		
		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_EVENT,QueryType.SELECT, idcity,date,eventtype,page,page*pageSize);
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

	public static boolean doesEventExists(int idevent) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet result = DBMapper.executeQuery(QUERY_GET_EVENT, QueryType.SELECT, idevent);
		return result.next();
	}
	
	public static City getCityFromId(int idcity) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs= DBMapper.executeQuery(QUERY_GET_CITY, QueryType.SELECT, idcity);
		rs.next();
		City city = new City(rs.getInt("idcity"),rs.getString("name"));
		return city;	
	}

	
	//Add
	
	



	public static WeatherEvent getWeatherEventFromTimeAndCity(long timestamp, int idCity) throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ResultSet rs = DBMapper.executeQuery(GET_EVENT_TIME_CITY, QueryType.SELECT, idCity, timestamp);

		if (!rs.next())
			return null;

		WeatherEvent event = new WeatherEvent(rs.getInt("idevent"), getCityFromId(rs.getInt("idcity")),
				EventType.getTypeFromId(rs.getInt("eventtype")), new Date(rs.getLong("date")), rs.getInt("odd"), "wait");

		return event;
	}

	public static void addEventToDatabase(WeatherEvent event) throws CannotConnectToDatabaseException, QueryFailedException {
		System.out.println("insert");
		DBMapper.executeQuery(ADD_EVENT, QueryType.INSERT, event.getCity().getId(), EventType.RAIN.getId(),
				event.getDate().getTime(), System.currentTimeMillis());
	}

	public static void updateEventOnDatabase(WeatherEvent event, double odd) throws CannotConnectToDatabaseException, QueryFailedException {
		DBMapper.executeQuery(UPDATE_EVENT, QueryType.UPDATE, event.getEventType().getId(), System.currentTimeMillis(), odd, event.getCity().getId(),
				event.getDate().getTime());
	}

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

	public static List<WeatherEvent> getEventsPast()
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();

		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_FUTUR_EVENT, QueryType.SELECT, System.currentTimeMillis());
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

	public static List <BetStruct> getBetsList(WeatherEvent event) 
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
}
