package vn.gpay.gsmart.core.devicein_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.devicein_type.DeviceInType;
import vn.gpay.gsmart.core.devicein_type.IDeviceInTypeService;
import vn.gpay.gsmart.core.devicein_type.IDeviceInTypeRepository;

@Service
public class DeviceInTypeService extends AbstractService<DeviceInType> implements IDeviceInTypeService{
	@Autowired IDeviceInTypeRepository repo;
	@Override
	protected JpaRepository<DeviceInType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
}
