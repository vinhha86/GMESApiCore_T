package vn.gpay.gsmart.core.api.PorderPOline;

import java.util.Date;

import vn.gpay.gsmart.core.base.ResponseBase;

public class add_grant_response extends ResponseBase {
	public String porderinfo;
	public int amount;
	public int duration;
	public Date startDate;
	public Date endDate;
}
