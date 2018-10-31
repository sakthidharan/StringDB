package com.sakthi.stringdb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakthi.stringdb.model.Protein;
import com.sakthi.stringdb.model.QProtein;
import com.sakthi.stringdb.repository.ProteinRepository;

@Service
@Transactional
public class ProteinService {

	@Autowired
	private ProteinRepository proteinRepo;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void addOrderedPair(String proteinOneName, String proteinTwoName) {
		QProtein qp = QProtein.protein;
		Optional<Protein> proteinOneOpt = proteinRepo.findOne(qp.name.eq(proteinOneName));
		Optional<Protein> proteinTwoOpt = proteinRepo.findOne(qp.name.eq(proteinTwoName));
	}

}
