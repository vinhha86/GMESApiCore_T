package vn.gpay.gsmart.core.api.product_balance;

import vn.gpay.gsmart.core.base.RequestBase;

public class ProductBalance_create_request extends RequestBase{
	public Long productid_link;
	public Integer amount;
	public String name;
}
