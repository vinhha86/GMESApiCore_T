package vn.gpay.gsmart.core.org;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IOrg_AutoID_Service extends Operations<Org_AutoID>{
	public List<String> getLastID(String Prefix);
}
