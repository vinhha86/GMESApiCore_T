package vn.gpay.gsmart.core.timesheet_lunch_khach;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface ITimeSheetLunchKhachService extends Operations<TimeSheetLunchKhach> {
	List<TimeSheetLunchKhach> getbyCa_ngay_org(Long shifttypeid_link, Date day, Long orgid_link);

	List<TimeSheetLunchKhach> getby_ngay_org(Date day, Long orgid_link);
}
