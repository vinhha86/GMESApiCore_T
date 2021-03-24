package vn.gpay.gsmart.core.api.handover_sku;

import vn.gpay.gsmart.core.base.RequestBase;

public class HandoverSKU_getByHandoverProduct_request extends RequestBase {
	public Long handoverid_link;
	public Long handoverproductid_link;
	public Long porderid_link;
	public Long productid_link;
	public Long orgid_to_link; // org grant
}
