package vn.gpay.gsmart.core.handover;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HandoverTypeService extends AbstractService<HandoverType> implements IHandoverTypeService {

	@Autowired HandoverTypeRepository repo;
	
	@Override
	protected JpaRepository<HandoverType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
