package vn.gpay.gsmart.core.devicesinvcheck;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDevicesInvCheckService  extends Operations<DevicesInvCheck>{

	public List<DevicesInvCheck> findByOrgCheckId(Long orgcheckid_link);
	
	public List<DevicesInvCheck> findByOrgCheckIdActive(Long orgcheckid_link);
}
