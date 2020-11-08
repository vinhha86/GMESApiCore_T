package vn.gpay.gsmart.core.deviceout;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IDeviceOutService extends Operations<DeviceOut>{

	List<DeviceOut> getByCode(Long orgrootid_link, String devicein_code);

	List<DeviceOut> getByOrgFromAndStatus(Long orgrootid_link, Long orgid_from_link, Integer status);

	List<DeviceOut> getByOrgToAndStatus(Long orgrootid_link, Long orgid_to_link, Integer status);

	List<DeviceOut> filterAll(Long orgrootid_link, String deviceout_code, Date deviceout_date_from,
			Date deviceout_date_to, Integer deviceouttypeid_link, String invoice_code, Long orgid_from_link,
			Long orgid_to_link, Integer usercreateid_link, Integer status);

}
