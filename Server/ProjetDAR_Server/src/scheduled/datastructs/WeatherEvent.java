package scheduled.datastructs;

import java.util.Date;

import org.json.JSONObject;

import utils.JSONable;

/**
 * 
 *@modify cb_mac 5/11/17 
 */
public class WeatherEvent implements JSONable{
	private int idEvent;
	private City city;
	private EventType eventType;
	private Date date;
	
	public WeatherEvent(int idEvent, City city, EventType eventType, Date date) {
		super();
		this.idEvent = idEvent;
		this.city = city;
		this.eventType = eventType;
		this.date = date;
	}
	
	public int getIdEvent() {
		return idEvent;
	}

	public City getCity() {
		return city;
	}
	
	public EventType getEventType() {
		return eventType;
	}
	
	public Date getDate() {
		return date;
	}
	
	@Override
	public String toString() {
		return "Weather event [id=" + idEvent  + ", city=" + city + ", eventType="+ eventType + ", date=" + date + "]";
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("idEvent",idEvent);
		result.put("city", city.toJSONObject());
		result.put("eventType", eventType.toJSONObject());
		result.put("date", date);
		
		return result;
	}
	
	
	
}
