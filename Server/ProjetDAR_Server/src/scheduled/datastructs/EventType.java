package scheduled.datastructs;

public enum EventType {
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
}
