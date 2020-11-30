package vn.gpay.gsmart.core.api.timesheet_absence;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class TimeSheetAbsence_getbypaging_request extends RequestBase{
//	limit, page, 
//	orgFactory, personnelCode, personnelName, datefrom, dateto, timeSheetAbsenceType
	public Integer limit;
	public Integer page;
	public Long orgFactory;
	public String personnelCode;
	public String personnelName;
	public Date datefrom;
	public Date dateto;
	public Long timeSheetAbsenceType;
}
