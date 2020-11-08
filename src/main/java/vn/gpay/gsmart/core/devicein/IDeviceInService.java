package vn.gpay.gsmart.core.devicein;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDeviceInService extends Operations<DeviceIn>{

	List<DeviceIn> getByCode(Long orgrootid_link, String devicein_code);

	List<DeviceIn> getByOrgFromAndStatus(Long orgrootid_link, Long orgid_from_link, Integer status);

	List<DeviceIn> getByOrgToAndStatus(Long orgrootid_link, Long orgid_to_link, Integer status);

	List<DeviceIn> getByDeviceOutAndStatus(Long orgrootid_link, Long deviceoutid_link, Integer status);

	List<DeviceIn> filterAll(Long orgrootid_link, String devicein_code, Date devicein_date_from, Date devicein_date_to,
			Integer deviceintypeid_link, String invoice_code, Long deviceoutid_link, Long orgid_from_link,
			Long orgid_to_link, Integer usercreateid_link);

}
