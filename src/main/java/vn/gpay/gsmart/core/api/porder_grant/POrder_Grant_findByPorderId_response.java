package vn.gpay.gsmart.core.api.porder_grant;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;

public class POrder_Grant_findByPorderId_response extends ResponseBase{
	public List<POrderGrant> data;
}
