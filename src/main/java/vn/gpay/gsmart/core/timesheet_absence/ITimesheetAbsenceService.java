package vn.gpay.gsmart.core.timesheet_absence;

import java.util.List;

import vn.gpay.gsmart.core.api.timesheet_absence.TimeSheetAbsence_getbypaging_request;
import vn.gpay.gsmart.core.base.Operations;

public interface ITimesheetAbsenceService extends Operations<TimesheetAbsence>{
	public List<TimesheetAbsence> getbypaging(TimeSheetAbsence_getbypaging_request entity);
	public List<TimesheetAbsence> getbyOrgid(Long org_id);
}
