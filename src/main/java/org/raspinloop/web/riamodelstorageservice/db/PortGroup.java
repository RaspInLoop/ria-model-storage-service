package org.raspinloop.web.riamodelstorageservice.db;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import lombok.Data;

@Data
@Entity
public class PortGroup {

	@Id
	@GeneratedValue
	private Long id;
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "group")
	private Set<Port> ports;
	
	@ManyToMany(mappedBy = "portGroups")
	private Set<Component> usedBy;
	
	private String Name;
	
	@Lob
	private String svg;	
	
	public PortGroup() {
	}

}
