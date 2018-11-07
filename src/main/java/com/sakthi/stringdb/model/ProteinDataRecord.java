package com.sakthi.stringdb.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.opencsv.bean.CsvBindByName;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(name = "unique_protein_data_record", columnNames = { "organism_protein_id",
		"automated_textmining", "coexpression", "combined_score", "database_annotated",
		"experimentally_determined_interaction", "gene_fusion", "homology", "neighborhood_on_chromosome", "node1",
		"node1external_id", "node1string_internal_id", "node2", "node2external_id", "node2string_internal_id",
		"phylogenetic_cooccurrence" }))
@Getter
@Setter
@ToString(includeFieldNames = true)
@EqualsAndHashCode(callSuper = true)
public class ProteinDataRecord extends RootModel {

	@ManyToOne(optional = false)
	@JoinColumn(name = "organism_protein_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinDataRecord_organismProtein"), nullable = false)
	private OrganismProtein organismProtein;

	@CsvBindByName(column = "#node1")
	private String node1;

	@CsvBindByName(column = "node2")
	private String node2;

	@Column(name = "node1string_internal_id")
	@CsvBindByName(column = "node1_string_internal_id")
	private String node1StringInternalId;

	@Column(name = "node2string_internal_id")
	@CsvBindByName(column = "node2_string_internal_id")
	private String node2StringInternalId;

	@Column(name = "node1external_id")
	@CsvBindByName(column = "node1_external_id")
	private String node1ExternalId;

	@Column(name = "node2external_id")
	@CsvBindByName(column = "node2_external_id")
	private String node2ExternalId;

	@Column(name = "neighborhood_on_chromosome")
	@CsvBindByName(column = "neighborhood_on_chromosome")
	private String neighborhoodOnChromosome;

	@Column(name = "gene_fusion")
	@CsvBindByName(column = "gene_fusion")
	private String geneFusion;

	@Column(name = "phylogenetic_cooccurrence")
	@CsvBindByName(column = "phylogenetic_cooccurrence")
	private String phylogeneticCooccurrence;

	@Column(name = "homology")
	@CsvBindByName(column = "homology")
	private String homology;

	@Column(name = "coexpression")
	@CsvBindByName(column = "coexpression")
	private String coexpression;

	@Column(name = "experimentally_determined_interaction")
	@CsvBindByName(column = "experimentally_determined_interaction")
	private String experimentallyDeterminedInteraction;

	@Column(name = "database_annotated")
	@CsvBindByName(column = "database_annotated")
	private String databaseAnnotated;

	@Column(name = "automated_textmining")
	@CsvBindByName(column = "automated_textmining")
	private String automatedTextmining;

	@Column(name = "combined_score")
	@CsvBindByName(column = "combined_score")
	private String combinedScore;

}
