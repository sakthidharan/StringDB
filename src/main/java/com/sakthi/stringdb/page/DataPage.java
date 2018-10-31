package com.sakthi.stringdb.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.sakthi.stringdb.exception.NotThisPageException;
import com.sakthi.stringdb.service.DataDownloader;
import com.sakthi.stringdb.service.ProteinService;

import lombok.extern.log4j.Log4j2;

@Lazy
@Component
@Log4j2
public class DataPage extends StringDbPage {

	@Autowired
	private DataDownloader dataDownloader;
	
	@Autowired
	private ProteinService proteinService;
	
	public DataPage(FirefoxDriver d, String organism) {
		super(d, organism);
	}

	public void extractData(String proteinName) {
		log.debug("Title of this page is : {}", d.getTitle());
		StringBuilder dataPageTitleContent = new StringBuilder();
		dataPageTitleContent.append(proteinName).append(" protein (").append(organism)
				.append(") - STRING interaction network");
		if (!d.getTitle().contains(dataPageTitleContent.toString())) {
			throw new NotThisPageException("This is not the DataPage for protein " + proteinName);
		}
		try {
			WebElement nodesContainer = d.findElement(By.cssSelector("#nodes"));
			log.debug("Got the nodes container");
			List<WebElement> nodes = nodesContainer.findElements(By.tagName("g"));
			for (int index = 0; index < nodes.size(); index++) {
				WebElement node = nodes.get(index);
				WebElement textElem = node.findElement(By.tagName("text"));
				String pairedProteinName = textElem.getText();
				log.debug("Node {} : {}", index, pairedProteinName);
				proteinService.addOrderedPair(proteinName, pairedProteinName);
			}
			log.debug("About to click export button");
			WebElement exportButton = d.findElementByCssSelector("#bottom_page_selector_table");
			exportButton.click();
			String tsvDownloadLink = d.findElementByCssSelector(
					"#bottom_page_selector_table_container > div > div:nth-child(2) > div > div:nth-child(4) > div.cell.exporttablecolumn2 > a")
					.getAttribute("href");
			log.debug("TSV download link : {}", tsvDownloadLink);
			dataDownloader.download(tsvDownloadLink);
		} catch (NoSuchElementException e) {
			log.error(e.getMessage());
		}
	}

}
