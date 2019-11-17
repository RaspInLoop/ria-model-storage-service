package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import lombok.Data;

@Data
public class ComponentInput {
	
	private Long id;

	private String componentId;

	private String description;
	
	private String svgContent;
	
	private String svgIcon;
	
	private String htmlDocumentation;
	
	private String parameters;
	
	private Set<PortGroupInput> portGroups;
}
