package org.raspinloop.web.riamodelstorageservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
public class ServiceTestConfiguration {
	
	@Bean
	ComponentStorageService componentService() {
		return new ComponentStorageService();
	}
	
	@Bean
	InstanceStorageService instanceService() {
		return new InstanceStorageService();
	}
	
	@Bean
	PackageStorageService packageService() {
		return new PackageStorageService();
	}
}
