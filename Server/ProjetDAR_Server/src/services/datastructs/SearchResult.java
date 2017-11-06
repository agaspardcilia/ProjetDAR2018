package services.datastructs;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

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
		JSONArray data = new JSONArray();
		
		results.forEach(d -> data.put(d.toJSONObject()));
		
		result.put("result", data); 
		
		return result;
	}
}
