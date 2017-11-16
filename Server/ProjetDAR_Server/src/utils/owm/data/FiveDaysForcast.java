package utils.owm.data;

import java.util.Date;
import java.util.List;

public class FiveDaysForcast {
	private City city;
	private List<Forecast> forecasts;
	
	public FiveDaysForcast(City city, List<Forecast> forecasts) {
		this.city = city;
		this.forecasts = forecasts;
	}
	
	public City getCity() {
		return city;
	}
	
	public List<Forecast> getForecasts() {
		return forecasts;
	}
	
	public int findIndexForcast(Date date) {
		int i = 0;
		for (Forecast f : forecasts) {
			if(f.getDate().equals(date))
				return i;
			i ++;
		}
		return -1;
	}
}
