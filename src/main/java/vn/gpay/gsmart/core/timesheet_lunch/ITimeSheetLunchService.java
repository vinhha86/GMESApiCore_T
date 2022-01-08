package vn.gpay.gsmart.core.timesheet_lunch;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimeSheetLunchService  extends Operations<TimeSheetLunch>{
//	public List<TimeSheetLunchBinding> getForTimeSheetLunch(Long orgid_link, Date workingdate);
	
	List<TimeSheetLunch> getForTimeSheetLunch(Long orgid_link, Date workingdate);
	List<TimeSheetLunch> getForTimeSheetLunchBeforeDate(Long orgid_link, Date workingdate);
	List<TimeSheetLunch> getForTimeSheetLunchByGrant(Long orgid_link, Date workingdate);
	List<TimeSheetLunch> getForTimeSheetLunchByGrantManyDay(Long orgid_link, Date date_from, Date date_to);
	List<TimeSheetLunch> getByPersonnelDateAndShift(Long personnelid_link, Date workingdate, Integer shifttypeid_link);
	List<TimeSheetLunch> getByPersonnelDate(Long personnelid_link, Date workingdate_start, Date workingdate_end);
	List<TimeSheetLunch> getForUpdateStatusTimeSheetLunch(Long orgid_link, Date workingdate);
	List<TimeSheetLunch> getForTimeSheetLunch_byOrg_Date(Long orgid_link, Date workingdate);
	List<TimeSheetLunch> getByConfirmStatus(Long timesheetShiftTypeOrg_id,Long orgid_link, Date workingdate, Integer status);
	List<TimeSheetLunch> getBy_multiShift(Long orgid_link, Date workingdate, List<Long> listIds);
	List<TimeSheetLunch> getBy_isworking_islunch(Boolean isworking, Boolean islunch);
	List<TimeSheetLunch> getByOrg_Shift(Long orgid_link,Integer shifttypeid_link,Date workingdate);
	List<TimeSheetLunch> getByOrg_Shift_Them(Long orgid_link,Integer shifttypeid_link,Date workingdate);
	List<TimeSheetLunch> getByOrg_Shift_DangKy(Long orgid_link,Integer shifttypeid_link,Date workingdate);
}
