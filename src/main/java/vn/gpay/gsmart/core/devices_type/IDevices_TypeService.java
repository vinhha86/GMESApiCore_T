package vn.gpay.gsmart.core.devices_type;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IDevices_TypeService extends Operations<Devices_Type>{
	List<Devices_Type> loadDivicesType();
}
