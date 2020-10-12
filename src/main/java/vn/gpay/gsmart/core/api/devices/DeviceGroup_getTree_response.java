package vn.gpay.gsmart.core.api.devices;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.devices.DeviceGroupTree;

public class DeviceGroup_getTree_response extends ResponseBase {
	public List<DeviceGroupTree> children;
}
