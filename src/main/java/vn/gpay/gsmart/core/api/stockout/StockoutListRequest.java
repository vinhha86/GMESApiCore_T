package vn.gpay.gsmart.core.api.stockout;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class StockoutListRequest extends RequestBase{

	public Long orgid_to_link;
	public Long orgid_from_link;
	public String stockoutcode;
	public Date stockoutdate_from;
	public Date stockoutdate_to;
	public Integer stockouttypeid_link;
	public int page;
	public int limit;
}
