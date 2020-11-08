package vn.gpay.gsmart.core.deviceout;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface DeviceOutRepository extends JpaRepository<DeviceOut, Long>,JpaSpecificationExecutor<DeviceOut>{
	@Query(value = "select c from DeviceOut c where c.orgrootid_link=:orgrootid_link"
			+ " and c.deviceout_code =:deviceout_code")
	public List<DeviceOut> getByCode(@Param ("orgrootid_link")final Long orgrootid_link ,
			@Param ("deviceout_code")final String deviceout_code);
	
	@Query(value = "select c from DeviceOut c where c.orgrootid_link=:orgrootid_link"
			+ " and c.orgid_from_link =:orgid_from_link"
			+ " and c.status = :status")
	public List<DeviceOut> getByOrgFromAndStatus(@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("orgid_from_link")final Long orgid_from_link,
			@Param ("status")final Integer status);

	@Query(value = "select c from DeviceOut c where c.orgrootid_link=:orgrootid_link"
			+ " and c.orgid_to_link =:orgid_to_link"
			+ " and c.status = :status")
	public List<DeviceOut> getByOrgToAndStatus(@Param ("orgrootid_link")final Long orgrootid_link,
			@Param ("orgid_to_link")final Long orgid_to_link,
			@Param ("status")final Integer status);
	
}
