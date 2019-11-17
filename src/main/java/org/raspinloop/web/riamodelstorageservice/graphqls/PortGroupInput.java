package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import lombok.Data;

@Data
public class PortGroupInput {
	
	private Set<PortInput> ports;
	
	private String Name;

	private String svg;	

}
