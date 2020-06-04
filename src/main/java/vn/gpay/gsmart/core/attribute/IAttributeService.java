package vn.gpay.gsmart.core.attribute;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface IAttributeService extends Operations<Attribute> {
	public List<Attribute> getList_byorgid_link(Long orgrootid_link);
	
	public List<Attribute> getList_attribute_forproduct(Integer product_type, Long orgrootid_link);
	
	public List<Attribute> getlist_notin_pcontractproduct(long pcontractid_link, long productid_link, long orgrootid_link);
}
