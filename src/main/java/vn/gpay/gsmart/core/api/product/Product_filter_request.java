package vn.gpay.gsmart.core.api.product;

import java.util.List;

import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.base.RequestBase;

public class Product_filter_request extends RequestBase {
	public int product_type;
	public String code;
	public String partnercode;
	public List<Attribute> attributes;
	public Long productid_link;
	public Long orgcustomerid_link;
}
