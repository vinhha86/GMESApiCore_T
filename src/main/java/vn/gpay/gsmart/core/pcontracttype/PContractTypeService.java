package vn.gpay.gsmart.core.pcontracttype;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContractTypeService extends AbstractService<PContractType> implements IPContractTypeService{

	@Autowired PContractTypeRepository repo;
	
	@Override
	protected JpaRepository<PContractType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
