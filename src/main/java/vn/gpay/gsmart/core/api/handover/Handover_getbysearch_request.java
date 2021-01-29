package vn.gpay.gsmart.core.api.handover;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class Handover_getbysearch_request extends RequestBase {
	public Long handovertypeid_link;
	public String ordercode;
	public Date handover_datefrom;
	public Date handover_dateto;
	public Long orgid_from_link;
	public Long orgid_to_link;
	public List<Integer> status;
	public Integer limit;
	public Integer page;
	public String viewId;
}