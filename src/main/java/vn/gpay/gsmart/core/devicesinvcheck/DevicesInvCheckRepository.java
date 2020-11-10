package vn.gpay.gsmart.core.devicesinvcheck;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional
public interface DevicesInvCheckRepository  extends JpaRepository<DevicesInvCheck, Long>,JpaSpecificationExecutor<DevicesInvCheck>{

	@Query(value = "select c from DevicesInvCheck c where c.orgcheckid_link =:orgcheckid_link")
	public List<DevicesInvCheck> findByOrgCheckId(@Param ("orgcheckid_link")final Long orgcheckid_link);
	
	@Query(value = "select c from DevicesInvCheck c where c.orgcheckid_link  = :orgcheckid_link and c.status = 0")
	public List<DevicesInvCheck> findByOrgCheckIdActive(@Param ("orgcheckid_link")final Long orgcheckid_link);
}
