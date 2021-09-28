package vn.gpay.gsmart.core.fob_price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class FOBPrice_Service extends AbstractService<FOBPrice> implements IFOBService {
	
	@Autowired FOBPrice_repository repo;
	
	@Override
	protected JpaRepository<FOBPrice, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<FOBPrice> getPrice_by_orgroot(Long orgrootid_link) {
		// TODO Auto-generated method stub
		return repo.getbyorgroot(orgrootid_link);
	}

	@Override
	public List<FOBPrice> getAllDefault() {
		// TODO Auto-generated method stub
		return repo.getAllDefault();
	}

	@Override
	public List<FOBPrice> getByName(String name) {
		return repo.getByName(name);
	}

	@Override
	public List<FOBPrice> getByName_other(String name, Long id) {
		return repo.getByName_other(name, id);
	}

}
