package com.sakthi.stringdb.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper=false)
public class ArgumentNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7855034939122196445L;
	
	private String missingArgument;

	@Override
	public String getMessage() {
		return super.getMessage();
	}	

}
