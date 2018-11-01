package com.sakthi.stringdb.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.opencsv.bean.CsvBindByName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ProteinDataRecord extends RootModel {

	@ManyToOne(optional = false)
	@JoinColumn(name = "organism_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinDataRecord_organism"), nullable = false)
	private Organism organism;

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinDataRecord_protein"), nullable = false)
	private Protein protein;

	@CsvBindByName(column = "#node1")
	private String node1;

	private String node2;
	
	@CsvBindByName(column = "node1_string_internal_id")
	private String node1StringInternalId;
	
	@CsvBindByName(column = "node2_string_internal_id")
	private String node2StringInternalId;
	
	@CsvBindByName(column = "node1_external_id")
	private String node1ExternalId;
	
	@CsvBindByName(column = "node2_external_id")
	private String node2ExternalId;
	
	@CsvBindByName(column = "neighborhood_on_chromosome")
	private String neighborhoodOnChromosome;
	
	@CsvBindByName(column = "gene_fusion")
	private String geneFusion;
	
	@CsvBindByName(column = "phylogenetic_cooccurrence")
	private String phylogeneticCooccurrence;
	
	@CsvBindByName(column = "homology")
	private String homology;
	
	@CsvBindByName(column = "coexpression")
	private String coexpression;
	
	@CsvBindByName(column = "experimentally_determined_interaction")
	private String experimentallyDeterminedInteraction;
	
	@CsvBindByName(column = "database_annotated")
	private String databaseAnnotated;
	
	@CsvBindByName(column = "automated_textmining")
	private String automatedTextmining;
	
	@CsvBindByName(column = "combined_score")
	private String combinedScore;

}
