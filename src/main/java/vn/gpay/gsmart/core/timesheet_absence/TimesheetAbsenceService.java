package vn.gpay.gsmart.core.timesheet_absence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimesheetAbsenceService extends AbstractService<TimesheetAbsence> implements ITimesheetAbsenceService{
	@Autowired ITimesheetAbsenceRepository repo;
	
	@Override
	protected JpaRepository<TimesheetAbsence, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
