package vn.gpay.gsmart.core.devices;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDeviceGroupService extends Operations<DeviceGroup> {

	public List<DeviceGroupTree> createTree( List<DeviceGroup> nodes);
	
	public void deleteDeviceGroupById(long id);
	
	public void deleteDeviceGroupByParentId(long parentid_link);
	
	public List<DeviceGroup> findByParentId(long parentid_link);
	
	public List<DeviceGroup> findAllParent();
}
