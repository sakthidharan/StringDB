package com.sakthi.stringdb.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sakthi.stringdb.service.DataExtractor;
import com.sakthi.stringdb.service.ProteinService;

import lombok.extern.log4j.Log4j2;

@Lazy
@Component
@Log4j2
public class DataPage extends StringDbPage {

	@Autowired
	private DataExtractor dataExtractor;

	@Autowired
	private ProteinService proteinService;

	public DataPage(FirefoxDriver d, @Qualifier("organismName") String organismName,
			@Qualifier("jsClickElement") String jsClickElement) {
		super(d, organismName, jsClickElement);
	}

	public void extractData(String proteinName) {
		waitUntilFullPageIsLoaded();
		try {
			WebElement nodesContainer = d.findElement(By.cssSelector("#nodes"));
			log.debug("Got the nodes container");
			List<WebElement> nodes = nodesContainer.findElements(By.tagName("g"));
			for (int index = 0; index < nodes.size(); index++) {
				WebElement node = nodes.get(index);
				WebElement textElem = node.findElement(By.tagName("text"));
				String pairedProteinName = textElem.getText();
				log.debug("Node {} : {}", index, pairedProteinName);
				proteinService.addOrderedPair(organismName, proteinName, pairedProteinName);
			}
			WebElement exportButton = d.findElementByCssSelector("#bottom_page_selector_table");
			d.executeScript(jsClickElement, exportButton);
			WebElement downloadLinkElement = d.findElementByCssSelector(
					"#bottom_page_selector_table_container > div > div:nth-child(2) > div > div:nth-child(4) > div.cell.exporttablecolumn2 > a");
			String tsvDownloadLink = downloadLinkElement.getAttribute("href");
			log.debug("TSV download link : {}", tsvDownloadLink);
			d.executeScript(jsClickElement, downloadLinkElement);
			dataExtractor.extract(tsvDownloadLink, proteinName);
			WebElement goBackToSearchPage = d.findElementByCssSelector("#search");
			d.executeScript(jsClickElement, goBackToSearchPage);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
		}
	}

}
