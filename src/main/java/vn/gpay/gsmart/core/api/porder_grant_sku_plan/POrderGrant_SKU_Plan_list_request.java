package vn.gpay.gsmart.core.api.porder_grant_sku_plan;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderGrant_SKU_Plan_list_request extends RequestBase{
	public Long porder_grant_skuid_link;
	public Date dateFrom;
	public Date dateTo;
}
