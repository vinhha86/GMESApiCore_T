package vn.gpay.gsmart.core.api.porder_list;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;

public class POrderList_saveGrantSkuOnChange_request extends RequestBase{
	public POrderGrant_SKU data;;
	public Long idGrant;
	public Long idPOrder;
	public Long idPcontractPo;
}
