package vn.gpay.gsmart.core.api.timesheet_lunch;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class TimeSheetLunch_updateStatus_request extends RequestBase {
	public Long orgid_link;
	public Date workingdate;
	public Integer status;
}
