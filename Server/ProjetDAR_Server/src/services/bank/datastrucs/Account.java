package services.bank.datastrucs;

import org.json.JSONObject;

import services.user.datastructs.User;
import utils.JSONable;

public class Account implements JSONable {
	private User owner;
	private double balance;
	
	public Account(User owner, double balance) {
		this.owner = owner;
		this.balance = balance;
	}

	public User getOwner() {
		return owner;
	}
	
	public double getBalance() {
		return balance;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("owner", owner.toJSONObject());
		result.put("balance", balance);
		
		return result;
	}

}
