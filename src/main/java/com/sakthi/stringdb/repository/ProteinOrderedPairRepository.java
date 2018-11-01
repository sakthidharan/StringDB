package com.sakthi.stringdb.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import com.sakthi.stringdb.model.ProteinOrderedPair;

@Repository
public interface ProteinOrderedPairRepository
		extends JpaRepository<ProteinOrderedPair, Long>, QuerydslPredicateExecutor<ProteinOrderedPair> {

}
