package vn.gpay.gsmart.core.api.sizesetattributevalue;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class SizeSetAttribute_createvalue_request extends RequestBase{
	public Long attributeid_link;
	public Long sizesetid_link;
	public List<Long> listvalue;
}
