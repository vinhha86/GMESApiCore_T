package vn.gpay.gsmart.core.api.porder;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;

public class get_sku_by_porder_and_po_response extends ResponseBase {
	public List<POrder_Product_SKU> data;
}
