package vn.gpay.gsmart.core.api.timesheet_absence;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class TimeSheetAbsence_save_request extends RequestBase{
	public Long id;
	public Long personnelid_link;
	public Long absencetypeid_link;
	public String absence_reason;
	public Date absencedate_from;
	public Date absencedate_to;
	public Date timefrom;
	public Date timeto;
}
