package vn.gpay.gsmart.core.api.handover_product;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.handover_product.HandoverProduct;

public class HandoverProduct_GetByPorderId_response extends ResponseBase {
	public HandoverProduct data;
	public String buyername;
	public String buyercode;
	public String unitName;
}
