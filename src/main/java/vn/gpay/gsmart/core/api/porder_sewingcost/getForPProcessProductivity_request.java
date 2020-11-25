package vn.gpay.gsmart.core.api.porder_sewingcost;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class getForPProcessProductivity_request extends RequestBase {
	public Long personnelid_link;
	public Date processingdate;
	public Integer shifttypeid_link;
	public Long porderid_link;
	public Long pordergrantid_link;
}
