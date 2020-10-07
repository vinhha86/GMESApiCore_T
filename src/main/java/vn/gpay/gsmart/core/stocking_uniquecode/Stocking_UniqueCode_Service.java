package vn.gpay.gsmart.core.stocking_uniquecode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Stocking_UniqueCode_Service extends AbstractService<Stocking_UniqueCode> implements IStocking_UniqueCode_Service {
	@Autowired IStocking_Unique_Repository repo;
	@Override
	protected JpaRepository<Stocking_UniqueCode, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public Stocking_UniqueCode getby_type(Integer type) {
		// TODO Auto-generated method stub
		return repo.getby_type(type).get(0);
	}

}
