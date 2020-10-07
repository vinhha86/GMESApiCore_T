package vn.gpay.gsmart.core.api.encode;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.encode.Encode;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU_Encode;

public class Encode_porder_getinfo_response extends ResponseBase{
	public Encode data;
	public POrder_Product_SKU_Encode skuencode; 
}
