package vn.gpay.gsmart.core.api.stockin;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class StockinListRequest extends RequestBase{

	public String stockcode;
	public Long orgid_to_link;
	public Long orgid_from_link;
	public Date stockindate_from;
	public Date stockindate_to;
	public Long stockintypeid_link;
	public int limit;
	public int page;
}
