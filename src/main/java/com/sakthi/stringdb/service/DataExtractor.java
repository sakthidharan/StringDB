package com.sakthi.stringdb.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;
import com.sakthi.stringdb.exception.CouldNotDeleteTsvFileException;
import com.sakthi.stringdb.exception.TSVFileNotFoundException;
import com.sakthi.stringdb.model.ProteinDataRecord;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class DataExtractor {

	@Autowired
	private ProteinService proteinService;

	@Autowired
	private FileExistenceChecker fileExistenceChecker;

	public void extract(String tsvDownloadLink, String proteinName) {
		Optional<File> tsvFileOpt = fileExistenceChecker.checkWithMaxWaitTime(tsvDownloadLink, 60);
		if (!tsvFileOpt.isPresent()) {
			throw new TSVFileNotFoundException();
		}
		File tsvFile = tsvFileOpt.get();
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
			in.close();
		} catch (FileNotFoundException e) {
			log.error("TSV File '{}' is not found. Exception is : '{}'", tsvFile.getAbsolutePath(), e.getMessage());
		} catch (IOException e) {
			log.error("Could not close TSV file. Exception is : '{}'", e.getMessage());
		}
		try {
			proteinService.saveDataRecordsForProtein(proteinName, proteinDataRecords);
		} catch (PersistenceException | DataIntegrityViolationException e) {
			log.debug("Could not save data record for protein '{}'", proteinName);
		}
		try {
			Files.delete(tsvFile.toPath());
		} catch (IOException e) {
			log.fatal("Could NOT delete TSV file '{}'", tsvFile.toPath().toString());
			throw new CouldNotDeleteTsvFileException(tsvFile.toPath().toString());
		}
	}

}
