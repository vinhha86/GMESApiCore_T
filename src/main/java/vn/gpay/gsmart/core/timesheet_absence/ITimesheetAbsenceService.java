package vn.gpay.gsmart.core.timesheet_absence;

import java.util.Date;
import java.util.List;


import vn.gpay.gsmart.core.api.timesheet_absence.TimeSheetAbsence_getbypaging_request;
import vn.gpay.gsmart.core.base.Operations;

public interface ITimesheetAbsenceService extends Operations<TimesheetAbsence> {
	public List<TimesheetAbsence> getbypaging(TimeSheetAbsence_getbypaging_request entity);


	int GetTimeSheetAbsenceByDate(Long Org_grant_id_link, Date today);

	public List<TimesheetAbsence> getbyOrgid(Long orgFactory,Long org_id,Date datefrom, Date dateto,String personnelCode,String personnelName,Long timeSheetAbsenceType);
	//lấy danh sách theo tổ - của tài khoản quản lý
	public List<TimesheetAbsence>getbyOrg_grant_id_link(Long Org_grant_id_link,Date datefrom, Date dateto,String personnelCode,String personnelName,Long timeSheetAbsenceType);
	//lấy danh sách theo ngày 
	public List<TimesheetAbsence> getAllbydate(Long orgFactory,Date datefrom, Date dateto,String personnelCode,String personnelName,Long timeSheetAbsenceType);

}
