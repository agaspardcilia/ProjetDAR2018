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
import services.event.EventUtils;
import utils.Debug;
import utils.owm.OWM;
import utils.owm.data.FiveDaysForcast;
import utils.owm.data.Forecast;
import utils.owm.data.Weather;
import utils.webapi.HttpException;

public class EventGenerator implements Runnable {
	private final static String GET_CITIES = "SELECT * FROM cities;";



	private OWM owm;

	public EventGenerator() {
		this.owm = new OWM("eff456cdb0b08998dc93401e3b72e54c");
	}


	public List<WeatherEvent> getNewEvents(City city) throws IOException, HttpException {
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


	@Override
	public void run() { //TODO tester le run : 
		Debug.display_notice("Refreshing events.");

		try {
			List<City> cities = getMonitoredCities();


			for (City c : cities) { // ajout des event et maj de la cote
				for (WeatherEvent we : getNewEvents(c)) {
					FiveDaysForcast fdf = owm.getFiveDaysForecastWeather(we.getCity().getName());
					int index = fdf.findIndexForcast(we.getDate());
					double odd = utils.odd.Odd.computeOdd(fdf.getForecasts().get(index));
					WeatherEvent dbEvent = EventUtils.getWeatherEventFromTimeAndCity(we.getDate().getTime(), we.getCity().getId());
					if (dbEvent == null) {
						EventUtils.addEventToDatabase(we);
					} else if(dbEvent.getEventType() != we.getEventType()) {
						EventUtils.updateEventOnDatabase(we, odd);
					}
				}

			}
			for(WeatherEvent event : EventUtils.getEventsListFutur()){ //maj des cotes
				FiveDaysForcast fdf = owm.getFiveDaysForecastWeather(event.getCity().getName());
				int index = fdf.findIndexForcast(event.getDate());
				double odd = utils.odd.Odd.computeOdd(fdf.getForecasts().get(index));
				EventUtils.updateEventOnDatabase(event, odd);
			}

			for(WeatherEvent event : EventUtils.getEventsListWait()){
				Weather mainWeather = owm.getCurrentWeather(event.getCity().getName()).getWeather();
				String weather = mainWeather.getMain();
				if(event.getEventType().getName() == weather){
					event.setStatus("valid");
				}
				else	
					event.setStatus("invalid");
				EventUtils.updateEventOnDatabase(event, 1);

				for(BetStruct bet : EventUtils.getBetsList(event)){
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
