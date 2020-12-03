package vn.gpay.gsmart.core.porder_grant_timesheet;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderGrantTimesheetService  extends Operations<POrderGrantTimesheet>{
	public List<POrderGrantTimesheet> getByPorderGrantAndPorderBalanceAndPersonnel(
			Long pordergrantid_link,
			Long porderbalanceid_link,
			Long personnelid_link
			);
	public List<POrderGrantTimesheet> getByPorderGrantAndPorderBalance(
			Long pordergrantid_link,
			Long porderbalanceid_link
			);
	List<POrderGrantTimesheet> getByPorderGrant(Long pordergrantid_link);
}
