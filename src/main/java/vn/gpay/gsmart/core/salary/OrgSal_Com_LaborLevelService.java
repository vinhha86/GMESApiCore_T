package vn.gpay.gsmart.core.salary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_Com_LaborLevelService extends AbstractService<OrgSal_Com_LaborLevel> implements IOrgSal_Com_LaborLevelService {
	@Autowired IOrgSal_Com_LaborLevelRepository repo;
	
	@Override
	protected JpaRepository<OrgSal_Com_LaborLevel, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
