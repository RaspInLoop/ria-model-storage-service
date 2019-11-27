package org.raspinloop.web.riamodelstorageservice;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.Package;
import org.raspinloop.web.riamodelstorageservice.db.PackageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PackageStorageService {

	@Autowired
	private PackageRepository repo;
	
	@Autowired
	private ComponentRepository compRepo;
	
	public Package getPackage(String packageId) {
		return repo.findByPackageId(packageId);		
	}

	public boolean deletePackage(String componentId) {
		return repo.deleteByPackageId(componentId) != 0;		
	}

	public Package updatePackage(Package packArg) {
		Package pack = repo.findByPackageId(packArg.getPackageId());	
		if (pack != null) {
			Long id = pack.getId();
			pack = packArg; // TODO update only if not null
			pack.setId(id);
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
}
