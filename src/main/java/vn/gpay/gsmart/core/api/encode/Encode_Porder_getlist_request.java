package vn.gpay.gsmart.core.api.encode;

import java.util.Date;

public class Encode_Porder_getlist_request {
	public String pordercode;
	public long usercreateid_link;
	public Date encodedatefrom;
	public Date encodedateto;
	public int limit;
	public int page;
}
