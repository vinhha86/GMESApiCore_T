package vn.gpay.gsmart.core.api.timesheet_lunch;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.timesheet_shift_type_org.TimesheetShiftTypeOrg;

import java.util.Date;
import java.util.List;

public class TimeSheetLunch_request extends RequestBase{
	public Long orgid_link;
	public Date date;
	public Long timesheet_shift_type_id_link;
	public Long timesheet_shift_type_org_id_link;
	public List<TimesheetShiftTypeOrg> listCa;
}
