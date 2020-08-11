package vn.gpay.gsmart.core.invcheck;

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
public class InvcheckServiceImpl extends AbstractService<Invcheck> implements IInvcheckService{

	@Autowired
	InvcheckRepository repository; 
	@Override
	protected JpaRepository<Invcheck, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<Invcheck> invcheck_list(Long orgid_link,String stockcode, String orgfrom_code, Date invdateto_from, Date invdateto_to,Integer status) {
		// TODO Auto-generated method stub
		
		Specification<Invcheck> specification = Specifications.<Invcheck>and()
	            .eq( "orgid_link", orgid_link)
	            .like(Objects.nonNull(stockcode), "invcheckcode", "%"+stockcode+"%")
//	            .eq(Objects.nonNull(orgfrom_code), "orgcheckid_link", orgfrom_code)
	            .ge((invdateto_from!=null && invdateto_to==null),"invcheckdatetime",GPAYDateFormat.atStartOfDay(invdateto_from))
                .le((invdateto_from==null && invdateto_to!=null),"invcheckdatetime",GPAYDateFormat.atEndOfDay(invdateto_to) )
                .between((invdateto_from!=null && invdateto_to!=null),"invcheckdatetime", GPAYDateFormat.atStartOfDay(invdateto_from), GPAYDateFormat.atEndOfDay(invdateto_to))
                .eq( status!=-1,"status", status)
	            .build();
		Sort sort = Sorts.builder()
		        .desc("invcheckdatetime")
		        .build();
	    return repository.findAll(specification,sort);
		
		//return repository.invcheck_list(orgid_link,stockcode, orgfrom_code, invdateto_from, invdateto_to);
	}
	@Override
	public List<Invcheck> invcheck_getactive(Long orgcheckid_link ) {
		// TODO Auto-generated method stub
		return repository.invcheck_getactive(orgcheckid_link);
	}
	@Override
	public List<Invcheck> invcheck_getbycode(String invcheckcode ) {
		// TODO Auto-generated method stub
		return repository.invcheck_getbycode(invcheckcode);
	}	
	@Override
	public void invcheck_deactive(Long invcheckid) {
		// TODO Auto-generated method stub
		 repository.invcheck_deactive(invcheckid);
	}

}
