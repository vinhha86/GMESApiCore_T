package vn.gpay.gsmart.core.api.timesheetinout;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.timesheetinout.TimeSheetDaily;

public class getDailyResponse extends ResponseBase{
	public List<TimeSheetDaily> data;
}
