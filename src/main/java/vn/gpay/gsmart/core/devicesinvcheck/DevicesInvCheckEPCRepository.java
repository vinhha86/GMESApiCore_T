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
public interface DevicesInvCheckEPCRepository  extends JpaRepository<DevicesInvCheckEPC, Long>,JpaSpecificationExecutor<DevicesInvCheckEPC>{

	@Query(value = "select c from DevicesInvCheckEPC c where c.devices_invcheckid_link =:devices_invcheckid_link")
	public List<DevicesInvCheckEPC> findByDevicesInvCheckId(@Param ("devices_invcheckid_link")final Long devices_invcheckid_link);
}
