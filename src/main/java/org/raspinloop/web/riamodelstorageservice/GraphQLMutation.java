package org.raspinloop.web.riamodelstorageservice;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.Instance;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.raspinloop.web.riamodelstorageservice.db.Port;
import org.raspinloop.web.riamodelstorageservice.db.PortGroup;
import org.raspinloop.web.riamodelstorageservice.graphqls.ComponentInput;
import org.raspinloop.web.riamodelstorageservice.graphqls.InstanceInput;
import org.raspinloop.web.riamodelstorageservice.graphqls.PackageInput;
import org.raspinloop.web.riamodelstorageservice.graphqls.PortGroupInput;
import org.springframework.stereotype.Controller;

import com.coxautodev.graphql.tools.GraphQLMutationResolver;

@Controller
public class GraphQLMutation implements GraphQLMutationResolver {

	private ComponentStorageService componentService;

	private PackageStorageService packageService;
	
	private InstanceStorageService instanceStorageService;

	public GraphQLMutation(ComponentStorageService componentService, InstanceStorageService instanceStorageService, PackageStorageService packageStorageService) {
		super();
		this.componentService = componentService;
		this.instanceStorageService = instanceStorageService;
		this.packageService = packageStorageService;
	}

	public boolean deleteComponent(String componentId) {
		return componentService.deleteComponent(componentId);
	}

	public Component updateComponent(ComponentInput component) {
		Component newcomp = new Component();
		newcomp.setId(component.getId());
		newcomp.setComponentId(component.getComponentId());
		newcomp.setDescription(component.getDescription());
		newcomp.setSvgContent(component.getSvgContent());
		newcomp.setSvgIcon(component.getSvgIcon());
		newcomp.setHtmlDocumentation(component.getHtmlDocumentation());
		newcomp.setParameters(component.getParameters());

		if (component.getPortGroups() != null) {
			newcomp.setPortGroups(component.getPortGroups()
					.stream()
					.map(pgi -> {
									PortGroup pg = new PortGroup();
									pg.setName(pgi.getName());
									pg.setPorts(collectPort(pgi));
									pg.setSvg(pgi.getSvg());
									return pg;
								})
					.collect(Collectors.toSet()));
		}
		return componentService.updateComponent(newcomp);
	}

	private Set<Port> collectPort(PortGroupInput pgi) {
		if (pgi.getPorts() != null) {
			return pgi.getPorts()
					.stream()
					.map(pi -> {
								Port p = new Port();
								p.setDescription(pi.getDescription());
								p.setPortId(pi.getPortId());
								p.setX(pi.getX());
								p.setY(pi.getY());
								return p;
							})
					.collect(Collectors.toSet());
		} else {
			return Collections.emptySet();
		}
	}
	
	public boolean deletePackage(String packageId) {
		return packageService.deletePackage(packageId);
	}

	public Package updatePackage(PackageInput packageInput) {		
		Package stored = packageService.updatePackage(convert(packageInput));
		for (String compId : packageInput.getComponentIds()) {
			stored = packageService.linkComponent(stored, compId);
		}		
		return stored;
	}
	
	private Package convert(PackageInput packageInput) {
		Package newpack = new Package();
		newpack.setId(packageInput.getId());
		newpack.setPackageId(packageInput.getPackageId());
		newpack.setDescription(packageInput.getDescription());
		newpack.setSvgIcon(packageInput.getSvgIcon());		
		if (packageInput.getPackages() != null) {
			newpack.setPackages(packageInput.getPackages()
					.stream()
					.map(this::convert)
					.collect(Collectors.toSet()));
		}
		
		return newpack;
	}

	public long createInstance(String name, String componentId) {
		return instanceStorageService.createInstance(componentId, name);
	}

	public boolean deleteInstance(long id) {
		return instanceStorageService.deleteInstance(id);
	}

	public Instance updateInstance(InstanceInput instance) {
		return instanceStorageService.updateInstance(instance.getId(), instance.getName(),
				instance.getComponentId(), instance.getParameters());
	}

}
