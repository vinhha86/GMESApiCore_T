package vn.gpay.gsmart.core.api.handover;

import vn.gpay.gsmart.core.base.RequestBase;

public class Handover_setstatus_request extends RequestBase{
	public Integer status;
	public Long handoverid_link;
	public Long approver_userid_link;
	public Long receiver_userid_link;
}
