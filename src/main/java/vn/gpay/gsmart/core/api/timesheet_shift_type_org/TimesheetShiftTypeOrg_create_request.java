package vn.gpay.gsmart.core.api.timesheet_shift_type_org;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class TimesheetShiftTypeOrg_create_request extends RequestBase{
	public Long id;
	public Long timesheet_shift_type_id_link;
	public Date timefrom;
	public Date timeto;
	public boolean checkboxfrom;
	public boolean checkboxto;
	public Long orgid_link;
}
