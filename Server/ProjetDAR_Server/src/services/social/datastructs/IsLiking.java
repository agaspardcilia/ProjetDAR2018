package services.social.datastructs;

import org.json.JSONObject;

import utils.JSONable;

public class IsLiking implements JSONable{
	private int idUser;
	private String idStatus;
	private boolean isLiking;
	public IsLiking(int idUser, String idStatus, boolean isLiking) {
		super();
		this.idUser = idUser;
		this.idStatus = idStatus;
		this.isLiking = isLiking;
	}

	public int getIdUser() {
		return idUser;
	}
	
	public String getIdStatus() {
		return idStatus;
	}
	
	public boolean isLiking() {
		return isLiking;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("iduser", idUser);
		result.put("statusid", idStatus);
		result.put("isliking", isLiking);
		
		return result;
	}
	
	
}
