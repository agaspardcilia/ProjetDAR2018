package services.datastructs;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import services.user.datastructs.User;
import utils.JSONable;

public class SearchResult implements JSONable {
	private int page;
	private int pageSize;
	private List<? extends JSONable> results;
	
	public SearchResult(int page, int pageSize, List<? extends JSONable> results) {
		this.page = page;
		this.pageSize = pageSize;
		this.results = results;
	}
	
	
	public int getPage() {
		return page;
	}
	
	public int getPageSize() {
		return pageSize;
	}
	
	public List<? extends JSONable> getResults() {
		return results;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("page", page);
		result.put("size", pageSize);
		JSONArray users = new JSONArray();
		
		results.forEach(u -> users.put(u.toJSONObject()));
		
		result.put("result", users); 
		
		return result;
	}
}
