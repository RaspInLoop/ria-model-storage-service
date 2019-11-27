package org.raspinloop.web.riamodelstorageservice.graphqls;

import java.util.Set;

import org.raspinloop.web.riamodelstorageservice.PackageStorageService;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.raspinloop.web.riamodelstorageservice.db.Component;

import com.coxautodev.graphql.tools.GraphQLResolver;

@org.springframework.stereotype.Component
public class PackageResolver implements GraphQLResolver<Package> {
		
	PackageStorageService service;
	
    public PackageResolver(PackageStorageService service) {
		this.service = service;
	}

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
