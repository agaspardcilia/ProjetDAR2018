package scheduled.datastructs;

public class City {
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
	
}
