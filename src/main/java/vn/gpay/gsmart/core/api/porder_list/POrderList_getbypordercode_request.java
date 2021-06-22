package vn.gpay.gsmart.core.api.porder_list;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderList_getbypordercode_request extends RequestBase {
	public String pordercode;
	public String ordercode;
	public Long granttoorgid_link;
}
