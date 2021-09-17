package vn.gpay.gsmart.core.product_balance_process;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IProductBalanceProcessService extends Operations<ProductBalanceProcess>{
	public List<Long> getProductBalanceProcessIdByProduct(Long productid_link);
	List<ProductBalanceProcess> getByProductSewingcost(Long productsewingcostid_link);

}
