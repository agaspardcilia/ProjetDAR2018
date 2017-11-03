package utils.owm.data;

public class City {
	private int id;
	private String name;
	private Coordinates coord;
	private String country;
	
	
	public City(int id, String name, Coordinates coord, String country) {
		this.id = id;
		this.name = name;
		this.coord = coord;
		this.country = country;
	}

	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public Coordinates getCoord() {
		return coord;
	}
	
	public String getCountry() {
		return country;
	}
}
