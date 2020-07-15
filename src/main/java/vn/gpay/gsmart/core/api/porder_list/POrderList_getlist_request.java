package vn.gpay.gsmart.core.api.porder_list;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderList_getlist_request extends RequestBase{
	public String ordercode;
	public String po;
	public String style;
	public Long buyerid;
	public Long vendorid;
	public Date orderdatefrom;
	public Date orderdateto;
}
