package services.user.datastructs;

import org.json.JSONObject;

public class User {
	private int id;
	private String username;
	private String email;
	
	public User(int id, String username, String email) {
		this.id = id;
		this.username = username;
		this.email = email;
	}
	
	public int getId() {
		return id;
	}
	
	public String getUsername() {
		return username;
	}
	
	public String getEmail() {
		return email;
	}

	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("id", id);
		result.put("username", username);
		result.put("email", email);
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof User) {
			User other = (User) obj;
			return id == other.getId();
		} else {
			return super.equals(obj);
		}
		
	}
}
