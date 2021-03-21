package vn.gpay.gsmart.core.cutplan_processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class CutplanProcessingDServiceImpl extends AbstractService<CutplanProcessingD> implements ICutplanProcessingDService{

	@Autowired
	CutplanProcessingDRepository repo;
	
	@Override
	protected JpaRepository<CutplanProcessingD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
