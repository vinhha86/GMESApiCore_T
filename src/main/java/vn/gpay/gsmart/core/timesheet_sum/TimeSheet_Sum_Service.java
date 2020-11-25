package vn.gpay.gsmart.core.timesheet_sum;

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

}
