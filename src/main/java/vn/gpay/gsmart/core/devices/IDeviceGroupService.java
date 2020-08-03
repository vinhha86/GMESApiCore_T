package vn.gpay.gsmart.core.devices;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDeviceGroupService extends Operations<DeviceGroup>{
	public List<DeviceGroup> findAllByOrderByIdAsc();
}
