package services.event;

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
import services.datastructs.SearchResult;

/**
 * 
 * @author cb_mac
 *
 */
public class EventUtils {
	//event queries 
	//city date type
	private final static String QUERY_LIST_EVENT=	"SELECT * FROM events WHERE idcity=? AND date=? AND eventtype=? ORDER BY date LIMIT ? OFFSET ?;";
	private final static String QUERY_GET_EVENT=	"SELECT * FROM events WHERE idevent=?;";
	private final static String QUERY_GET_CITY=		"SELECT idcity,name FROM cities WHERE idcity=?;";

	private final static String QUERY_GET_CITIES = 	"SELECT * FROM cities;";
	
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
	
	
	public static JSONObject getEventsListJSON(int idcity,Date date,int eventtype ,int page ,int pageSize) {		
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


	// Date is not being use in this version.
	public static List<WeatherEvent> getEventsList(int idcity,Date date,int eventtype,int page ,int pageSize) 
			throws CannotConnectToDatabaseException, QueryFailedException, SQLException{
		List<WeatherEvent> events = new ArrayList<>();
//		if(!EventUtils.doesEventExists(idevent))
//			return events;
		
		ResultSet rs = DBMapper.executeQuery(QUERY_LIST_EVENT,QueryType.SELECT, idcity,date,eventtype,page,page*pageSize);
		while(rs.next()) {
			City city = getCityFromId(idcity);
			EventType eType=EventType.getTypeFromId(eventtype) ;
			Date d= new Date(rs.getLong("date"));
			events.add(new WeatherEvent(rs.getInt("idevent"), city, eType, d));
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

	
	public static List<City> getCitiesFromDatabase() throws CannotConnectToDatabaseException, QueryFailedException, SQLException {
		ArrayList<City> result = new ArrayList<>();
		
		ResultSet rs = DBMapper.executeQuery(QUERY_GET_CITIES, QueryType.SELECT);
		
		while (rs.next()) {
			result.add(new City(rs.getInt("idcity"),rs.getString("name")));
		}
		
		return result;
	}
}
