package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class LaborLevelServiceImpl  extends AbstractService<LaborLevel> implements ILaborLevelService{
	
	@Autowired LaborLevelRepository repo;

	@Override
	protected JpaRepository<LaborLevel, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<LaborLevel> findAllByOrderByIdAsc() {
		// TODO Auto-generated method stub
		return repo.findAllByOrderByIdAsc();
	}

}
