package org.raspinloop.web.riamodelstorageservice.db;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude="parent")
@Entity
public class Package {

	public Package() {
	}
	
	private @Id @GeneratedValue Long id;

	private String packageId;
	
	@Lob
	private String description;
		
	@Lob
	private String svgIcon;	
	
	@ManyToOne(cascade={CascadeType.ALL})
	@JoinColumn(name="parent_id")
	private Package parent;
	
	@OneToMany(mappedBy="parent")
	private Set<Package> packages = new HashSet<Package>();
	
	@OneToMany(mappedBy="packageOwner")
	private Set<Component> components = new HashSet<Component>();
}
