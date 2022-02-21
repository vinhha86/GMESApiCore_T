package vn.gpay.gsmart.core.api.timesheetinout;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.timesheetinout.TimeSheetMonth;

public class GetTimeSheetMonthResponse extends ResponseBase {
	public List<TimeSheetMonth> data;
}
