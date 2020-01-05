package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.PortGroupRepository;
import org.raspinloop.web.riamodelstorageservice.db.PortRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComponentStorageService {

	private final ComponentRepository repo;
	private final PortGroupRepository pgRepo;
	private final PortRepository prepo;

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
			comp = copyIfNotNull(component);
			comp.setId(id);
		} else {
			comp = component;
		}
		// save/update portGroup/ports
		if (comp.getPortGroups() != null) {
			comp.getPortGroups().forEach(pg -> {
				pgRepo.save(pg);
				pg.getPorts().forEach( p -> {
					p.setGroup(pg);
					prepo.save(p);
				});
				
			});
		}
		return repo.save(comp);
	}

	private Component copyIfNotNull(Component component) {
		Component comp = new Component();
		if (component.getDescription() != null) {
			comp.setDescription(component.getDescription());
		}
		if (component.getSvgContent() != null) {
			comp.setSvgContent(component.getSvgContent());
		}
		if (component.getSvgIcon() != null) {
			comp.setSvgIcon(component.getSvgIcon());
		}
		if (component.getHtmlDocumentation() != null) {
			comp.setHtmlDocumentation(component.getHtmlDocumentation());
		}
		if (component.getParameters() != null) {
			comp.setParameters(component.getParameters());
		}
		comp.setPortGroups(component.getPortGroups());
		return comp;
	}

	public Iterable<Component> getComponents() {
		return repo.findAll();
	}
}
