package com.sakthi.stringdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;

public class StringDbApplication {

	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(StringDbWebBrowser.class);
		app.setWebApplicationType(WebApplicationType.NONE);
		app.run(args);
	}	
	
}
