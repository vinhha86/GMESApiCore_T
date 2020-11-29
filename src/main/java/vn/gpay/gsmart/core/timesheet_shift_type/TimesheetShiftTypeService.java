package vn.gpay.gsmart.core.timesheet_shift_type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimesheetShiftTypeService extends AbstractService<TimesheetShiftType> implements ITimesheetShiftTypeService {

	@Autowired TimesheetShiftTypeRepository repo;
	@Override
	protected JpaRepository<TimesheetShiftType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<TimesheetShiftType> getByName(String name) {
		// TODO Auto-generated method stub
		return repo.getByName(name);
	}
	@Override
	public List<TimesheetShiftType> getShift1ForAbsence() {
		// TODO Auto-generated method stub
		return repo.getShift1ForAbsence();
	}

}
