package vn.gpay.gsmart.core.productattributevalue;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IProductAttributeService extends Operations<ProductAttributeValue> {
	public List<ProductAttributeValue> getall_byProductId(Long productid_link);
	public List<ProductAttributeValue> getList_byAttId(Long attributeid_link, Long productid_link);
	public List<ProductAttributeValue> getOne_byproduct_and_value(long productid_link, long attributeid_link, long attributevalueid_link);
}
