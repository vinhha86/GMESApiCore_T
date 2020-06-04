package vn.gpay.gsmart.core.api.product;

import java.util.List;


import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.product.ProductBinding;

public class Product_getall_response extends ResponseBase {
	public List<Product> data;
	public List<ProductBinding> pagedata;
	public long totalCount;
}
