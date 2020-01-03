package org.raspinloop.web.riamodelstorageservice.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  PortGroupRepository  extends CrudRepository<PortGroup, Long> {

}
