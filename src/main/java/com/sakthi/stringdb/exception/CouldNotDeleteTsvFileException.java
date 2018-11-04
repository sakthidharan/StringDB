package com.sakthi.stringdb.exception;

import lombok.Getter;

@Getter
public class CouldNotDeleteTsvFileException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2971807907194380971L;

	private final String message;

	public CouldNotDeleteTsvFileException(String tsvFilePath) {
		this.message = "Could NOT delete TSV file " + tsvFilePath;
	}

}
