package vn.gpay.gsmart.core.deviceout;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class DeviceOutService extends AbstractService<DeviceOut> implements IDeviceOutService{

	@Autowired
	DeviceOutRepository repo; 
	
	@Override
	protected JpaRepository<DeviceOut, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<DeviceOut> getByCode(Long orgrootid_link, String devicein_code){
		return repo.getByCode(orgrootid_link,devicein_code);
	}
	
	@Override
	public List<DeviceOut> getByOrgFromAndStatus(Long orgrootid_link,
			Long orgid_from_link,
			Integer status){
		return repo.getByOrgFromAndStatus(orgrootid_link,orgid_from_link,status);
	}
	
	@Override
	public List<DeviceOut> getByOrgToAndStatus(Long orgrootid_link,
			Long orgid_to_link,
			Integer status){
		return repo.getByOrgToAndStatus(orgrootid_link,orgid_to_link,status);
	}
	
	@Override
	public List<DeviceOut> filterAll(Long orgrootid_link,
			String deviceout_code,
			Date deviceout_date_from,
			Date deviceout_date_to,
			Integer deviceouttypeid_link,
			String invoice_code,
			Long orgid_from_link,
			Long orgid_to_link,
			Integer usercreateid_link,
			Integer status){
		try {
			Specification<DeviceOut> specification = Specifications.<DeviceOut>and()
		            .eq( "orgrootid_link", orgrootid_link)
		            .eq(Objects.nonNull(deviceout_code) && deviceout_code.length() > 0, "deviceout_code", deviceout_code)
		            .eq(Objects.nonNull(deviceouttypeid_link), "deviceouttypeid_link", deviceouttypeid_link)
		            .eq(Objects.nonNull(invoice_code), "invoice_code", invoice_code)
		            .eq(Objects.nonNull(orgid_from_link), "orgid_from_link", orgid_from_link)
		            .eq(Objects.nonNull(orgid_to_link), "orgid_to_link", orgid_to_link)
		            .eq(Objects.nonNull(usercreateid_link), "usercreateid_link", usercreateid_link)
		            .ge((deviceout_date_from!=null && deviceout_date_to==null),"deviceout_date",GPAYDateFormat.atStartOfDay(deviceout_date_from))
	                .le((deviceout_date_from==null && deviceout_date_to!=null),"deviceout_date",GPAYDateFormat.atEndOfDay(deviceout_date_to))
	                .between((deviceout_date_from!=null && deviceout_date_to!=null),"deviceout_date", GPAYDateFormat.atStartOfDay(deviceout_date_from), GPAYDateFormat.atEndOfDay(deviceout_date_to))
	                .eq(Objects.nonNull(status), "status", status)
//	                .ne("status", -1)
//	                .eq("status", 0)
		            .build();
//			Sort sort = Sorts.builder()
//			        .desc("deviceout_date")
//			        .build();
			List<DeviceOut> a = repo.findAll(specification);
			return a;
		} catch (Exception ex){
			ex.printStackTrace();
			return null;
		}
	}
}