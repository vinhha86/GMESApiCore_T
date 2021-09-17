package vn.gpay.gsmart.core.product_balance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ProductBalanceService extends AbstractService<ProductBalance> implements IProductBalanceService{
	
	@Autowired IProductBalanceRepository repo;

	@Override
	protected JpaRepository<ProductBalance, Long> getRepository() {
		return repo;
	}

	@Override
	public List<ProductBalance> getByProduct(Long productid_link) {
		return repo.getByProduct(productid_link);
	}

	@Override
	public List<ProductBalance> getByProductAndProductSewingCost(Long productid_link, Long productsewingcostid_link) {
		return repo.getByProductAndProductSewingCost(productid_link, productsewingcostid_link);
	}

	@Override
	public List<ProductBalance> getByBalanceName_Product(String balance_name, Long productid_link) {
		return repo.getByBalanceName_Product(balance_name, productid_link);
	}

}
