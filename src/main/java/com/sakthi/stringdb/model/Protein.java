package com.sakthi.stringdb.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(uniqueConstraints=@UniqueConstraint(name="unique_protein_name",columnNames= {"name"}))
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class Protein extends RootModel {

	private String name;
}
