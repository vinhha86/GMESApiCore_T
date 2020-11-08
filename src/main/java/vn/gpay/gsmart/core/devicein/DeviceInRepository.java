package vn.gpay.gsmart.core.devicein;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeviceInRepository extends JpaRepository<DeviceIn, Long>,JpaSpecificationExecutor<DeviceIn>{
	@Query(value = "select c from DeviceIn c where c.orgrootid_link=:orgrootid_link"
			+ " and c.devicein_code =:devicein_code")
	public List<DeviceIn> getByCode(@Param ("orgrootid_link")final Long orgrootid_link ,
			@Param ("devicein_code")final String devicein_code);
	
	@Query(value = "select c from DeviceIn c where c.orgrootid_link=:orgrootid_link"
			+ " and c.orgid_from_link =:orgid_from_link"
			+ " and c.status = :status")
	public List<DeviceIn> getByOrgFromAndStatus(@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("orgid_from_link")final Long orgid_from_link,
			@Param ("status")final Integer status);

	@Query(value = "select c from DeviceIn c where c.orgrootid_link=:orgrootid_link"
			+ " and c.orgid_to_link =:orgid_to_link"
			+ " and c.status = :status")
	public List<DeviceIn> getByOrgToAndStatus(@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("orgid_to_link")final Long orgid_to_link,
			@Param ("status")final Integer status);
	
	@Query(value = "select c from DeviceIn c where c.orgrootid_link=:orgrootid_link"
			+ " and c.deviceoutid_link =:deviceoutid_link"
			+ " and c.status = :status")
	public List<DeviceIn> getByDeviceOutAndStatus(@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("deviceoutid_link")final Long deviceoutid_link,
			@Param ("status")final Integer status);
}
