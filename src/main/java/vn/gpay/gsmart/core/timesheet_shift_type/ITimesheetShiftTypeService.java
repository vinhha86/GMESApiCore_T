package vn.gpay.gsmart.core.timesheet_shift_type;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimesheetShiftTypeService extends Operations<TimesheetShiftType>{
	public List<TimesheetShiftType> getByName(String name);
}
