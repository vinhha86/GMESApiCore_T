package vn.gpay.gsmart.core.api.product;

import vn.gpay.gsmart.core.base.RequestBase;

public class Product_getall_request extends RequestBase {
	public int product_type;
	public int limit;
	public int page;
	public String name;
	public String code;
	
	public String buyercode;
	public Integer producttypeid_link;
}
