package com.sakthi.stringdb.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
@EqualsAndHashCode
public class RootModel {
	
	@Id
	@GeneratedValue
	@Column(updatable=false, nullable=false)
	private Long id;

}
