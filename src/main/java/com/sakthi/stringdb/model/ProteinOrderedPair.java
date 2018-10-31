package com.sakthi.stringdb.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_protein_ordered_pair", columnNames = { "proteinOne",
		"proteinTwo" }))
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ProteinOrderedPair extends RootModel {

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_one_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinOrderedPair_proteinOne"), nullable = false)
	private Protein proteinOne;

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_two_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinOrderedPair_proteinTwo"), nullable = false)
	private Protein proteinTwo;
}
