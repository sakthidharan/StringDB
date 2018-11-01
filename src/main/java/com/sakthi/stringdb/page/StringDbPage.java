package com.sakthi.stringdb.page;

import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class StringDbPage {
	
	protected FirefoxDriver d;

	protected String organismName;

	public StringDbPage(FirefoxDriver d, String organism) {
		this.d = d;
		this.organismName = organism;
	}
}
