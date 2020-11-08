package vn.gpay.gsmart.core.deviceout_d;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDeviceOut_dService extends Operations<DeviceOut_d>{

	List<DeviceOut_d> getByDeviceOut(Long orgrootid_link, Long deviceoutid_link);

}
