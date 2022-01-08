package vn.gpay.gsmart.core.timesheet_lunch_khach;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheetLunchKhachService extends AbstractService<TimeSheetLunchKhach>
		implements ITimeSheetLunchKhachService {
	@Autowired
	ITimeSheetLunchLKhachRepository repo;

	@Override
	protected JpaRepository<TimeSheetLunchKhach, Long> getRepository() {
		return repo;
	}

	@Override
	public List<TimeSheetLunchKhach> getbyCa_ngay_org(Long shifttypeid_link, Date day, Long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getByDate_Ca_Org(shifttypeid_link, day, orgid_link);
	}

	@Override
	public List<TimeSheetLunchKhach> getby_ngay_org(Date day, Long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getByDate_Org(day, orgid_link);
	}

	@Override
	public List<TimeSheetLunchKhach> getby_nhieungay_org(Date date_from, Date date_to, Long orgid_link) {
		// TODO Auto-generated method stub
		return repo.getByManyDate_Org(date_from, date_to, orgid_link);
	}

}
