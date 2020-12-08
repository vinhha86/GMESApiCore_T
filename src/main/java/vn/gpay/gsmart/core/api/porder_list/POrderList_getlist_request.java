package vn.gpay.gsmart.core.api.porder_list;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderList_getlist_request extends RequestBase{
	public String ordercode;
	public String povendor;
	public String pobuyer;
	public String style;
	public Long buyerid;
	public Long vendorid;
	public Long factoryid;
	public Date golivedatefrom;
	public Date golivedateto;
	public List<Integer> status;
	public int limit;
	public int page;
}
