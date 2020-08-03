package vn.gpay.gsmart.core.api.devices;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.devices.DeviceGroup;

public class DeviceGroupRequest extends RequestBase{
	public DeviceGroup data; // create, save
	public Long id; // delete
}
