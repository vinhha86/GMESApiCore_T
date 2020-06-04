package vn.gpay.gsmart.core.porder_product_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_Product_SKU_Service extends Operations<POrder_Product_SKU> {
	public List<POrder_Product_SKU> getby_productid_link(Long productid_link);

	public List<POrder_Product_SKU> getby_porderandsku(Long porderid_link, Long skuid_link);

	public List<POrder_Product_SKU> getby_porder(Long porderid_link);
}
