package vn.gpay.gsmart.core.timesheet_lunch_khach;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheetLunchKhachService extends AbstractService<TimeSheetLunchKhach> implements ITimeSheetLunchKhachService{
	@Autowired ITimeSheetLunchLKhachRepository repo;
	
	@Override
	protected JpaRepository<TimeSheetLunchKhach, Long> getRepository() {
		return repo;
	}

}
