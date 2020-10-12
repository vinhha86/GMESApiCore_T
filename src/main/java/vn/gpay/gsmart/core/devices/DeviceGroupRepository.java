package vn.gpay.gsmart.core.devices;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeviceGroupRepository extends JpaRepository<DeviceGroup, Long>,JpaSpecificationExecutor<DeviceGroup> {
	@Query(value = "delete from DeviceGroup c where c.id = :id")
	public void deleteDeviceGroupById(@Param ("id")final long id);
	
	@Query(value = "delete from DeviceGroup c where c.parentid_link = :parentid_link")
	public void deleteDeviceGroupByParentId(@Param ("parentid_link")final long parentid_link);
	
	@Query(value = "select c from DeviceGroup c where c.parentid_link =:parentid_link ")
	public List<DeviceGroup> findByParentId(@Param ("parentid_link")final long parentid_link);
	
	@Query(value = "select c from DeviceGroup c where c.parentid_link = -1")
	public List<DeviceGroup> findAllParent();
}