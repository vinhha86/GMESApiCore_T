package vn.gpay.gsmart.core.porder_product_sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_Product_SKU_Service extends Operations<POrder_Product_SKU> {
	public List<POrder_Product_SKU> getby_productid_link(Long productid_link);

	public List<POrder_Product_SKU> getby_porderandsku(Long porderid_link, Long skuid_link);

	public List<POrder_Product_SKU> getby_porder(Long porderid_link);
	
	public POrder_Product_SKU get_sku_in_encode(Long porderid_link , Long skuid_link);
	
	public List<POrder_Product_SKU> getlist_sku_in_porder(Long orgrootid_link, Long porderid_link);
	
	List<Long> getlist_colorid_byporder(Long porderid_link);

	List<POrder_Product_SKU> getsumsku_byporder(long porderid_link);
}
