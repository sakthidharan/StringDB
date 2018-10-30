package com.sakthi.stringdb.model;

import javax.persistence.Entity;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Protein extends RootModel {

	private String name;
}
