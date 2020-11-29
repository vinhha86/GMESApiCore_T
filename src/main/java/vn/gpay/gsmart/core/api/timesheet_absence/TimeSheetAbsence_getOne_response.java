package vn.gpay.gsmart.core.api.timesheet_absence;

import java.util.Date;

import vn.gpay.gsmart.core.base.ResponseBase;

public class TimeSheetAbsence_getOne_response extends ResponseBase{
	public Long orgFactoryId;
	public Long orgProductionLineId;
	public Long personnelid_link;
	public Date absencedate_from;
	public Date absencedate_to;
	public String absence_reason;
	public Long absencetypeid_link;
	public String timefrom;
	public String timeto;
	public boolean isConfirm;
}
