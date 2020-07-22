package vn.gpay.gsmart.core.porder_status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrder_Status_Service extends AbstractService<POrder_Status> implements IPOrder_Status_Service{
	
	@Autowired IPOrder_Status_Repository repo;
	
	@Override
	protected JpaRepository<POrder_Status, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
