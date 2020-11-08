package vn.gpay.gsmart.core.deviceout_d;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeviceOut_dRepository extends JpaRepository<DeviceOut_d, Long>,JpaSpecificationExecutor<DeviceOut_d>{
	@Query(value = "select c from DeviceOut_d c where c.orgrootid_link=:orgrootid_link"
			+ " and c.deviceoutid_link =:deviceoutid_link")
	public List<DeviceOut_d> getByDeviceOut(@Param ("orgrootid_link")final Long orgrootid_link ,
			@Param ("deviceoutid_link")final Long deviceoutid_link);
	
}
