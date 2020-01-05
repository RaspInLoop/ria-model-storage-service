package org.raspinloop.web.riamodelstorageservice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.raspinloop.web.riamodelstorageservice.db.PackageRepository;
import org.raspinloop.web.riamodelstorageservice.db.PortGroupRepository;
import org.raspinloop.web.riamodelstorageservice.db.PortRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ComponentStorageService {

	private final ComponentRepository componentRepo;
	private final PackageRepository packageRepo;
	private final PortGroupRepository portGroupRepo;
	private final PortRepository portRepo;

	public Component getComponent(String componentId) {
		return componentRepo.findBycomponentId(componentId);
	}

	public boolean deleteComponent(String componentId) {
		return componentRepo.deleteByComponentId(componentId) != 0;
	}

	public Component updateComponent(Component component) {
		Component comp = componentRepo.findBycomponentId(component.getComponentId());
		if (comp != null) {
			Long id = comp.getId();			
			comp = copyIfNotNull(component);
			comp.setId(id);
		} else {
			comp = component;
		}		
		// save/update packageOwner
		String[] packageTokens = comp.getComponentId().split("\\.");		
		String packageId = Arrays.asList(packageTokens)
				.subList(0, packageTokens.length-1)
				.stream()
				.collect(Collectors.joining("."));
		
		Package packageOwner = packageRepo.findByPackageId(packageId);
		if (packageOwner != null) {
			comp.setPackageOwner(packageOwner);
		} // if not valid owner, component will be stored orphan.
			
		// save/update portGroup/ports
		if (comp.getPortGroups() != null) {
			comp.getPortGroups().forEach(pg -> {
				portGroupRepo.save(pg);
				pg.getPorts().forEach( p -> {
					p.setGroup(pg);
					portRepo.save(p);
				});
				
			});
		}
		return componentRepo.save(comp);
	}

	private Component copyIfNotNull(Component component) {
		Component comp = new Component();
		comp.setComponentId(component.getComponentId()); //Mandatory
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
		return componentRepo.findAll();
	}
}
