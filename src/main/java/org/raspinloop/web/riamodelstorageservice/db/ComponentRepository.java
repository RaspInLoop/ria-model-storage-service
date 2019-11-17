package org.raspinloop.web.riamodelstorageservice.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  ComponentRepository  extends CrudRepository<Component, Long> {
	public Component findBycomponentId(String componentId);
	public Long deleteByComponentId(String componentId);
}
