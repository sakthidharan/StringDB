package com.sakthi.stringdb.page;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sakthi.stringdb.exception.NotThisPageException;

import lombok.extern.log4j.Log4j2;

@Lazy
@Component
@Log4j2
public class SearchPage extends StringDbPage {

	public SearchPage(FirefoxDriver d, String organism) {
		super(d, organism);
	}

	public void search(String proteinName) {
		log.debug("Title of this page is : {}", d.getTitle());
		String searchPageTitleContent = "functional protein association networks";
		if (!d.getTitle().contains(searchPageTitleContent)) {
			throw new NotThisPageException("This is not the starting Search page. Protein is " + proteinName);
		}
		try {
			d.findElementByCssSelector("#primary_input\\:single_identifier").sendKeys(proteinName);
			d.findElementByCssSelector("#species_text_single_identifier").sendKeys(organismName);
			d.findElementByCssSelector("#input_form_single_identifier > div:nth-child(5) > a:nth-child(1)").click();
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
		}
	}
}
