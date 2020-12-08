package vn.gpay.gsmart.core.timesheet_sum;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheet_Sum_Service extends AbstractService<TimeSheet_Sum> implements ITimeSheet_Sum_Service {
	@Autowired TimeSheet_Sum_Repository repo;
	@Override
	protected JpaRepository<TimeSheet_Sum, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<TimeSheet_Sum> getByKey(
			Long personnelid_link,
			Integer year,
			Integer month,
			Integer sumcolid_link			
			){
		return repo.getByKey(personnelid_link, year, month, sumcolid_link);
	}
	
	@Override
	public List<TimeSheet_Sum> getall_bymanageorg(long orgid_link, int year, int month) {
		// TODO Auto-generated method stub
		return repo.getall_bymanageorg(orgid_link, year, month);
	}
}
