package vn.gpay.gsmart.core.deviceout_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DeviceOutTypeService extends AbstractService<DeviceOutType> implements IDeviceOutTypeService{
	@Autowired IDeviceOutTypeRepository repo;
	@Override
	protected JpaRepository<DeviceOutType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
