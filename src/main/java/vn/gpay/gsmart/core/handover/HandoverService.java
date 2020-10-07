package vn.gpay.gsmart.core.handover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HandoverService  extends AbstractService<Handover> implements IHandoverService{

	@Autowired HandoverRepository repo;
	
	@Override
	protected JpaRepository<Handover, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
