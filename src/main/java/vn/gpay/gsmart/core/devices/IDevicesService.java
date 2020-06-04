package vn.gpay.gsmart.core.devices;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDevicesService extends Operations<Devices>{

	public List<Devices> device_listtree(Long orgid_link,Long org_governid_link,String search);
	
	public List<Devices> device_govern(Long orgid_link, int type);
	
	public Devices finByCode(String deviceid);
}
