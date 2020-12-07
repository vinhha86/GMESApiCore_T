package vn.gpay.gsmart.core.timesheet;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimeSheet_Service extends Operations<TimeSheet> {
	
	List<TimeSheetBinding> getForRegisterCodeCountChart(
			Date tenDaysAgo, Date today
			);

	List<TimeSheet> getByTime(String register_code, Date datefrom, Date dateto);
	
}
