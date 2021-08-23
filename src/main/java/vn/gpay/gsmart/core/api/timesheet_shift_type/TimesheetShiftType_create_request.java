package vn.gpay.gsmart.core.api.timesheet_shift_type;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class TimesheetShiftType_create_request extends RequestBase{
	public Long id;
	public String name;
	public Date timefrom;
	public Date timeto;
	public boolean checkboxfrom;
	public boolean checkboxto;
	public Long orgid_link;
}
