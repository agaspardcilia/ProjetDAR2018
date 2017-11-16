package services.bet.datastruct;

import java.util.Date;

import org.json.JSONObject;

import services.ServicesTools;
import utils.JSONable;

public class BetStruct implements JSONable {
	private int idBet;
	private int idUser;
	private int idEvent;
	private double odd;
	private double moneyBet;
	private Date date;
	
	
	public BetStruct(int idBet, int idUser, int idEvent, double odd, double moneyBet, Date date) {
		this.idBet = idBet;
		this.idUser = idUser;
		this.idEvent = idEvent;
		this.odd = odd;
		this.moneyBet = moneyBet;
		this.date = date;
	}
	
	public int getIdBet() {
		return idBet;
	}
	
	public int getIdUser() {
		return idUser;
	}
	
	public int getIdEvent() {
		return idEvent;
	}
	
	public double getOdd() {
		return odd;
	}
	
	public double getMoneyBet() {
		return moneyBet;
	}
	public Date getDate() {
		return date;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put(ServicesTools.IDBET_ARG, idBet);
		result.put(ServicesTools.IDUSER_ARG, idUser);
		result.put(ServicesTools.IDEVENT_ARG, idEvent);
		result.put(ServicesTools.ODD_ARG, odd);
		result.put(ServicesTools.MONEYBET_ARG, moneyBet);
		result.put(ServicesTools.DATE_ARG, date);
		
		return result;
	}

}
