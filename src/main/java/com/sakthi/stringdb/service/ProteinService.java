package com.sakthi.stringdb.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sakthi.stringdb.model.Organism;
import com.sakthi.stringdb.model.OrganismProtein;
import com.sakthi.stringdb.model.Protein;
import com.sakthi.stringdb.model.ProteinDataRecord;
import com.sakthi.stringdb.model.ProteinOrderedPair;
import com.sakthi.stringdb.model.QOrganismProtein;
import com.sakthi.stringdb.model.QProtein;
import com.sakthi.stringdb.model.QProteinOrderedPair;
import com.sakthi.stringdb.repository.ProteinDataRecordRepository;
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
	private ProteinDataRecordRepository proteinDataRecRepo;

	@Autowired
	private TransactionTemplate txnTemplate;

	@Autowired
	private GenericApplicationContext genericAppCtx;

	@Autowired
	private ProteinOrderedPairService proteinOrderedPairService;

	@Autowired
	private OrganismProteinService organismProteinService;

	@PersistenceContext
	private EntityManager entityManager;

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
		Optional<Protein> proteinOneOpt = proteinRepo.findOne(qp.name.eq(proteinOneName));
		Protein proteinOne = null;
		if (proteinOneOpt.isPresent()) {
			proteinOne = proteinOneOpt.get();
		} else {
			proteinOne = txnTemplate.execute(createProteinOne);
		}
		Optional<Protein> proteinTwoOpt = proteinRepo.findOne(qp.name.eq(proteinTwoName));
		Protein proteinTwo = null;
		if (proteinTwoOpt.isPresent()) {
			proteinTwo = proteinTwoOpt.get();
		} else {
			proteinTwo = txnTemplate.execute(createProteinTwo);
		}
		try {
			proteinOrderedPairService.create(organism, proteinOne, proteinTwo);
		} catch (DataIntegrityViolationException | PersistenceException e) {
			log.debug("Protein Ordered Pair ({},{}) already exists.", proteinOneName, proteinTwoName);
		}
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void saveDataRecordsForProtein(String proteinName, List<ProteinDataRecord> proteinDataRecords) {
		if (organismService == null) {
			organismService = genericAppCtx.getBean(OrganismService.class);
		}
		Organism organism = organismService.getCurrentOrganism();
		QProtein qp = QProtein.protein;
		Optional<Protein> proteinOpt = proteinRepo.findOne(qp.name.eq(proteinName));
		if (proteinOpt.isPresent()) {
			for (ProteinDataRecord proteinDataRecord : proteinDataRecords) {
				Optional<OrganismProtein> orgPrtOpt = organismProteinService.findByOrganismAndProtein(organism,
						proteinOpt.get());
				OrganismProtein orgPrt = null;
				if (orgPrtOpt.isPresent()) {
					orgPrt = orgPrtOpt.get();
				} else {
					orgPrt = organismProteinService.create(organism, proteinOpt.get());
				}
				proteinDataRecord.setOrganismProtein(orgPrt);
				proteinDataRecRepo.save(proteinDataRecord);
			}
		} else {
			log.error("Protein '{}' Not found when it's data record was about to be saved.", proteinName);
		}
	}

	@Transactional(readOnly = true)
	public Optional<String> getNextUnexploredProteinName() {
		if (organismService == null) {
			organismService = genericAppCtx.getBean(OrganismService.class);
		}
		Organism organism = organismService.getCurrentOrganism();
		QOrganismProtein qop = QOrganismProtein.organismProtein;
		QProteinOrderedPair qPair = QProteinOrderedPair.proteinOrderedPair;
		JPAQueryFactory queryFactory = new JPAQueryFactory(entityManager);
		Optional<ProteinOrderedPair> proteinOrderedPairOpt = Optional.ofNullable(queryFactory.selectFrom(qPair)
				.leftJoin(qop).on(qPair.proteinTwo.eq(qop.protein).and(qPair.organism.eq(qop.organism)))
				.where(qop.protein.isNull().and(qPair.organism.eq(organism))).fetchFirst());
		if (proteinOrderedPairOpt.isPresent()) {
			return Optional.of(proteinOrderedPairOpt.get().getProteinTwo().getName());
		} else {
			return Optional.empty();
		}
	}

}
