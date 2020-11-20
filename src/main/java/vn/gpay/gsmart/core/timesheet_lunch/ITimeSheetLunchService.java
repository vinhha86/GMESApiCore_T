package vn.gpay.gsmart.core.timesheet_lunch;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimeSheetLunchService  extends Operations<TimeSheetLunch>{
//	public List<TimeSheetLunchBinding> getForTimeSheetLunch(Long orgid_link, Date workingdate);
	
	List<TimeSheetLunch> getForTimeSheetLunch(Long orgid_link, Date workingdate);
	List<TimeSheetLunch> getByPersonnelDateAndShift(Long personnelid_link, Date workingdate, Integer shifttypeid_link);
}
