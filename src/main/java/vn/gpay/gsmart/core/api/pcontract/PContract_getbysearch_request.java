package vn.gpay.gsmart.core.api.pcontract;

import vn.gpay.gsmart.core.base.RequestBase;

public class PContract_getbysearch_request extends RequestBase {
	public String productbuyer_code;
	public String po_code;
	public Integer orgbuyerid_link;
	public Integer orgvendorid_link;
	public String contractbuyer_code;
	public Integer contractbuyer_yearfrom;
	public Integer contractbuyer_yearto;
}
