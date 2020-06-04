package vn.gpay.gsmart.core.api.stockin;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class StockinListRequest extends RequestBase{

	public Long stockcode;
	public String stockincode;
	public Date stockindate_from;
	public Date stockindate_to;
	public Long stockintypeid_link;
	public int status;
}
