package com.sakthi.stringdb.page;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Lazy
@Component
@Log4j2
public class SearchPage extends StringDbPage {

	public SearchPage(FirefoxDriver d, @Qualifier("organismName") String organismName,
			@Qualifier("jsClickElement") String jsClickElement) {
		super(d, organismName, jsClickElement);
	}

	public void search(String proteinName) {
		waitUntilFullPageIsLoaded();
		try {
			d.findElementByCssSelector("#primary_input\\:single_identifier").sendKeys(proteinName);
			d.findElementByCssSelector("#species_text_single_identifier").sendKeys(organismName);
			WebElement searchButton = d
					.findElementByCssSelector("#input_form_single_identifier > div:nth-child(5) > a:nth-child(1)");
			searchButton.click();
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
		}
	}
}
