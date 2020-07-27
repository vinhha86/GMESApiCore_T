package vn.gpay.gsmart.core.pcontract;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_AutoID_Service extends Operations<PContract_AutoID> {
	public String getLastID(String Prefix);
}
