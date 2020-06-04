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
	public List<Devices> device_listtree(Long orgid_link,Long org_governid_link,String search) {
		// TODO Auto-generated method stub
		
		Specification<Devices> specification = Specifications.<Devices>and()
	            .eq( "orgid_link", orgid_link)
	            .eq(org_governid_link!=0, "org_governid_link", org_governid_link)
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
	public List<Devices> device_govern(Long orgid_link,int type) {
		// TODO Auto-generated method stub
		return repository.device_govern(orgid_link,type);
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
}
