package com.sakthi.stringdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_protein_name", columnNames = { "name" }))
@Value
@EqualsAndHashCode(callSuper = false)
public class Protein extends RootModel {

	@Column(nullable = false)
	private String name;

}
