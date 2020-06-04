package vn.gpay.gsmart.core.porder_grant;

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
	public void deleteByOrderId(Long porderid_link){
		for(POrderGrant pordergrant: repo.getByOrderId(porderid_link)){
			repo.delete(pordergrant);
		}
	}
}
