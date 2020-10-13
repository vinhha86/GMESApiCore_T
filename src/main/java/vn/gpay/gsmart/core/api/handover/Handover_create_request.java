package vn.gpay.gsmart.core.api.handover;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.handover.Handover;
import vn.gpay.gsmart.core.handover_product.HandoverProduct;

public class Handover_create_request extends RequestBase{
	public Handover data;
	public HandoverProduct handoverProduct;
}
