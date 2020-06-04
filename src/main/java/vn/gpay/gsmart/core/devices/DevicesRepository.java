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

	@Query(value = "select c from Devices c where orgid_link =:orgid_link and parent_id is null order by id")
	public List<Devices> device_listtree(@Param ("orgid_link")final  Long orgid_link);
	
	@Query(value = "select c from Devices c where org_governid_link =:orgid_link and type=:type")
	public List<Devices> device_govern(@Param ("orgid_link")final  Long orgid_link,@Param ("type")final  int type);
	
	
	@Query(value = "select c from Devices c where c.code =:code and c.status <>3")
	public List<Devices> finByCode(@Param ("code")final  String code);
}
