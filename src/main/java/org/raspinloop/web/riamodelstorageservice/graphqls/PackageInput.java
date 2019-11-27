package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import lombok.Data;

@Data
public class PackageInput {
	
	private Long id;

	private String packageId;

	private String description;	
	
	private String svgIcon;	
	
	private Set<PackageInput> packages;
	
	private Set<String> componentIds;
}
