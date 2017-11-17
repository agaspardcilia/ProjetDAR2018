package services.bet.datastruct;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import utils.JSONable;

public class BetsResultStruct implements JSONable{
	private List<BetStruct> bets;
	public BetsResultStruct(List<BetStruct> betsC) {
		this.bets = betsC;
	}
	
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		JSONArray data = new JSONArray();
		
		bets.forEach(d -> data.put(d.toJSONObject()));
		
		result.put("result", data); 
		
		return result;
	}

	

}
