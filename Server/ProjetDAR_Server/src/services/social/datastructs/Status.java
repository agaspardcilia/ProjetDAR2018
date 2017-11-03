package services.social.datastructs;

import org.json.JSONObject;

import services.user.datastructs.User;
import utils.JSONable;

public class Status implements JSONable{
	private User author;
	private long timestamp;
	private String content;
	private String id;
	
	public Status(String id, User author, String content, long timestamp) {
		this.id = id;
		this.author = author;
		this.content = content;
		this.timestamp = timestamp;
	}
	
	
	public User getAuthor() {
		return author;
	}


	public long getTimestamp() {
		return timestamp;
	}


	public String getContent() {
		return content;
	}


	public String getId() {
		return id;
	}


	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("id", id);
		result.put("author", author.toJSONObject());
		result.put("content", content);
		result.put("timestamp", timestamp);
		
		return result;
	}
	
}
