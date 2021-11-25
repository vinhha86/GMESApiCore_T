package vn.gpay.gsmart.core.api.timesheet_lunch;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;

public class TimeSheetLunch_updateStatus_request extends RequestBase {
	public List<Long> selectIds;
	public List<Long> unselectIds;
	public Long orgid_link;
	public Date workingdate;
	public Integer status;
}
