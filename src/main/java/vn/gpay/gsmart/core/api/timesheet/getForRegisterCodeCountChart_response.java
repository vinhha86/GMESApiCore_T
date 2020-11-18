package vn.gpay.gsmart.core.api.timesheet;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.timesheet.TimeSheetBinding;

public class getForRegisterCodeCountChart_response extends ResponseBase {
	public List<TimeSheetBinding> data;
}
