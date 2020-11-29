package vn.gpay.gsmart.core.timesheet_absence;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimesheetAbsenceTypeService extends AbstractService<TimesheetAbsenceType> implements ITimesheetAbsenceTypeService{
	@Autowired ITimesheetAbsenceTypeRepository repo;
	
	@Override
	protected JpaRepository<TimesheetAbsenceType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
