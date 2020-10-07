package vn.gpay.gsmart.core.handover_sku;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HandoverSKUService extends AbstractService<HandoverSKU> implements IHandoverSKUService{

	@Autowired HandoverSKURepository repo;
	
	@Override
	protected JpaRepository<HandoverSKU, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
