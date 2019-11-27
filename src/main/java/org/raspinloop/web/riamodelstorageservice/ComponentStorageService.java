package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ComponentStorageService {

	@Autowired
	private ComponentRepository repo;
	
	public Component getComponent(String componentId) {
		return repo.findBycomponentId(componentId);		
	}

	public boolean deleteComponent(String componentId) {
		return repo.deleteByComponentId(componentId) != 0;		
	}

	public Component updateComponent(Component component) {
		Component comp = repo.findBycomponentId(component.getComponentId());	
		if (comp != null) {
			Long id = comp.getId();
			comp = component; // TODO update only if not null
			comp.setId(id);
			return repo.save(comp);
		}
		else {
			return repo.save(component);
		}		
	}

	public Iterable<Component> getComponents() {
		return repo.findAll();
	}
}
