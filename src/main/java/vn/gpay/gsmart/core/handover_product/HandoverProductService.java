package vn.gpay.gsmart.core.handover_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HandoverProductService extends AbstractService<HandoverProduct> implements IHandoverProductService{

	@Autowired HandoverProductRepository repo;
	
	@Override
	protected JpaRepository<HandoverProduct, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
