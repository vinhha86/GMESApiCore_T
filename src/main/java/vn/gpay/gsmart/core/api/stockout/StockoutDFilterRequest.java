package vn.gpay.gsmart.core.api.stockout;

import java.util.Date;

public class StockoutDFilterRequest{
	public Date stockoutdate_from;
	public Date stockoutdate_to;
	public Long skuid_link;
	public String skucode;
	public Integer stockouttypeid;
}
