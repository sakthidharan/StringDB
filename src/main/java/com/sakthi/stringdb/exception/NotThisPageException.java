package com.sakthi.stringdb.exception;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Value
@EqualsAndHashCode(callSuper = false)
public class NotThisPageException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5884049692647448149L;

	private final String message;
}
