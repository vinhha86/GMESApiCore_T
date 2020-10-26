package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_TypeService extends AbstractService<OrgSal_Type> implements IOrgSal_TypeService {
	@Autowired IOrgSal_TypeRepository repo;
	
	@Override
	public List<OrgSal_Type> getall_byorg(Long orgid_link, Integer typeid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorg(orgid_link, typeid_link);
	}

	@Override
	protected JpaRepository<OrgSal_Type, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
