package vn.gpay.gsmart.core.devices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DeviceGroupServiceImpl extends AbstractService<DeviceGroup> implements IDeviceGroupService{
	
	@Autowired DeviceGroupRepository repo;

	@Override
	protected JpaRepository<DeviceGroup, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<DeviceGroup> findAllByOrderByIdAsc() {
		// TODO Auto-generated method stub
		return repo.findAllByOrderByIdAsc();
	}

}
