package vn.gpay.gsmart.core.api.handover_product;

import vn.gpay.gsmart.core.base.RequestBase;

public class HandoverProduct_GetByPorderId_request extends RequestBase{
	public Long porderid_link;
	public Long productid_link; // pack to stock
	public Integer product_quantity; // pack to stock
}
