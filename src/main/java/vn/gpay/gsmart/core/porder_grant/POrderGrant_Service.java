package vn.gpay.gsmart.core.porder_grant;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.wenhao.jpa.Specifications;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.DateFormat;

@Service
public class POrderGrant_Service extends AbstractService<POrderGrant> implements IPOrderGrant_Service {
	@Autowired IPOrderGrant_Repository repo;
	@Override
	protected JpaRepository<POrderGrant, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<POrderGrant>getByOrderCodeAndOrg(Long granttoorgid_link, String ordercode){
		return repo.getByOrderCodeAndOrg(granttoorgid_link, ordercode);
	}
	
	@Override
	public POrderGrant getByOrderIDAndOrg(Long granttoorgid_link, Long porderid_link){
		List<POrderGrant> a= repo.getByOrderIDAndOrg(granttoorgid_link, porderid_link);
		if (a.size() > 0)
			return a.get(0);
		else
			return null;
	}
	
	@Override
	public void deleteByOrderId(Long porderid_link){
		for(POrderGrant pordergrant: repo.getByOrderId(porderid_link)){
			repo.delete(pordergrant);
		}
	}
	
	@Override
	//Danh sach cac lenh duoc phan cho Phan xuong va da phan chuyen
	public List<POrderGrant> get_granted_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link){
//		if(granttoorgid_link == 3 || granttoorgid_link ==5) {
//			long i = 0;
//		}
		Specification<POrderGrant> specification = Specifications.<POrderGrant>and()
				.ge("porder.status", 1)
	            .eq("granttoorgid_link", granttoorgid_link)
	            .ge(Objects.nonNull(golivedate_from),"porder.golivedate",DateFormat.atStartOfDay(golivedate_from))
                .le(Objects.nonNull(golivedate_to),"porder.golivedate",DateFormat.atEndOfDay(golivedate_to))
	            .build();

		List<POrderGrant> a = repo.findAll(specification);
		return a;
	}
}
