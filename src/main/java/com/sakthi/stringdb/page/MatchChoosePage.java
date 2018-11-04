package com.sakthi.stringdb.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sakthi.stringdb.exception.NotThisPageException;

import lombok.extern.log4j.Log4j2;

@Lazy
@Component
@Log4j2
public class MatchChoosePage extends StringDbPage {

	public MatchChoosePage(FirefoxDriver d, String organism, String jsClickElement) {
		super(d, organism, jsClickElement);
	}

	public boolean choose(String proteinName) {
		StringBuilder matchChoosePageTitleContent = new StringBuilder();
		matchChoosePageTitleContent.append(proteinName).append(" - STRING protein network");
		if (!d.getTitle().contains(matchChoosePageTitleContent.toString())) {
			throw new NotThisPageException("This is not the MatchChoosePage for protein " + proteinName);
		}
		boolean matchFound = false;
		try {
			WebElement table = d.findElementByCssSelector(".proceed_form_table");
			List<WebElement> rows = table.findElements(By.xpath("div"));
			for (int i = 1; i < rows.size(); i++) {
				WebElement row = rows.get(i);
				WebElement proteinDescription = row.findElement(By.className("proceed_form_item_column"));
				String text = proteinDescription.getText().trim();
				if (text.startsWith(proteinName)) {
					matchFound = true;
					log.debug("Found match. So breaking loop");
					WebElement inputRadioButton = row.findElement(By.className("proceed_form_organism_column"))
							.findElement(By.tagName("input"));
					clickRadioButton(inputRadioButton);
					WebElement continueToDataPage = d.findElementByCssSelector("a.button:nth-child(15)");
					d.executeScript(jsClickElement, continueToDataPage);
					break;
				}
			}
			if (!matchFound) {
				log.error("No match found in MatchChoosePage for protein {} in organism {}", proteinName, organismName);
			}
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
		}
		return matchFound;
	}

	private void clickRadioButton(WebElement inputRadioButton) {
		try {
			inputRadioButton.click();			
		} catch (ElementClickInterceptedException e) {
			log.debug("Radio button could not be clicked because it was obscured");
		}
	}

}
