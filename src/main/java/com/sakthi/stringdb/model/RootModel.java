package com.sakthi.stringdb.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class RootModel {
	
	@Id
	@GeneratedValue
	@Column(updatable=false, nullable=false)
	private Long id;

}
