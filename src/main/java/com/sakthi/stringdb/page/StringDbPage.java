package com.sakthi.stringdb.page;

import org.openqa.selenium.firefox.FirefoxDriver;

public abstract class StringDbPage {

	protected FirefoxDriver d;

	protected String organismName;

	protected String jsClickElement;

	public StringDbPage(FirefoxDriver d, String organismName, String jsClickElement) {
		this.d = d;
		this.organismName = organismName;
		this.jsClickElement = jsClickElement;
	}
}
