package com.sakthi.stringdb.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.sakthi.stringdb.model.Organism;
import com.sakthi.stringdb.model.Protein;
import com.sakthi.stringdb.model.ProteinDataRecord;
import com.sakthi.stringdb.model.ProteinOrderedPair;
import com.sakthi.stringdb.model.QProtein;
import com.sakthi.stringdb.repository.ProteinDataRecordRepository;
import com.sakthi.stringdb.repository.ProteinOrderedPairRepository;
import com.sakthi.stringdb.repository.ProteinRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class ProteinService {

	private OrganismService organismService;

	@Autowired
	private ProteinRepository proteinRepo;

	@Autowired
	private ProteinOrderedPairRepository proteinOrderedPairRepo;

	@Autowired
	private ProteinDataRecordRepository proteinDataRecRepo;

	@Autowired
	private TransactionTemplate txnTemplate;

	@Autowired
	private GenericApplicationContext genericAppCtx;

	@PostConstruct
	public void afterPropertiesSet() {
		txnTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void addOrderedPair(String organismName, String proteinOneName, String proteinTwoName) {
		if (organismService == null) {
			organismService = genericAppCtx.getBean(OrganismService.class);
		}
		Organism organism = organismService.getCurrentOrganism();

		QProtein qp = QProtein.protein;
		TransactionCallback<Protein> createProteinOne = txnStatus -> proteinRepo.save(new Protein(proteinOneName));
		TransactionCallback<Protein> createProteinTwo = txnStatus -> proteinRepo.save(new Protein(proteinTwoName));
		Protein proteinOne = proteinRepo.findOne(qp.name.eq(proteinOneName))
				.orElse(txnTemplate.execute(createProteinOne));
		Protein proteinTwo = proteinRepo.findOne(qp.name.eq(proteinTwoName))
				.orElse(txnTemplate.execute(createProteinTwo));
		TransactionCallback<ProteinOrderedPair> createProteinOrderedPair = txnStatus -> proteinOrderedPairRepo
				.save(new ProteinOrderedPair(organism, proteinOne, proteinTwo));
		try {
			txnTemplate.execute(createProteinOrderedPair);
		} catch (DataIntegrityViolationException | PersistenceException e) {
			log.debug("Protein Ordered Pair ({},{}) already exists.", proteinOneName, proteinTwoName);
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void saveDataRecordsForProtein(String proteinName, List<ProteinDataRecord> proteinDataRecords) {
		if (organismService == null) {
			organismService = genericAppCtx.getBean(OrganismService.class);
		}
		Organism organism = organismService.getCurrentOrganism();
		QProtein qp = QProtein.protein;
		Optional<Protein> proteinOpt = proteinRepo.findOne(qp.name.eq(proteinName));
		if (proteinOpt.isPresent()) {
			for (ProteinDataRecord proteinDataRecord : proteinDataRecords) {
				proteinDataRecord.setProtein(proteinOpt.get());
				proteinDataRecord.setOrganism(organism);
				proteinDataRecRepo.save(proteinDataRecord);
			}
		} else {
			log.error("Protein '{}' Not found when about to save it's data record", proteinName);
		}
	}

}
