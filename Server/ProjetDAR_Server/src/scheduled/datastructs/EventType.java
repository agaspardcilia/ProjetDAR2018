package scheduled.datastructs;

import org.json.JSONObject;

import utils.JSONable;

/**
 * 
 *@modify cb_mac 5/11/17 
 *
 */
public enum EventType implements JSONable{
	RAIN(1, 500, "Rain");
	
	private int id;
	private int owmId;
	private String name;
	
	private EventType(int id, int owmId, String name) {
		this.id = id;
		this.owmId = owmId;
		this.name = name;
	}
	
	public int getId() {
		return id;
	}
	
	public int getOwmId() {
		return owmId;
	}
	
	public String getName() {
		return name;
	}
	
	public static EventType getTypeFromId(int id) {
		for (EventType et : EventType.values())  {
			if (et.id == id)
				return et;
		}
		
		return null;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		result.put("id", id);
		result.put("owmId", owmId);
		result.put("name", name);
		return result;
		
	}
}
