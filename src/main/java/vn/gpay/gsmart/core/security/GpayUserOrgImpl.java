package vn.gpay.gsmart.core.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class GpayUserOrgImpl  extends AbstractService<GpayUserOrg> implements IGpayUserOrgService{

	@Autowired
	GpayUserOrgRepository repository; 
	@Override
	protected JpaRepository<GpayUserOrg, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	
	@Override
	public List<GpayUserOrg> getall_byuser(Long userid_link) {
		// TODO Auto-generated method stub
		return repository.getall_byuser(userid_link);
	}
	
	@Override
	public List<GpayUserOrg> getall_byuser_andtype(Long userid_link, Integer orgtypeid_link) {
		// TODO Auto-generated method stub
		return repository.getall_byuser_andtype(userid_link, orgtypeid_link);
	}
	
	@Override
	public List<GpayUserOrg> getby_user_org(Long userid_link, Long orgid_link) {
		// TODO Auto-generated method stub
		return repository.getby_user_org(userid_link, orgid_link);
	}
}
