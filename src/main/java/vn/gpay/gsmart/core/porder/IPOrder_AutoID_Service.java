package vn.gpay.gsmart.core.porder;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrder_AutoID_Service extends Operations<POrder_AutoID> {
	public String getLastID(String Prefix);
}
