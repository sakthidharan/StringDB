package com.sakthi.stringdb.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.EqualsAndHashCode;
import lombok.Value;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_organism_protein_explored", columnNames = { "organism_id",
		"protein_id" }))
@Value
@EqualsAndHashCode(callSuper = false)
public class OrganismProteinExplored extends RootModel {

	@ManyToOne(optional = false)
	@JoinColumn(name = "organism_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_organismProtein_organism"), nullable = false)
	private Organism organism;

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_organismProtein_proteinOne"), nullable = false)
	private Protein protein;

}
