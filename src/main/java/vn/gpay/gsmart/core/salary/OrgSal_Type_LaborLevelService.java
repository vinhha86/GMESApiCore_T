package vn.gpay.gsmart.core.salary;

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

}
