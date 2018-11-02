package com.sakthi.stringdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sakthi.stringdb.model.Organism;
import com.sakthi.stringdb.model.Protein;
import com.sakthi.stringdb.model.ProteinOrderedPair;
import com.sakthi.stringdb.repository.ProteinOrderedPairRepository;

@Service
@Transactional
public class ProteinOrderedPairService {

	@Autowired
	private ProteinOrderedPairRepository proteinOrderedPairRepo;

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public void create(Organism organism, Protein proteinOne, Protein proteinTwo) {
		proteinOrderedPairRepo.save(new ProteinOrderedPair(organism, proteinOne, proteinTwo));
	}

}
