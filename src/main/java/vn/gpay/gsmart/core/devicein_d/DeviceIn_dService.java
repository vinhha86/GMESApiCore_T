package vn.gpay.gsmart.core.devicein_d;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DeviceIn_dService extends AbstractService<DeviceIn_d> implements IDeviceIn_dService{

	@Autowired
	DeviceIn_dRepository repo; 
	
	@Override
	protected JpaRepository<DeviceIn_d, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<DeviceIn_d> getByDeviceIn(Long orgrootid_link, Long deviceinid_link){
		return repo.getByDeviceIn(orgrootid_link,deviceinid_link);
	}
}
