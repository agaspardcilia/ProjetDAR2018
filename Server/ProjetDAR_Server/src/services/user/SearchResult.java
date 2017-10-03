package services.user;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class SearchResult {
	private int page;
	private int pageSize;
	private List<User> results;
	
	public SearchResult(int page, int pageSize, List<User> results) {
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
	
	public List<User> getResults() {
		return results;
	}
	
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("page", page);
		result.put("size", pageSize);
		JSONArray users = new JSONArray();
		
		results.forEach(u -> users.put(u.toJSONObject()));
		
		result.put("users", users); 
		
		return result;
	}
}
