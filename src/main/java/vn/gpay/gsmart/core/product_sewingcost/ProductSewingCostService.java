package vn.gpay.gsmart.core.product_sewingcost;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class ProductSewingCostService  extends AbstractService<ProductSewingCost> implements IProductSewingCostService {
	@Autowired IProductSewingCostRepository repo;
	@Override
	protected JpaRepository<ProductSewingCost, Long> getRepository() {
		return repo;
	}
	@Override
	public List<ProductSewingCost> getby_product_and_workingprocess(Long productid_link, Long workingprocessid_link) {
		workingprocessid_link = workingprocessid_link == 0 ? null : workingprocessid_link;
		
		return repo.getby_product_and_workingprocess(productid_link, workingprocessid_link);
	}
	@Override
	public List<ProductSewingCost> getByProductUnused(Long productid_link, List<Long> listProductBalanceProcessId) {
		return repo.getByProductUnused(productid_link, listProductBalanceProcessId);
	}
	@Override
	public List<ProductSewingCost> getByProductUnused(Long productid_link) {
		return repo.getByProductUnused(productid_link);
	}
	@Override
	public List<ProductSewingCost> getby_workingprocess(Long workingprocessid_link) {
		return repo.getby_workingprocess(workingprocessid_link);
	}


}
