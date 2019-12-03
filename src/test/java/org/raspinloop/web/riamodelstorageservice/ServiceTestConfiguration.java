package org.raspinloop.web.riamodelstorageservice;


import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.InstanceRepository;
import org.raspinloop.web.riamodelstorageservice.db.PackageRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceTestConfiguration {
	
	@Bean
	ComponentStorageService componentService(ComponentRepository componentRepo) {
		return new ComponentStorageService(componentRepo);
	}
	
	@Bean
	InstanceStorageService instanceService(ComponentRepository componentRepo, InstanceRepository instanceRepo) {
		return new InstanceStorageService(componentRepo, instanceRepo);
	}
	
	@Bean
	PackageStorageService packageService(PackageRepository packageRepo, ComponentRepository componentRepo) {
		return new PackageStorageService(packageRepo, componentRepo);
	}
}
