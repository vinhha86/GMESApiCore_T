package vn.gpay.gsmart.core.sku;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface ISKU_AttributeValue_Service extends Operations<SKU_Attribute_Value> {
	public List<SKU_Attribute_Value> getlist_byProduct_and_value(Long productid_link, Long attributevalueid_link);
	
	public List<SKU_Attribute_Value> getlist_byProduct_and_attribute(Long productid_link, Long attributeid_link);

	public List<SKU_Attribute_Value> getlist_byproduct(Long productid_link);
	
	public List<SKU_Attribute_Value> getlist_bysku(Long skuid_link);
	
	public long getsku_byproduct_and_valuemau_valueco(long productid_link, long valuemau, long valueco);
	
	public List<SKU_Attribute_Value> get_bycolor(long pcontractid_link, long productid_link, long colorid_link);
}	
