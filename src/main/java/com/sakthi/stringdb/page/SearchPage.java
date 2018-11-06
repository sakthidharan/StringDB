package com.sakthi.stringdb.page;

import java.util.List;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
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

	public boolean search(String proteinName) {
		waitUntilFullPageIsLoaded();
		try {
			WebElement prtNameInputTextBox = d.findElementByCssSelector("#primary_input\\:single_identifier");
			prtNameInputTextBox.clear();
			prtNameInputTextBox.sendKeys(proteinName);
			d.findElementByCssSelector("#organism_text_input_single_identifier > div:nth-child(2) > div:nth-child(2)")
					.click();// drop down click
			WebElement organismInputDropDownElement = d.findElementByCssSelector("#speciesList_single_identifier");
			Select organismSelect = new Select(organismInputDropDownElement);
			organismSelect.selectByValue("83332");
			d.findElementByCssSelector("#speciesFloatingDiv_single_identifier > div:nth-child(1) > button:nth-child(5)")
					.click();// after option selected in drop down, click it's own select button
			d.findElementByCssSelector("#input_form_single_identifier").submit();
			waitUntilFullPageIsLoaded();
			List<WebElement> elementsInProteinNotFoundPage = d.findElementsByCssSelector("#error_form");
			if (elementsInProteinNotFoundPage.isEmpty()) {
				// protein is found
				return true;
			} else {
				// protein not found
				elementsInProteinNotFoundPage.get(0).click();// click the start over element
				return false;
			}
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
			return false;
		}
	}
}
