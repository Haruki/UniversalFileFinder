package com.pimpelkram.universalfilefinder.config;

public enum ErrorTypes {

	FileNotFound("FileNotFound"), InvalidJson("InvalidJson");

	private String name;

	private ErrorTypes(String name) {
		this.name = name;
	}

}
