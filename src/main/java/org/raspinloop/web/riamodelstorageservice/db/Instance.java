package org.raspinloop.web.riamodelstorageservice.db;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Data;

@Data
@Entity
public class Instance {

	public Instance() {
	}
	private @Id @GeneratedValue Long id;
	
	private String instanceName;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "component_id", nullable = false)
	private Component componentRef;
	
	@Lob
	private String parameters;
	
}
