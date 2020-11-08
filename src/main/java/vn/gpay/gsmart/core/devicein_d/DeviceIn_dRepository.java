package vn.gpay.gsmart.core.devicein_d;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeviceIn_dRepository extends JpaRepository<DeviceIn_d, Long>,JpaSpecificationExecutor<DeviceIn_d>{
	@Query(value = "select c from DeviceIn_d c where c.orgrootid_link=:orgrootid_link"
			+ " and c.deviceinid_link =:deviceinid_link")
	public List<DeviceIn_d> getByDeviceIn(@Param ("orgrootid_link")final Long orgrootid_link ,
			@Param ("deviceinid_link")final Long deviceinid_link);
	
}
