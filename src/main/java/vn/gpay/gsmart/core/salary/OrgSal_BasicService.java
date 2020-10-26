package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_BasicService extends AbstractService<OrgSal_Basic> implements IOrgSal_BasicService {
	@Autowired IOrgSal_BasicRepository repo;
	
	@Override
	public List<OrgSal_Basic> getall_byorg(long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorg(orgid_link);
	}

	@Override
	protected JpaRepository<OrgSal_Basic, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
