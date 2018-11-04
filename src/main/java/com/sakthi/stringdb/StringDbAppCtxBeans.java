package com.sakthi.stringdb;

import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:string-db.properties")
public class StringDbAppCtxBeans {

	@Bean
	public FirefoxOptions firefoxOptions() {
		FirefoxOptions firefoxOptions = new FirefoxOptions();
//		FirefoxBinary firefoxBinary = new FirefoxBinary();
//		firefoxBinary.addCommandLineOptions("--headless");		
//		firefoxOptions.setBinary(firefoxBinary);
		FirefoxProfile profile = new FirefoxProfile();
		profile.setPreference("browser.download.dir", "C:\\Users\\COMPUTER\\Downloads");
		profile.setPreference("browser.download.folderList", 2);
		profile.setPreference("browser.helperApps.neverAsk.openFile", "text/tab-separated-values");
		profile.setPreference("browser.helperApps.neverAsk.saveToDisk", "text/tab-separated-values");
		profile.setPreference("browser.helperApps.alwaysAsk.force", false);
		profile.setPreference("browser.download.manager.alertOnEXEOpen", false);
		profile.setPreference("browser.download.manager.focusWhenStarting", false);
		profile.setPreference("browser.download.manager.useWindow", false);
		profile.setPreference("browser.download.manager.showAlertOnComplete", false);
		profile.setPreference("browser.download.manager.closeWhenDone", false);
		profile.setPreference("browser.download.manager.showWhenStarting", false);
		profile.setPreference("pdfjs.disabled", true);
		firefoxOptions.setProfile(profile);
		return firefoxOptions;
	}
}
