package com.sakthi.stringdb.service;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Paths;
import java.util.Optional;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class FileExistenceChecker {

	@Value("${tsv.download.directory}")
	private String tsvDownloadDirectory;

	public Optional<File> checkWithMaxWaitTime(String tsvDownloadLink, int maxWaitTimeSeconds) {
		Optional<URI> uriOpt = convertToUri(tsvDownloadLink);
		if (!uriOpt.isPresent()) {
			return Optional.empty();
		}
		Optional<NameValuePair> nvpOpt = URLEncodedUtils.parse(uriOpt.get(), Charset.forName("UTF-8")).stream()
				.filter(nvp -> nvp.getName().equals("download_file_name")).findFirst();
		if (!nvpOpt.isPresent()) {
			log.error("Key 'download_file_name' not found in query string of tsvDownloadLink '{}'", tsvDownloadLink);
			return Optional.empty();
		}
		String tsvFileName = nvpOpt.get().getValue();
		File tsvFile = Paths.get(tsvDownloadDirectory, tsvFileName).toFile();
		int totalWaitTimeSeconds = 0;
		while (totalWaitTimeSeconds <= maxWaitTimeSeconds) {
			if (tsvFile.exists()) {
				return Optional.of(tsvFile);
			}
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				Thread.currentThread().interrupt();
				log.error("Thread sleep interrupted : '{}'", e.getMessage());
			}
			totalWaitTimeSeconds += 5;
		}
		log.error("File did not get downloaded within even 60 seconds after link click");
		return Optional.empty();
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
