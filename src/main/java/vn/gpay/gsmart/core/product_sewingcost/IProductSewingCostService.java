package vn.gpay.gsmart.core.product_sewingcost;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IProductSewingCostService extends Operations<ProductSewingCost> {
	List<ProductSewingCost> getby_product_and_workingprocess(Long productid_link, Long workingprocessid_link);
	List<ProductSewingCost> getby_workingprocess(Long workingprocessid_link);
	public List<ProductSewingCost> getByProductUnused(Long productid_link, List<Long> listProductBalanceProcessId);
	public List<ProductSewingCost> getByProductUnused(Long productid_link);

}
