package vn.gpay.gsmart.core.salary;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class OrgSal_LevelService extends AbstractService<OrgSal_Level> implements IOrgSal_LevelService {
	@Autowired IOrgSal_LevelRepository repo;

	@Override
	protected JpaRepository<OrgSal_Level, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
