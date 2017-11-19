package utils.odd;

import java.util.Date;

import utils.owm.data.Forecast;

public class Odd {
	
	/*
	 * day < 3:
	 * 		odd = 1
	 * day between 3 to 5:
	 * 		odd = 0.5 * day
	 * day > 5:
	 * 		odd = 0.5 * 5 
	 */
	public static double computeOdd(Forecast weather){
		Date current = new Date(System.currentTimeMillis());
		double days;
		if(weather.getDate().getDay() - current.getDay() < 0){
			days = 7 - current.getDay() + weather.getDate().getDay();
		}
		else // end of week
			days = weather.getDate().getDay() - current.getDay();
		if(days < 3){
			return 1;
		}
		else if(days > 5){
			return 0.5 * 5;
		}
		else {
			return 0.5 * days;
		}
	}	
}
