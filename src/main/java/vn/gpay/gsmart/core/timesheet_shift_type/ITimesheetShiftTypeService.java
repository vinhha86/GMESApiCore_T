package vn.gpay.gsmart.core.timesheet_shift_type;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimesheetShiftTypeService extends Operations<TimesheetShiftType> {
	public List<TimesheetShiftType>getTimesheetShiftType_ByIdOrgid_link(Long id);
	public Long getTimesheetShiftTypeID_ByName(String name);
	public List<TimesheetShiftType>getShift_ByIdOrgid_link(Long orgid_link);
}
