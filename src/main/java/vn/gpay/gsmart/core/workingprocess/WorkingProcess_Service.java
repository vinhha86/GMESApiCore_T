package vn.gpay.gsmart.core.workingprocess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class WorkingProcess_Service extends AbstractService<WorkingProcess> implements IWorkingProcess_Service {
	@Autowired IWorkingProcess_Repository repo;
	@Override
	protected JpaRepository<WorkingProcess, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
	@Override
	public List<WorkingProcess>findAll_SubProcess(){
		return repo.findAll_SubProcess();
	}
	@Override
	public List<WorkingProcess>findAll_MainProcess(){
		return repo.findAll_MainProcess();
	}

	@Override
	public List<WorkingProcess> getby_product(Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getby_product(productid_link);
	}
}
