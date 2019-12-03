package org.raspinloop.web.riamodelstorageservice.db;

import java.util.Set;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository       
public interface  PackageRepository  extends CrudRepository<Package, Long> {
	public Package findByPackageId(String packageId);
	public Long deleteByPackageId(String packageId);
	public Set<Package> findByParentId(Long id);
}
