package vn.gpay.gsmart.core.devices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
@Service
public class DevicesServiceImpl extends AbstractService<Devices> implements IDevicesService{

	@Autowired
	DevicesRepository repository; 
	@Override
	protected JpaRepository<Devices, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<Devices> device_list(Long orgrootid_link,Long org_governid_link,String search, boolean disable) {
		// TODO Auto-generated method stub
		
		Specification<Devices> specification = Specifications.<Devices>and()
	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(org_governid_link!=0, "org_governid_link", org_governid_link)
	            .eq("disable", disable)
	            .ne("status", -1)
	            .predicate(Specifications.or()
	            		.like( "code", "%"+search+"%")
	            		.like("name", "%"+search+"%")
	            		.build())
	            .build();
		Sort sort = Sorts.builder()
		        .desc("id")
		        .build();
	    return repository.findAll(specification,sort);
	}
	@Override
	public List<Devices> device_govern(Long orgid_link,Long type) {
		// TODO Auto-generated method stub
		try {
			List<Devices> a= repository.device_govern(orgid_link, type);
		return a;
		} catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
	@Override
	public Devices finByCode(String code) {
		// TODO Auto-generated method stub
		List<Devices>  list = repository.finByCode(code);
		if(list!=null && list.size()>0) {
			return list.get(0);
		}
		return null;
	}
	
	@Override
	public Devices finByEPC(String epc) {
		List<Devices>  list = repository.finByEPC(epc);
		if(list!=null && list.size()>0) {
			return list.get(0);
		} else
			return null;
	}
	
	@Override
	public Devices finByOrgEPC(Long org_governid_link, String epc) {
		List<Devices>  list = repository.finByOrgEPC(org_governid_link, epc);
		if(list!=null && list.size()>0) {
			return list.get(0);
		} else
			return null;
	}
	
	@Override
	public List<Devices> findByOrg(long org_governid_link) {
		return repository.findByOrg(org_governid_link);
	}
	@Override
	public List<Devices> findByDeviceGroup(long devicegroupid_link) {
		return repository.findByDeviceGroup(devicegroupid_link);
	}
}
