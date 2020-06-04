package vn.gpay.gsmart.core.api.product;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValueBinding;


public class Product_getattvalue_response extends ResponseBase {
	public List<ProductAttributeValueBinding> data;
}
