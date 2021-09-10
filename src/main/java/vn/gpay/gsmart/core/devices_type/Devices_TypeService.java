package vn.gpay.gsmart.core.devices_type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Devices_TypeService extends AbstractService<Devices_Type> implements IDevices_TypeService{
	@Autowired IDevices_TypeRepository repo;

	@Override
	protected JpaRepository<Devices_Type, Long> getRepository() {
		return repo;
	}

	@Override
	public List<Devices_Type> loadDivicesType() {
		return repo.loadDivicesType();
	}

	@Override
	public List<Devices_Type> loadDevicesTypeByName(String name) {
		return repo.loadDevicesTypeByName(name);
	}

	@Override
	public List<Devices_Type> loadDevicesTypeByCode(String code) {
		return repo.loadDevicesTypeByCode(code);
	}
	
}
