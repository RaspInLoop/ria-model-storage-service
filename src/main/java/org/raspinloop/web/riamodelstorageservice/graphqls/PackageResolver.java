package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Package;

import com.coxautodev.graphql.tools.GraphQLResolver;

import lombok.RequiredArgsConstructor;

@org.springframework.stereotype.Component
@RequiredArgsConstructor
public class PackageResolver implements GraphQLResolver<Package> {

	public long getId(Package pack) {
    	return pack.getId();
    }
    
    public String getPackageId(Package pack) {
    	return pack.getPackageId();
    }
    
    public String getDescription(Package pack) {
    	return pack.getDescription();
    }
    
    public String getSvgIcon(Package pack) {
    	return pack.getSvgIcon();
    }

	public Set<Package> getPackages(Package pack) {
		return pack.getPackages();		        
    }
	
	public Set<Component> getComponents(Package pack) {
        return pack.getComponents();
    }
}
