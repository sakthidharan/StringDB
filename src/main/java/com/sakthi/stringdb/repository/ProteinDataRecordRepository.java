package com.sakthi.stringdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.sakthi.stringdb.model.ProteinDataRecord;

@Repository
public interface ProteinDataRecordRepository
		extends JpaRepository<ProteinDataRecord, Long>, QuerydslPredicateExecutor<ProteinDataRecord> {

}
