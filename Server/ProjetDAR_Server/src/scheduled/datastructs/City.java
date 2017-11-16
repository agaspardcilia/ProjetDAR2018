package scheduled.datastructs;

import org.json.JSONObject;

import utils.JSONable;

/**
 * 
 *@modify cb_mac 5/11/17 
 *
 */
public class City implements JSONable{
	private int id;
	private String name;
	
	public City(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return "City[id=" + id +" ,name=" + name + "]";
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		result.put("id", id);
		result.put("name", name);
		return result;
	}
	
}
