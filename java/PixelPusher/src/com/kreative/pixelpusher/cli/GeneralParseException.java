package com.kreative.pixelpusher.cli;

public class GeneralParseException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	public GeneralParseException(String message, String badString) {
		super(message + ": " + badString);
	}
	
	public GeneralParseException(String message, String badString, Throwable cause) {
		super(message + ": " + badString, cause);
	}
}
