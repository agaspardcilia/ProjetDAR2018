package services.diag.datastruct;

import org.json.JSONObject;

import utils.JSONable;

public class ExceptionReport implements JSONable {
	private Exception exception;
	
	public ExceptionReport(Exception exception) {
		this.exception = exception;
	}
	
	public Exception getException() {
		return exception;
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject result = new JSONObject();
		
		result.put("class", exception.getClass());
		result.put("message", exception.getMessage());
		result.put("local", exception.getLocalizedMessage());
		
		return result;
	}

}
