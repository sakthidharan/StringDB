package com.sakthi.stringdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.sakthi.stringdb.model.ProteinDataRecord;
import com.sakthi.stringdb.repository.ProteinDataRecordRepository;

@Service
@Transactional
public class ProteinDataRecordService {
	
	@Autowired
	private ProteinDataRecordRepository proteinDataRecRepo;
	
	@Transactional(readOnly=false, rollbackFor=Exception.class, propagation=Propagation.REQUIRES_NEW)
	public void save(ProteinDataRecord proteinDataRecord) {
		proteinDataRecRepo.save(proteinDataRecord);
	}

}
