package vn.gpay.gsmart.core.attributevalue;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IAttributeValueService extends Operations<Attributevalue> {
	public List<Attributevalue> getlist_byidAttribute(Long attributeid_link);
	public List<Attributevalue> getlistid_notin_pcontract_attribute(long orgrootid_link, long pcontractid_link, long productid_link, long attributeid_link);
	Integer getMaxSortValue(Long id);
	public List<Attributevalue> getByValue(String value, Long attributeid_link);
	public List<Attributevalue> getColorForStockin(Long stockinid_link);
}
