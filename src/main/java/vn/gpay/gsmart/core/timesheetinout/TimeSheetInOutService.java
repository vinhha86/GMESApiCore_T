package vn.gpay.gsmart.core.timesheetinout;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;


@Service
public class TimeSheetInOutService extends AbstractService<TimeSheetInOut> implements ITimeSheetInOutService{
@Autowired ITimeSheetInOutRepository repo;
	@Override
	protected JpaRepository<TimeSheetInOut, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<TimeSheetInOut> getAll(Date todate, Date fromdate) {
		// TODO Auto-generated method stub
		return repo.getAll(todate, fromdate);
	}


}
