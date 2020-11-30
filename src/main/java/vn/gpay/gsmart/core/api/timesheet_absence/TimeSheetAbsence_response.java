package vn.gpay.gsmart.core.api.timesheet_absence;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.timesheet_absence.TimesheetAbsence;

public class TimeSheetAbsence_response extends ResponseBase{
	public List<TimesheetAbsence> data;
	public Integer totalCount;
}
