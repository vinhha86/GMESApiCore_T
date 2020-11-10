package vn.gpay.gsmart.core.devicesinvcheck;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DevicesInvCheckEPCService  extends AbstractService<DevicesInvCheckEPC> implements IDevicesInvCheckEPCService{

	@Autowired DevicesInvCheckEPCRepository repository;
	
	@Override
	protected JpaRepository<DevicesInvCheckEPC, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<DevicesInvCheckEPC> findByDevicesInvCheckId(Long devices_invcheckid_link) {
		// TODO Auto-generated method stub
		return repository.findByDevicesInvCheckId(devices_invcheckid_link);
	}

}
