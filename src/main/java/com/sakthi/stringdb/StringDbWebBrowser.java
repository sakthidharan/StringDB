package com.sakthi.stringdb;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.GenericApplicationContext;

import com.sakthi.stringdb.exception.ArgumentNotFoundException;
import com.sakthi.stringdb.exception.NotThisPageException;
import com.sakthi.stringdb.page.DataPage;
import com.sakthi.stringdb.page.MatchChoosePage;
import com.sakthi.stringdb.page.SearchPage;
import com.sakthi.stringdb.service.ProteinService;

import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class StringDbWebBrowser implements ApplicationRunner {

	private static final String GECKO_DRIVER_LOCATION_PROPERTY = "gecko.driver.location";

	private static final String PROTEIN_NAME_PROPERTY = "protein.name";

	private static final String ORGANISM_PROPERTY = "organism";

	@Autowired
	private GenericApplicationContext genericAppCtx;

	@Autowired
	private ProteinService proteinService;

	@Autowired
	private FirefoxOptions firefoxOptions;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (!args.containsOption(GECKO_DRIVER_LOCATION_PROPERTY)) {
			throw new ArgumentNotFoundException(GECKO_DRIVER_LOCATION_PROPERTY);
		}
		String geckoDriverLocation = args.getOptionValues(GECKO_DRIVER_LOCATION_PROPERTY).get(0);
		if (!args.containsOption(PROTEIN_NAME_PROPERTY)) {
			throw new ArgumentNotFoundException(PROTEIN_NAME_PROPERTY);
		}
		String proteinName = args.getOptionValues(PROTEIN_NAME_PROPERTY).get(0);
		if (!args.containsOption(ORGANISM_PROPERTY)) {
			throw new ArgumentNotFoundException(ORGANISM_PROPERTY);
		}
		String organismName = args.getOptionValues(ORGANISM_PROPERTY).get(0);
		log.debug("Starting application");
		System.setProperty("webdriver.gecko.driver", geckoDriverLocation);
		FirefoxDriver d = new FirefoxDriver(firefoxOptions);
		d.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		genericAppCtx.registerBean(FirefoxDriver.class, () -> d);
		genericAppCtx.registerBean(ORGANISM_PROPERTY, String.class, () -> organismName);
		SearchPage searchPage = genericAppCtx.getBean(SearchPage.class);
		MatchChoosePage matchChoosePage = genericAppCtx.getBean(MatchChoosePage.class);
		DataPage dataPage = genericAppCtx.getBean(DataPage.class);
		Optional<String> nextProteinNameOpt = Optional.of(proteinName);
		d.get("https://string-db.org/cgi/input.pl?input_page_show_search=on");
		do {
			proteinName = nextProteinNameOpt.get();
			try {
				searchPage.search(proteinName);
				Thread.sleep(1000);
				matchChoosePage.choose(proteinName);
				Thread.sleep(1000);
			} catch (NotThisPageException e) {
				log.error(e.getMessage());
			}
			try {
				dataPage.extractData(proteinName);
				Thread.sleep(1000);
			} catch (NotThisPageException e) {
				log.error(e.getMessage());
				break;
			}
			nextProteinNameOpt = proteinService.getNextUnexploredProteinName();
		} while (nextProteinNameOpt.isPresent());
		d.quit();
	}

}
