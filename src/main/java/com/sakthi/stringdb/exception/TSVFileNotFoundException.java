package com.sakthi.stringdb.exception;

public class TSVFileNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5104681462543925807L;

	private final String message;

	public TSVFileNotFoundException() {
		this.message = "The TSV file to download could not be found.";
	}

	@Override
	public String getMessage() {
		return message;
	}

}
