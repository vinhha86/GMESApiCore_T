package vn.gpay.gsmart.core.api.porder_list;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;

public class POrderList_getPOrderGrantSKUbyGrantId_response extends ResponseBase{
	public List<POrderGrant_SKU> data;
}
