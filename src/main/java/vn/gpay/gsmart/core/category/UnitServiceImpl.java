package vn.gpay.gsmart.core.category;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class UnitServiceImpl extends AbstractService<Unit> implements IUnitService {

	@Autowired
	UnitRepository repositoty;

	@Override
	protected JpaRepository<Unit, Long> getRepository() {
		// TODO Auto-generated method stub
		return repositoty;
	}

	@Override
	public List<Unit> getbyName(String name) {
		// TODO Auto-generated method stub
		return repositoty.getByName(name);
	}

}
