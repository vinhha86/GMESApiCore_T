package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_Type_LevelService extends AbstractService<OrgSal_Type_Level> implements IOrgSal_Type_LevelService {
	@Autowired IOrgSal_Type_LevelRepository repo;
	
	@Override
	public List<OrgSal_Type_Level> getall_byorgrootid(long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorgrootid(orgrootid_link);
	}

	@Override
	public List<OrgSal_Type_Level> getall_byorg_and_type(Long orgid_link, Integer typeid_link) {
		// TODO Auto-generated method stub
		return repo.getall_byorg_and_type(orgid_link,typeid_link);
	}

	
	@Override
	protected JpaRepository<OrgSal_Type_Level, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
