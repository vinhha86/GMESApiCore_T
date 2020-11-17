package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;


@Service
public class OrgSal_BasicService extends AbstractService<OrgSal_Basic> implements IOrgSal_BasicService {
	@Autowired IOrgSal_BasicRepository repo;
	@Autowired IOrgService orgService;
	
	@Override
	public List<OrgSal_Basic> getall_byorg(long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorg(orgid_link);
	}

	@Override
	public OrgSal_Basic getone_byorg(long orgid_link) {
		Org theOrg = orgService.findOne(orgid_link);
		if (null != theOrg){
			Boolean iscontinue = true;
			OrgSal_Basic result = null;
			while (iscontinue){
				List<OrgSal_Basic> a = repo.getall_byorg(orgid_link);
				if (a.size() > 0){
					result = a.get(0);
					iscontinue = false;
				} else {
					if (null != theOrg.getParentid_link()){
						orgid_link = theOrg.getParentid_link();
						iscontinue = true;
					} else {
						iscontinue = false;
					}
				}
			}
			return result;
		} else 
			return null;
	}
	
	@Override
	protected JpaRepository<OrgSal_Basic, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
