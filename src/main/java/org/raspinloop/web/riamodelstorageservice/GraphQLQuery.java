package org.raspinloop.web.riamodelstorageservice;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Instance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.coxautodev.graphql.tools.GraphQLQueryResolver;

@Controller
public class GraphQLQuery implements GraphQLQueryResolver {

	@Autowired
	ComponentStorageService componentService;

	@Autowired
	InstanceStorageService instanceService;

	public Collection<Component> getComponents() {
		Set<Component> entities = new HashSet<>();
		componentService.getComponents().forEach(entities::add);
		return entities;
	}
	
	public Component getComponent(String componentId) {
		Component entity = componentService.getComponent(componentId);
		return entity;
	}

	public Instance getInstance(Long id) {
		Instance instance = instanceService.getInstance(id);		
		return instance;
	}
}