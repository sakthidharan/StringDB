package com.sakthi.stringdb;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
//@Slf4j
@Log4j2
public class StringDbWebBrowser implements ApplicationRunner {

	@Override
	public void run(ApplicationArguments args) throws Exception {
		log.debug("Starting application");
//		System.setProperty("webdriver.chrome.driver", "/home/sakthi/Softwares/installedSoftwares/chromedriver");
//		ChromeOptions chromeOptions = new ChromeOptions();
//	      chromeOptions.setBinary("/Applications/Google Chrome Canary.app/Contents/MacOS/Google Chrome Canary");
//		chromeOptions.addArguments("--headless");
//		ChromeDriver d = new ChromeDriver(chromeOptions);
		System.setProperty("webdriver.gecko.driver", "/home/sakthi/Softwares/installedSoftwares/geckodriver");
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
			// Thread.sleep(5000);
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
					inputRadioButton.click();
					log.debug("clicked radio button");
					d.findElementByCssSelector("a.button:nth-child(15)").click();
					break;
				}
				d.findElementByCssSelector("#bottom_page_selector_table").click();
			}
		} catch (NoSuchElementException e) {
			log.error("Exception : {}", e.getMessage());
		}
//		d.close();
		d.quit();
	}

}
