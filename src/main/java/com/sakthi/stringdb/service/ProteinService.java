package com.sakthi.stringdb.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sakthi.stringdb.repository.ProteinRepository;

@Service
@Transactional
public class ProteinService {

	@Autowired
	private ProteinRepository proteinRepo;

	@Transactional(readOnly = false, rollbackFor = Exception.class)
	public void addOrderedPair(String proteinOne, String proteinTwo) {
		
	}

}
