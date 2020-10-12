package vn.gpay.gsmart.core.devices;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDevicesService extends Operations<Devices>{

	public List<Devices> device_list(Long orgrootid_link,Long org_governid_link,String search, boolean disable);
	
	public List<Devices> device_govern(Long orgid_link, Long type);
	
	public Devices finByCode(String deviceid);

	Devices finByEPC(String epc);
	
	List<Devices> findByOrg(long org_governid_link);
	
	public List<Devices> findByDeviceGroup(long devicegroupid_link);

	Devices finByOrgEPC(Long org_governid_link, String epc);
}
