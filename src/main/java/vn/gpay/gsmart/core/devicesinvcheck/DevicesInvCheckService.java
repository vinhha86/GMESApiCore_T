package vn.gpay.gsmart.core.devicesinvcheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DevicesInvCheckService  extends AbstractService<DevicesInvCheck> implements IDevicesInvCheckService{

	@Autowired DevicesInvCheckRepository repository;
	
	@Override
	protected JpaRepository<DevicesInvCheck, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<DevicesInvCheck> findByOrgCheckId(Long orgcheckid_link) {
		// TODO Auto-generated method stub
		return repository.findByOrgCheckId(orgcheckid_link);
	}

	@Override
	public List<DevicesInvCheck> findByOrgCheckIdActive(Long orgcheckid_link) {
		// TODO Auto-generated method stub
		return repository.findByOrgCheckIdActive(orgcheckid_link);
	}

}
