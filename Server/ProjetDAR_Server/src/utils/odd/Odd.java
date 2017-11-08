package utils.odd;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import utils.owm.data.FiveDaysForcast;
import utils.owm.data.Forecast;
import utils.webapi.HttpException;
import utils.owm.OWM;

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
		Calendar calendar = Calendar.getInstance();
		Date current = new Date(System.currentTimeMillis());
		double days;
		if(weather.getDate().getDay() - current.getDay() < 0){
			days = 7 - current.getDay() + weather.getDate().getDay();
		}
		else
			days = weather.getDate().getDay() - current.getDay(); // gerer les début fin de semaine
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
