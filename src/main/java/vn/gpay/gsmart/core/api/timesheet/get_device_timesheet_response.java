package vn.gpay.gsmart.core.api.timesheet;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.devices.device_timesheet;

public class get_device_timesheet_response extends ResponseBase {
	public List<device_timesheet> data;
}
