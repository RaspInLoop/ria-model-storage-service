package org.raspinloop.web.riamodelstorageservice.db;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.raspinloop.server.model.IPortGroup;

import lombok.Data;

@Data
@Entity
public class Component {

	public Component() {
	}

	private @Id @GeneratedValue Long id;
	private String componentId;
	private String description;
	private String svgContent;
	private String svgIcon;
	private String htmlDocumentation;
	private String parameters;
	private Set<IPortGroup> portGroups;
}
