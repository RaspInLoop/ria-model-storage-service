package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.raspinloop.web.riamodelstorageservice.db.PackageRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PackageStorageService {


	private final PackageRepository repo;

	private final ComponentRepository compRepo;	
	
	public Package getPackage(String packageId) {
		return repo.findByPackageId(packageId);		
	}

	public boolean deletePackage(String packageId) {
		return repo.deleteByPackageId(packageId) != 0;		
	}

	public Package updatePackage(Package packArg) {
		Package pack = repo.findByPackageId(packArg.getPackageId());	
		if (pack != null) {
			pack.setId(pack.getId());
			if (packArg.getDescription() != null)
				pack.setDescription(packArg.getDescription());
			if (packArg.getSvgIcon() != null)
				pack.setSvgIcon(packArg.getSvgIcon());
			if (packArg.getComponents() != null)
				pack.setComponents(packArg.getComponents());
			if (packArg.getPackageId() != null)
				pack.setPackageId(packArg.getPackageId());
			if (packArg.getPackages() != null)
				pack.setPackages(packArg.getPackages());
			if (packArg.getParent() != null)
				pack.setParent(packArg.getParent());			
			return repo.save(pack);
		}
		else {
			return repo.save(packArg);
		}		
	}


	public Iterable<org.raspinloop.web.riamodelstorageservice.db.Package> getPackages() {
		return repo.findAll();
	}

	public Package linkComponent(Package stored, String compId) {
		Component comp = compRepo.findBycomponentId(compId);
		if (comp != null) {
			stored.getComponents().add(comp);
			comp.setPackageOwner(stored);
		}
		return stored;		
	}

	public Package linkPackage(Package stored, Package updatePackage) {
		stored.getPackages().add(updatePackage);
		updatePackage.setParent(stored);
		return stored;
	}
}
