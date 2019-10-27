package org.raspinloop.web.riamodelstorageservice.db;

import java.util.Set;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.raspinloop.server.model.IPort;
import org.raspinloop.server.model.IPortGroupDefinition;

import lombok.Data;

@Data
public class PortGroup {

	private @Id @GeneratedValue Long id;
	private Set<IPort> ports;
	private IPortGroupDefinition definition;
	private String Name;
	private String svg;

	public PortGroup() {
	}

}
