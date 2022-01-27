package vn.gpay.gsmart.core.api.Stockout_order;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class Stockout_order_getBySearch_request extends RequestBase{
	public Date stockoutorderdate_from;
	public Date stockoutorderdate_to;
	public Integer stockouttypeid_link;
	public int page;
	public int limit;
	public Integer stockouttypeid_link_from;
	public Integer stockouttypeid_link_to;
}
