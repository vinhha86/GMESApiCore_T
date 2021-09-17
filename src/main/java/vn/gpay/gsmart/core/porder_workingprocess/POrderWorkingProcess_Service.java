package vn.gpay.gsmart.core.porder_workingprocess;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderWorkingProcess_Service extends AbstractService<POrderWorkingProcess> implements IPOrderWorkingProcess_Service {

	@Autowired IPOrderWorkingProcess_Repository repo;
	@Override
	protected JpaRepository<POrderWorkingProcess, Long> getRepository() {
		return repo;
	}
	
	@Override
	public List<POrderWorkingProcess>findAll_SubProcess(){
		return repo.findAll_SubProcess();
	}
	@Override
	public List<POrderWorkingProcess>findAll_MainProcess(){
		return repo.findAll_MainProcess();
	}

	@Override
	public List<POrderWorkingProcess> getby_product(Long productid_link) {
		return repo.getby_product(productid_link);
	}

	@Override
	public List<POrderWorkingProcess> getByName_Product(String name, Long productid_link) {
		return repo.getByName_Product(name, productid_link);
	}

	@Override
	public List<POrderWorkingProcess> getby_porder(Long porderid_link) {
		return repo.getby_porder(porderid_link);
	}

	@Override
	public List<POrderWorkingProcess> getByCode(String code, Long productid_link, Long porderid_link) {
		return repo.getByCode(code, productid_link, porderid_link);
	}

	@Override
	public List<POrderWorkingProcess> getByCode_NotId(String code, Long productid_link, Long porderid_link, Long id) {
		return repo.getByCode_NotId(code, productid_link, porderid_link, id);
	}

}
