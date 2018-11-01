package com.sakthi.stringdb.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBeanBuilder;
import com.sakthi.stringdb.model.ProteinDataRecord;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DataDownloader {

	@Autowired
	private ProteinService proteinService;

	public void download(String tsvDownloadLink, String proteinName) {
		Optional<URL> urlOpt = convertToUrl(tsvDownloadLink);
		if (!urlOpt.isPresent()) {
			return;
		}
		URL tsvDownloadURL = urlOpt.get();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(tsvDownloadURL.openStream()));
			CsvToBeanBuilder<ProteinDataRecord> csvToBeanBuilder = new CsvToBeanBuilder<>(in);
			List<ProteinDataRecord> proteinDataRecords = csvToBeanBuilder.withType(ProteinDataRecord.class).build()
					.parse();
			proteinService.saveDataRecordsForProtein(proteinName, proteinDataRecords);
		} catch (IOException e) {
			log.error("Error opening IO stream to tsvDownloadLink '{}' : '{}'", tsvDownloadURL.toString(),
					e.getMessage());
		}
	}

	private Optional<URL> convertToUrl(String tsvDownloadLink) {
		URL url = null;
		try {
			url = new URL(tsvDownloadLink);
		} catch (MalformedURLException e) {
			log.debug("Malformed Download link : '{}'", tsvDownloadLink);
			return Optional.empty();
		}
		return Optional.ofNullable(url);
	}

}
