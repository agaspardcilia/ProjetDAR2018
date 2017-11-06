package services.social.datastructs;

import org.json.JSONObject;

import services.user.datastructs.User;
import utils.JSONable;

public class Comment implements JSONable {
	private String id;
	private User author;
	private long timestamp;
	private String content;
	private String statusId;
	
	public Comment(String id, User author, long timestamp, String content, String statusId) {
		super();
		this.id = id;
		this.author = author;
		this.timestamp = timestamp;
		this.content = content;
		this.statusId = statusId;
	}
	
	public String getId() {
		return id;
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
	
	public String getStatusId() {
		return statusId;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("id", id);
		result.put("author", author.toJSONObject());
		result.put("timestamp", timestamp);
		result.put("content", content);
		result.put("status_id", statusId);
		
		return result;
	}

	
}
