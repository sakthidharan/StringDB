package com.sakthi.stringdb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.sakthi.stringdb.model.ProteinDataRecord;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DataExtractor {

	@Autowired
	private ProteinService proteinService;

	public void extract(String tsvDownloadLink, String proteinName) {
		Optional<URI> uriOpt = convertToUri(tsvDownloadLink);
		if (!uriOpt.isPresent()) {
			return;
		}
		Optional<NameValuePair> nvpOpt = URLEncodedUtils.parse(uriOpt.get(), Charset.forName("UTF-8")).stream()
				.filter(nvp -> nvp.getName().equals("download_file_name")).findFirst();
		if (!nvpOpt.isPresent()) {
			log.error("Key 'download_file_name' not found in query string of tsvDownloadLink '{}'", tsvDownloadLink);
			return;
		}
		String tsvFileName = nvpOpt.get().getValue();
		File tsvFile = Paths.get("C:\\Users\\COMPUTER\\Downloads", tsvFileName).toFile();
		List<ProteinDataRecord> proteinDataRecords = new ArrayList<>();
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(tsvFile)));
			CsvToBeanBuilder<ProteinDataRecord> csvToBeanBuilder = new CsvToBeanBuilder<>(in);
			csvToBeanBuilder.withSeparator('\t');
			HeaderColumnNameMappingStrategy<ProteinDataRecord> mappingStrategy = new HeaderColumnNameMappingStrategy<>();
			mappingStrategy.setType(ProteinDataRecord.class);
			CsvToBean<ProteinDataRecord> csvToBean = csvToBeanBuilder.withType(ProteinDataRecord.class)
					.withMappingStrategy(mappingStrategy).build();
			proteinDataRecords = csvToBean.parse();			
		} catch (FileNotFoundException e) {
			log.error("TSV File '{}' is not found. Exception is : '{}'", tsvFile.getAbsolutePath(), e.getMessage());
		}
		try {
			proteinService.saveDataRecordsForProtein(proteinName, proteinDataRecords);
		}catch(PersistenceException | DataIntegrityViolationException e) {
			log.debug("Could not save data record for protein '{}'", proteinName);
		}
		try {
			proteinService.markProteinAsExplored(proteinName);
		}catch(PersistenceException | DataIntegrityViolationException e) {
			log.error("Could not mark protein '{}' as explored.", proteinName);
		}
	}

	private Optional<URI> convertToUri(String tsvDownloadLink) {
		URI uri = null;
		try {
			uri = new URI(tsvDownloadLink);
		} catch (URISyntaxException e) {
			log.debug("Malformed Download link : '{}'", tsvDownloadLink);
			return Optional.empty();
		}
		return Optional.ofNullable(uri);
	}

}
