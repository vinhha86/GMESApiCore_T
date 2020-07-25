package vn.gpay.gsmart.core.api.porder_list;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;

public class POrderList_getProductSKUbyPorder_response extends ResponseBase{
	public List<POrder_Product_SKU> data;
}
