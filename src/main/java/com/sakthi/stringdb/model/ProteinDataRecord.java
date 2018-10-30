package com.sakthi.stringdb.model;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.opencsv.bean.CsvBindByName;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class ProteinDataRecord extends RootModel {

	@ManyToOne(optional = false)
	@JoinColumn(name = "protein_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_proteinDataRecord_protein"), nullable = false)
	private Protein protein;

	@CsvBindByName(column = "#node1")
	private String node1;

	private String node2;
	private String node1_string_internal_id;
	private String node2_string_internal_id;
	private String node1_external_id;
	private String node2_external_id;
	private String neighborhood_on_chromosome;
	private String gene_fusion;
	private String phylogenetic_cooccurrence;
	private String homology;
	private String coexpression;
	private String experimentally_determined_interaction;
	private String database_annotated;
	private String automated_textmining;
	private String combined_score;

}
