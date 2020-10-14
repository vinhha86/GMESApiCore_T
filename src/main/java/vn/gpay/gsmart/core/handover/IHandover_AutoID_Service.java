package vn.gpay.gsmart.core.handover;

import vn.gpay.gsmart.core.base.Operations;

public interface IHandover_AutoID_Service extends Operations<Handover_AutoID> {
	public String getLastID(String Prefix);
}
