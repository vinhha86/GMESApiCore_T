package vn.gpay.gsmart.core.timesheet_sum_col_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheet_Sum_ColType_Service extends AbstractService<TimeSheet_Sum_ColType> implements ITimeSheet_Sum_ColType_Service {
	@Autowired TimeSheet_Sum_ColType_Repo repo;
	@Override
	protected JpaRepository<TimeSheet_Sum_ColType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
