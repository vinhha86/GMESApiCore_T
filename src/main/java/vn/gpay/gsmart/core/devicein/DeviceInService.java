package vn.gpay.gsmart.core.devicein;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Sorts;
import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class DeviceInService extends AbstractService<DeviceIn> implements IDeviceInService{

	@Autowired
	DeviceInRepository repo; 
	
	@Override
	protected JpaRepository<DeviceIn, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<DeviceIn> getByCode(Long orgrootid_link, String devicein_code){
		return repo.getByCode(orgrootid_link,devicein_code);
	}
	
	@Override
	public List<DeviceIn> getByOrgFromAndStatus(Long orgrootid_link,
			Long orgid_from_link,
			Integer status){
		return repo.getByOrgFromAndStatus(orgrootid_link,orgid_from_link,status);
	}
	
	@Override
	public List<DeviceIn> getByOrgToAndStatus(Long orgrootid_link,
			Long orgid_to_link,
			Integer status){
		return repo.getByOrgToAndStatus(orgrootid_link,orgid_to_link,status);
	}
	
	@Override
	public List<DeviceIn> getByDeviceOutAndStatus(Long orgrootid_link,
			Long deviceoutid_link,
			Integer status){
		return repo.getByDeviceOutAndStatus(orgrootid_link,deviceoutid_link,status);
	}
	
	@Override
	public List<DeviceIn> filterAll(Long orgrootid_link,
			String devicein_code,
			Date devicein_date_from,
			Date devicein_date_to,
			Integer deviceintypeid_link,
			String invoice_code,
			Long deviceoutid_link,
			Long orgid_from_link,
			Long orgid_to_link,
			Integer usercreateid_link){
		Specification<DeviceIn> specification = Specifications.<DeviceIn>and()
//	            .eq( "orgrootid_link", orgrootid_link)
	            .eq(Objects.nonNull(devicein_code), "devicein_code", devicein_code)
	            .eq(Objects.nonNull(deviceintypeid_link), "deviceintypeid_link", deviceintypeid_link)
	            .eq(Objects.nonNull(invoice_code), "invoice_code", invoice_code)
	            .eq(Objects.nonNull(deviceoutid_link), "deviceoutid_link", deviceoutid_link)
	            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
	            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
	            .eq(Objects.nonNull(usercreateid_link), "usercreateid_link", usercreateid_link)
	            .ge((devicein_date_from!=null && devicein_date_to==null),"devicein_date",GPAYDateFormat.atStartOfDay(devicein_date_from))
                .le((devicein_date_from==null && devicein_date_to!=null),"devicein_date",GPAYDateFormat.atEndOfDay(devicein_date_to))
                .between((devicein_date_from!=null && devicein_date_to!=null),"devicein_date", GPAYDateFormat.atStartOfDay(devicein_date_from), GPAYDateFormat.atEndOfDay(devicein_date_to))
                .ne("status", -1)
	            .build();
		Sort sort = Sorts.builder()
//		        .desc("devicein_date")
		        .build();
	    return repo.findAll(specification,sort);
	}
}
