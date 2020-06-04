package vn.gpay.gsmart.core.porder_product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrder_Product_Service extends AbstractService<POrder_Product> implements IPOrder_Product_Service {
	@Autowired IPOrder_Product_Repository repo;
	@Override
	protected JpaRepository<POrder_Product, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
