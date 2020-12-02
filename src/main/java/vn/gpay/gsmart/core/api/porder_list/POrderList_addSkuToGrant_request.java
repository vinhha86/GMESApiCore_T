package vn.gpay.gsmart.core.api.porder_list;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class POrderList_addSkuToGrant_request extends RequestBase {
	public List<Long> idSkus;
	public Long idGrant;
	public Long idPOrder;
	public Long idPcontractPo;
}
