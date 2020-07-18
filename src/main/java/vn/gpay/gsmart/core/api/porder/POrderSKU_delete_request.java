package vn.gpay.gsmart.core.api.porder;

import java.util.List;

import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;

public class POrderSKU_delete_request {
	public Long porderid_link;
	public List<POrder_Product_SKU> data;
}
