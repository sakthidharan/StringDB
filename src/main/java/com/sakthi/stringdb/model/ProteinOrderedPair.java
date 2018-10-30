package com.sakthi.stringdb.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProteinOrderedPair extends RootModel {

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_one_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinOrderedPair_proteinOne"), nullable = false)
	private Protein proteinOne;

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_two_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinOrderedPair_proteinTwo"), nullable = false)
	private Protein proteinTwo;
}
