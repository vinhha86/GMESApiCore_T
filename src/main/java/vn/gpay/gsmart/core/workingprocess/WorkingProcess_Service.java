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
		return repo.getby_product(productid_link);
	}

	@Override
	public List<WorkingProcess> getByName_Product(String name, Long productid_link) {
		return repo.getByName_Product(name, productid_link);
	}

	@Override
	public List<WorkingProcess> getByCode(String code, Long productid_link) {
		return repo.getByCode(code, productid_link);
	}

	@Override
	public List<WorkingProcess> getByCode_NotId(String code, Long productid_link, Long id) {
		return repo.getByCode_NotId(code, productid_link, id);
	}
}
