package org.raspinloop.web.riamodelstorageservice.db;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.raspinloop.web.riamodelstorageservice.db.Package;

@Repository
public interface  PackageRepository  extends CrudRepository<Package, Long> {
	public Package findByPackageId(String packageId);
	public Long deleteByPackageId(String packageId);
}
