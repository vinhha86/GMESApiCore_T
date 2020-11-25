package vn.gpay.gsmart.core.api.porder;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrder_updateStatus_request extends RequestBase{
	public Long porderid_link;
	public Integer status;
}
