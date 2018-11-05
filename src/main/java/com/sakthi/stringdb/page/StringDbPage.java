package com.sakthi.stringdb.page;

import org.openqa.selenium.firefox.FirefoxDriver;

import lombok.extern.log4j.Log4j2;

@Log4j2
public abstract class StringDbPage {

	protected FirefoxDriver d;

	protected String organismName;

	protected String jsClickElement;

	public StringDbPage(FirefoxDriver d, String organismName, String jsClickElement) {
		this.d = d;
		this.organismName = organismName;
		this.jsClickElement = jsClickElement;
	}

	protected void waitUntilFullPageIsLoaded() {
		while (!d.executeScript("return document.readyState").toString().equals("complete")) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.debug("Thread interrupted while waiting for full page load");
			}
		}
	}
}
