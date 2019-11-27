package org.raspinloop.web.riamodelstorageservice.db;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude="packageOwner")
@Entity
public class Component {

	public Component() {
	}
	private @Id @GeneratedValue Long id;
	private String componentId;
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="package_id")
	private Package packageOwner;
	
	@Lob
	private String description;
	
	@Lob
	private String svgContent;
	
	@Lob
	private String svgIcon;
	
	@Lob
	private String htmlDocumentation;
	
	@Lob
	private String parameters;
	
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(
			  name = "port_group_usage", 
			  joinColumns = @JoinColumn(name = "component_id"), 
			  inverseJoinColumns = @JoinColumn(name = "port_group_id"))
	private Set<PortGroup> portGroups;
}
