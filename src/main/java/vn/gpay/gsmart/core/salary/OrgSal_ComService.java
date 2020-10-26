package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_ComService extends AbstractService<OrgSal_Com> implements IOrgSal_ComService {
	@Autowired IOrgSal_ComRepository repo;
	
	@Override
	public List<OrgSal_Com> getall_byorg(long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorg(orgid_link);
	}

	@Override
	protected JpaRepository<OrgSal_Com, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
