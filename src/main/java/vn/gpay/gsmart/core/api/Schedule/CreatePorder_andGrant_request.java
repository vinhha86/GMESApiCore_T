package vn.gpay.gsmart.core.api.Schedule;

import java.util.Date;

public class CreatePorder_andGrant_request {
	public Date startdate;
	public Date enddate;
	public Integer quantity;
	public Integer duration;
	public Integer productivity;
	public Long orgid_link;
	public Long orggrantid_link;
	public Long pcontract_poid_link;
}
