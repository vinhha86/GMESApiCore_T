package vn.gpay.gsmart.core.position;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Position_Service extends AbstractService<Position> implements IPosition_Service {
	@Autowired Position_Repository repo;
	@Override
	protected JpaRepository<Position, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
