package vn.gpay.gsmart.core.deviceout_d;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class DeviceOut_dService extends AbstractService<DeviceOut_d> implements IDeviceOut_dService{

	@Autowired
	DeviceOut_dRepository repo; 
	
	@Override
	protected JpaRepository<DeviceOut_d, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<DeviceOut_d> getByDeviceOut(Long orgrootid_link, Long deviceoutid_link){
		return repo.getByDeviceOut(orgrootid_link,deviceoutid_link);
	}
}
