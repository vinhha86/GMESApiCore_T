package vn.gpay.gsmart.core.timesheet_sum_col;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheet_Sum_Col_Service extends AbstractService<TimeSheet_Sum_Col> implements ITimeSheet_Sum_Col_Service {
	@Autowired TimeSheet_Sum_Col_Repo repo;
	@Override
	protected JpaRepository<TimeSheet_Sum_Col, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
