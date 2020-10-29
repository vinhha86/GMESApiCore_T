package vn.gpay.gsmart.core.salary;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_Type_LaborLevelService extends AbstractService<OrgSal_Type_LaborLevel> implements IOrgSal_Type_LaborLevelService {
	@Autowired IOrgSal_Type_LaborLevelRepository repo;

	@Override
	protected JpaRepository<OrgSal_Type_LaborLevel, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<OrgSal_Type_LaborLevel> getall_bysaltype(Long saltypeid_link) {
		return repo.getall_bysaltype(saltypeid_link);
	}
	
	@Override
	public List<OrgSal_Type_LaborLevel> getall_bysaltype_laborlevel(Long saltypeid_link, Long laborlevelid_link) {
		return repo.getall_bysaltype_laborlevel(saltypeid_link, laborlevelid_link);
	}
}
