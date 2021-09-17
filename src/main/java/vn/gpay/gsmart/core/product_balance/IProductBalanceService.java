package vn.gpay.gsmart.core.product_balance;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IProductBalanceService extends Operations<ProductBalance>{
	public List<ProductBalance> getByProduct(Long productid_link);
	List<ProductBalance> getByProductAndProductSewingCost(Long productid_link, Long productsewingcostid_link);
	List<ProductBalance> getByBalanceName_Product(String balance_name, Long productid_link);

}
