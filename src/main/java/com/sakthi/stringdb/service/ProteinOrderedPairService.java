package com.sakthi.stringdb.service;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sakthi.stringdb.model.Organism;
import com.sakthi.stringdb.model.Protein;
import com.sakthi.stringdb.model.ProteinOrderedPair;
import com.sakthi.stringdb.model.QOrganism;
import com.sakthi.stringdb.model.QProtein;
import com.sakthi.stringdb.model.QProteinOrderedPair;
import com.sakthi.stringdb.repository.OrganismRepository;
import com.sakthi.stringdb.repository.ProteinOrderedPairRepository;
import com.sakthi.stringdb.repository.ProteinRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Transactional
@Log4j2
public class ProteinOrderedPairService {

	@Autowired
	private ProteinOrderedPairRepository proteinOrderedPairRepo;

	@Autowired
	private ProteinRepository proteinRepo;

	@Autowired
	private OrganismRepository organismRepo;

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void create(Organism organism, Protein proteinOne, Protein proteinTwo) {
		proteinOrderedPairRepo.save(new ProteinOrderedPair(organism, proteinOne, proteinTwo, true));
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void markProteinTwoAsInvalid(String organismName, String proteinTwoName) {
		QProtein qp = QProtein.protein;
		Optional<Protein> prtTwoOpt = proteinRepo.findOne(qp.name.eq(proteinTwoName));
		if (!prtTwoOpt.isPresent()) {
			log.fatal("How come protein two '{}' is not found ???", proteinTwoName);
			return;
		}
		QOrganism qo = QOrganism.organism;
		Optional<Organism> orgOpt = organismRepo.findOne(qo.name.eq(organismName));
		if (!orgOpt.isPresent()) {
			log.fatal("How come organism '{}' is not found ???", organismName);
			return;
		}
		QProteinOrderedPair qpop = QProteinOrderedPair.proteinOrderedPair;
		Iterator<ProteinOrderedPair> popIter = proteinOrderedPairRepo
				.findAll(qpop.organism.eq(orgOpt.get()).and(qpop.proteinTwo.eq(prtTwoOpt.get()))).iterator();
		while (popIter.hasNext()) {
			ProteinOrderedPair pop = popIter.next();
			pop.setProteinTwoValidity(false);
			proteinOrderedPairRepo.save(pop);
		}
	}

}
