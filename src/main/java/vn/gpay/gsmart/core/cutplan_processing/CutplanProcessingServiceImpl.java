package vn.gpay.gsmart.core.cutplan_processing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class CutplanProcessingServiceImpl extends AbstractService<CutplanProcessing> implements ICutplanProcessingService{

	@Autowired
	CutplanProcessingRepository repo;
	
	@Override
	protected JpaRepository<CutplanProcessing, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
