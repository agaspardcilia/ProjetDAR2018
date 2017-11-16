package services.social.datastructs;

import org.json.JSONObject;

import utils.JSONable;

public class Count implements JSONable {
	private String statusId;
	private int count;
	
	public Count(String statusId, int count) {
		this.statusId = statusId;
		this.count = count;
	}
	
	
	public String getStatusId() {
		return statusId;
	}
	
	public int getCount() {
		return count;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("status_id", statusId);
		result.put("count", count);
		
		return result;
	}

}
