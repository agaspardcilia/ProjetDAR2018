package services.bank;

import services.errors.GenericError;
import services.errors.ServletError;

public enum BankErrors implements ServletError {
	NEGATIVE_AMOUNT(new GenericError(4001,"Negative amount."));
	
	
	private GenericError error;
	
	private BankErrors(GenericError error) {
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
