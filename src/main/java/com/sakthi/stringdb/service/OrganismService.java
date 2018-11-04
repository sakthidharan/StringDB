package com.sakthi.stringdb.service;

import java.util.Optional;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

import com.sakthi.stringdb.model.Organism;
import com.sakthi.stringdb.model.QOrganism;
import com.sakthi.stringdb.repository.OrganismRepository;

@Lazy
@Service
@Transactional
public class OrganismService {

	@Autowired(required = true)
	@Qualifier("organismName")
	private String organismName;

	private Organism organism;

	@Autowired
	private OrganismRepository organismRepo;

	@Autowired
	private TransactionTemplate txnTemplate;

	public OrganismService(String organismName) {
		super();
		this.organismName = organismName;
	}

	@PostConstruct
	public void afterPropertiesSet() {
		txnTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
		organism = createOrGet(organismName);
	}

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public Organism getOrCreate(String inputOrganismName) {
		if (organism.getName().equals(inputOrganismName))
			return organism;
		else {
			organism = createOrGet(inputOrganismName);
			organismName = organism.getName();
			return organism;
		}
	}

	private Organism createOrGet(String orgName) {
		QOrganism qOrg = QOrganism.organism;		
		Optional<Organism> orgOpt = organismRepo.findOne(qOrg.name.eq(organismName));
		if(!orgOpt.isPresent()) {
			TransactionCallback<Organism> createOrganism = txnStatus -> organismRepo.save(new Organism(orgName));
			return txnTemplate.execute(createOrganism);
		}else {
			return orgOpt.get();
		}		
	}

	@Transactional(readOnly=true)
	public Organism getCurrentOrganism() {
		return organism;
	}

}
