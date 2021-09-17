package vn.gpay.gsmart.core.product_balance_process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ProductBalanceProcessService extends AbstractService<ProductBalanceProcess> implements IProductBalanceProcessService{

	@Autowired IProductBalanceProcessRepository repo;
	
	@Override
	protected JpaRepository<ProductBalanceProcess, Long> getRepository() {
		return repo;
	}

	@Override
	public List<Long> getProductBalanceProcessIdByProduct(Long productid_link) {
		return repo.getProductBalanceProcessIdByProduct(productid_link);
	}

	@Override
	public List<ProductBalanceProcess> getByProductSewingcost(Long productsewingcostid_link) {
		return repo.getByProductSewingcost(productsewingcostid_link);
	}

}
