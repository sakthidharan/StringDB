package com.sakthi.stringdb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.sakthi.stringdb.exception.ArgumentNotFoundException;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class StringDbWebBrowser implements ApplicationRunner {

	private static final String GECKO_DRIVER_LOCATION_PROPERTY = "gecko.driver.location";

	@Override
	public void run(ApplicationArguments args) throws Exception {
		String geckoDriverLocation = null;
		if (args.containsOption(GECKO_DRIVER_LOCATION_PROPERTY)) {
			geckoDriverLocation = args.getOptionValues(GECKO_DRIVER_LOCATION_PROPERTY).get(0);
		} else {
			throw new ArgumentNotFoundException(GECKO_DRIVER_LOCATION_PROPERTY);
		}

		log.debug("Starting application");
		System.setProperty("webdriver.gecko.driver", geckoDriverLocation);
		FirefoxBinary firefoxBinary = new FirefoxBinary();
		firefoxBinary.addCommandLineOptions("--headless");
		FirefoxOptions firefoxOptions = new FirefoxOptions();
		firefoxOptions.setBinary(firefoxBinary);
		FirefoxDriver d = new FirefoxDriver(firefoxOptions);
		d.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		try {
			d.get("https://string-db.org/cgi/input.pl?input_page_show_search=on");
			log.debug("title is : {}", d.getTitle());
			d.findElementByCssSelector("#primary_input\\:single_identifier").sendKeys("clpC1");
			d.findElementByCssSelector("#species_text_single_identifier").sendKeys("Mycobacterium tuberculosis H37Rv");
			d.findElementByCssSelector("#input_form_single_identifier > div:nth-child(5) > a:nth-child(1)").click();
			WebElement table = d.findElementByCssSelector(".proceed_form_table");
			log.debug("Table as text : {}", table.getText());
			List<WebElement> rows = table.findElements(By.xpath("div"));
			for (int i = 1; i < rows.size(); i++) {
				WebElement row = rows.get(i);
				log.debug("Row as text : {}", row.getText());
				WebElement proteinDescription = row.findElement(By.className("proceed_form_item_column"));
				String text = proteinDescription.getText().trim();
				log.debug("protein description : {}", text);
				if (text.startsWith("clpC1")) {
					log.debug("Found match. So breaking loop");
					WebElement inputRadioButton = row.findElement(By.className("proceed_form_organism_column"))
							.findElement(By.tagName("input"));
					try {
						inputRadioButton.click();
					} catch (ElementClickInterceptedException e) {
						log.debug("Radio button could not be clicked because it was obscured");
					}
					d.findElementByCssSelector("a.button:nth-child(15)").click();
					break;
				}
			}
			log.debug("In the final page");
			WebElement nodesContainer = d.findElement(By.cssSelector("#nodes"));
			log.debug("Got the nodes container");
			List<WebElement> nodes = nodesContainer.findElements(By.tagName("g"));
			for (int index = 0; index < nodes.size(); index++) {
				WebElement node = nodes.get(index);
				WebElement textElem = node.findElement(By.tagName("text"));
				log.debug("Node {} : {}", index, textElem.getText());
			}
			log.debug("About to click export button");
			WebElement exportButton = d.findElementByCssSelector("#bottom_page_selector_table");
			exportButton.click();
			String tsvDownloadLink = d.findElementByCssSelector(
					"#bottom_page_selector_table_container > div > div:nth-child(2) > div > div:nth-child(4) > div.cell.exporttablecolumn2 > a")
					.getAttribute("href");
			log.debug("TSV download link : {}",tsvDownloadLink);
		} catch (NoSuchElementException e) {
			log.error("Exception : {}", e.getMessage());
		}
		d.quit();
	}

}
