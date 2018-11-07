package com.sakthi.stringdb.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sakthi.stringdb.model.Organism;
import com.sakthi.stringdb.model.OrganismProtein;
import com.sakthi.stringdb.model.Protein;
import com.sakthi.stringdb.model.QOrganismProtein;
import com.sakthi.stringdb.repository.OrganismProteinRepository;

@Service
@Transactional
public class OrganismProteinService {

	@Autowired
	private OrganismProteinRepository orgPrtRepo;

	@Transactional(readOnly = false, rollbackFor = Exception.class, propagation = Propagation.REQUIRES_NEW)
	public OrganismProtein getOrCreate(Organism organism, Protein protein) {
		QOrganismProtein qop = QOrganismProtein.organismProtein;
		Optional<OrganismProtein> orgPrtOpt = orgPrtRepo
				.findOne(qop.organism.eq(organism).and(qop.protein.eq(protein)));
		if (orgPrtOpt.isPresent()) {
			return orgPrtOpt.get();
		} else {
			return orgPrtRepo.save(new OrganismProtein(organism, protein));
		}
	}

	@Transactional(readOnly = true)
	public boolean proteinAlreadyExists(String organismName, String proteinName) {
		QOrganismProtein qop = QOrganismProtein.organismProtein;
		return orgPrtRepo.exists(qop.protein.name.eq(proteinName).and(qop.organism.name.eq(organismName)));
	}

}
