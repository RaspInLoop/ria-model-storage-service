package org.raspinloop.web.riamodelstorageservice;

import java.util.Optional;

import org.raspinloop.web.riamodelstorageservice.db.Component;
import org.raspinloop.web.riamodelstorageservice.db.ComponentRepository;
import org.raspinloop.web.riamodelstorageservice.db.Instance;
import org.raspinloop.web.riamodelstorageservice.db.InstanceRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class InstanceStorageService {

	
	private final ComponentRepository componentRepo;
	
	private final InstanceRepository instanceRepo;

	public Long createInstance(String componentId, String name) {
		Component comp = componentRepo.findBycomponentId(componentId);
		if (comp != null) {
			Instance inst = new Instance();
			inst.setComponentRef(comp);
			inst.setInstanceName(name);
			inst = instanceRepo.save(inst);
			return inst.getId();

		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, componentId + " Not Found");
		}
	}

	public Instance updateInstance(long instId, String name, String componentId, String instParameters) {
		Component comp = componentRepo.findBycomponentId(componentId);
		if (comp == null) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, componentId + " Not Found");
				
		Optional<Instance> isntOpt = instanceRepo.findById(instId);
		if (! isntOpt.isPresent()) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instance with Id {"+ instId + "} Not Found");
		
		Instance instToUpdate = isntOpt.get();
		instToUpdate.setInstanceName(name);
		instToUpdate.setComponentRef(comp);
		instToUpdate.setParameters(instParameters);
		return instanceRepo.save(instToUpdate);			
	}

	public Instance getInstance(Long instanceId) {
		Optional<Instance> isntOpt = instanceRepo.findById(instanceId);
		if (! isntOpt.isPresent()) 
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Instance with Id {"+ instanceId + "} Not Found");
		
		return isntOpt.get();
	}

	public boolean deleteInstance(Long id) {
		instanceRepo.deleteById(id);
		return true; // how to know if delete fail ?
	}

}
