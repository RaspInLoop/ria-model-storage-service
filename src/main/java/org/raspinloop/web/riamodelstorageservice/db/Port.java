package org.raspinloop.web.riamodelstorageservice.db;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Entity
@EqualsAndHashCode(exclude="group")
public class Port {

	private @Id @GeneratedValue Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "portgroup_id")
	private PortGroup group;
	
	String portId;
	
	int x;
	
	int y;
	
	@Lob
	String description;

	public Port() {
	}

}
