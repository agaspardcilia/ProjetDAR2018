package services.social;

import services.errors.GenericError;
import services.errors.ServletError;

public enum SocialErrors implements ServletError {
	PLACHOLDER(new GenericError(3007, "Place holder error."));
	
	
	private GenericError error;
	
	private SocialErrors(GenericError error) {
		this.error = error;
	}
	
	@Override
	public int getCode() {
		return error.getCode();
	}

	@Override
	public String getMessage() {
		return error.getMessage();
	}

}
