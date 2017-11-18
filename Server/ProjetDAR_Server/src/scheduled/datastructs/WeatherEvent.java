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
	private double odd;
	private String status;
	
	public WeatherEvent(int idEvent, City city, EventType eventType, Date date, double odd, String status) {
		super();
		this.idEvent = idEvent;
		this.city = city;
		this.eventType = eventType;
		this.date = date;
		this.odd = odd;
		this.status = status;
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
	
	public double getOdd() {
		return odd;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setOdd(double oddS){
		this.odd = oddS;
	}
	
	public void setStatus(String statusS){
		this.status = statusS;
	}
	
	@Override
	public String toString() {
		return "Weather event [id=" + idEvent  + ", city=" + city + ", eventType="+ eventType + ", date=" + date + ", odd=" + odd + ", status=" + status + "]";
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("idEvent",idEvent);
		result.put("city", city.toJSONObject());
		result.put("eventType", eventType.toJSONObject());
		result.put("date", date.getTime());
		
		return result;
	}
	
	
	
}