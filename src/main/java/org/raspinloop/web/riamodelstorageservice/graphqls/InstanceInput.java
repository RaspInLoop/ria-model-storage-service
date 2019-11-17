package org.raspinloop.web.riamodelstorageservice.graphqls;

import lombok.Data;

@Data
public class InstanceInput {

	private Long id;
	
	private String name;
	
	private String componentId;

	private String parameters;
	
}
