package vn.gpay.gsmart.core.laborlevel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class LaborLevel_Service extends AbstractService<LaborLevel> implements ILaborLevel_Service {
	@Autowired ILaborLevel_Repository repo;
	@Override
	protected JpaRepository<LaborLevel, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
