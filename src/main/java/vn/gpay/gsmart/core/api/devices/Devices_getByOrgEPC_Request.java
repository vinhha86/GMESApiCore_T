package vn.gpay.gsmart.core.api.devices;

import vn.gpay.gsmart.core.base.RequestBase;

public class Devices_getByOrgEPC_Request extends RequestBase {
	public String epc;
	public Long org_governid_link;
}
