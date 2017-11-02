package scheduled.datastructs;

import java.util.Date;

public class WeatherEvent {
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
	
	
	
}
