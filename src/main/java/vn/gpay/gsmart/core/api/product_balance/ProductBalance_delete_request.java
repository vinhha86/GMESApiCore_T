package vn.gpay.gsmart.core.api.product_balance;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class ProductBalance_delete_request extends RequestBase{
	public Long id;
	public List<Long> idList;
}
