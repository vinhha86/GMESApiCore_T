package vn.gpay.gsmart.core.api.pcontract;

import vn.gpay.gsmart.core.base.RequestBase;

public class PContract_findByContractcode_request extends RequestBase {
	public String contractcode;
	public Long productid_link;
	public Long pcontractid_link;
}
