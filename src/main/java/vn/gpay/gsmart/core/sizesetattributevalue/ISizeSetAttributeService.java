package vn.gpay.gsmart.core.sizesetattributevalue;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ISizeSetAttributeService extends Operations<SizeSetAttributeValue>{
	public List<SizeSetAttributeValue> getall_bySizeSetId(Long sizesetid_link);
	public List<SizeSetAttributeValue> getList_byAttId(Long attributeid_link, Long sizesetid_link);
	public List<SizeSetAttributeValue> getOne_bysizeset_and_value(long sizesetid_link, long attributeid_link, long attributevalueid_link);
	public List<SizeSetAttributeValue> getallother_bySizeSetId(Long sizesetid_link);
}
