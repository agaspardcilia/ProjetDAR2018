package services.auth.datastructs;

import org.json.JSONObject;

import services.ServicesTools;
import utils.JSONable;

public class LoginAnswer implements JSONable {
	private String key;
	private int idUser;
	
	
	public LoginAnswer(String key, int idUser) {
		this.key = key;
		this.idUser = idUser;
	}
	
	public String getKey() {
		return key;
	}
	
	public int getIdUser() {
		return idUser;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put(ServicesTools.KEY_ARG, key);
		result.put(ServicesTools.IDUSER_ARG, idUser);
		
		return result;
	}

}
