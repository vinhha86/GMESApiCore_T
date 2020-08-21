package vn.gpay.gsmart.core.porder_grant;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;

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
	public List<POrderGrant> get_granted_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link, 
			String POBuyer, Long orgbuyerid_link , Long orgvendorid_link){
		
		int status = 1;
		POBuyer = "%"+POBuyer+"%";
		orgvendorid_link = orgvendorid_link == 0 ? null : orgvendorid_link;
		orgbuyerid_link = orgbuyerid_link == 0 ? null : orgbuyerid_link;
		
		List<POrderGrant> a = repo.get_granted_bygolivedate(status, granttoorgid_link, golivedate_from,
				golivedate_to, POBuyer, orgbuyerid_link, orgvendorid_link);
		return a;
	}

	@Override
	public List<POrderGrant> get_porder_test(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,
			String POBuyer, Long orgbuyerid_link, Long orgvendorid_link) {
		// TODO Auto-generated method stub
		POBuyer = "%"+POBuyer+"%";
		orgvendorid_link = orgvendorid_link == 0 ? null : orgvendorid_link;
		orgbuyerid_link = orgbuyerid_link == 0 ? null : orgbuyerid_link;
		List<POrderGrant> a = repo.get_grantedTest_bygolivedate(granttoorgid_link, golivedate_from,
				golivedate_to, POBuyer, orgbuyerid_link, orgvendorid_link);
		return a;
	}

	@Override
	public List<POrderGrant> getByOrderId(Long porderid_link) {
		return repo.getByOrderId(porderid_link);
	}

	@Override
	public List<POrderGrant> getByOrgId(Long granttoorgid_link) {
		// TODO Auto-generated method stub
		return repo.getByOrgId(granttoorgid_link);
	}
}
