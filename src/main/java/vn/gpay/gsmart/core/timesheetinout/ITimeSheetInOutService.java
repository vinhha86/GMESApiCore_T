package vn.gpay.gsmart.core.timesheetinout;

import java.sql.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimeSheetInOutService extends Operations<TimeSheetInOut>{
	public List<TimeSheetInOut> getAll(Date todate,Date fromdate);
}
