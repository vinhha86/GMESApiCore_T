package vn.gpay.gsmart.core.api.holiday;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.holiday.Holiday;

public class Holiday_save_request extends RequestBase{
	public Holiday data;
	public Long time;
	public Long timeto;
}
