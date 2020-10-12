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
public interface DevicesRepository extends JpaRepository<Devices, Long>,JpaSpecificationExecutor<Devices>{

	@Query(value = "select c from Devices c where orgrootid_link =:orgrootid_link and parent_id is null order by id")
	public List<Devices> device_listtree(@Param ("orgrootid_link")final  Long orgrootid_link);
	
	@Query(value = "select c from Devices c "
			+ "where c.org_governid_link =:orgid_link "
			+ "and c.type=:type "
			+ "and c.status <> -1")
	public List<Devices> device_govern(@Param ("orgid_link")final  Long orgid_link,@Param ("type")final  Long type);
	
	
	@Query(value = "select c from Devices c where c.code =:code and c.status <>3")
	public List<Devices> finByCode(@Param ("code")final  String code);
	
	@Query(value = "select c from Devices c where c.epc =:epc")
	public List<Devices> finByEPC(@Param ("epc")final  String epc);
	
	@Query(value = "select c from Devices c where c.org_governid_link = :org_governid_link and c.epc =:epc")
	public List<Devices> finByOrgEPC(@Param ("org_governid_link")final long org_governid_link, @Param ("epc")final  String epc);
	
	@Query(value = "select c from Devices c where c.org_governid_link =:org_governid_link "
			+ "and c.disable is false")
	public List<Devices> findByOrg(@Param ("org_governid_link")final long org_governid_link);
	
	@Query(value = "select c from Devices c where c.devicegroupid_link =:devicegroupid_link")
	public List<Devices> findByDeviceGroup(@Param ("devicegroupid_link")final  long devicegroupid_link);
}
