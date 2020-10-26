package vn.gpay.gsmart.core.handover_sku;

import java.util.List;

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

	@Override
	public List<HandoverSKU> getByHandoverId(Long handoverid_link, Long productid_link) {
		// TODO Auto-generated method stub
		return repo.getByHandoverId(handoverid_link, productid_link);
	}
	
	@Override
	public List<HandoverSKU> getByHandoverId(Long handoverid_link) {
		// TODO Auto-generated method stub
		return repo.getByHandoverId(handoverid_link);
	}

	@Override
	public List<HandoverSKU> getByHandoverIdAndProductId(Long handoverid_link, Long handoverproductid_link) {
		// TODO Auto-generated method stub
		return repo.getByHandoverIdAndProductId(handoverid_link, handoverproductid_link);
	}

}
