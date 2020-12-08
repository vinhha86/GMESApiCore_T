package vn.gpay.gsmart.core.timesheet_sum;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimeSheet_Sum_Service extends Operations<TimeSheet_Sum> {

	List<TimeSheet_Sum> getByKey(Long personnelid_link, Integer year, Integer month, Integer sumcolid_link);

	List<TimeSheet_Sum> getall_bymanageorg(long orgid_link, int year, int month);

}
