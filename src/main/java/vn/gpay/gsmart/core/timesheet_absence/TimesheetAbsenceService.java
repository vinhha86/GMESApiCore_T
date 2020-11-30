package vn.gpay.gsmart.core.timesheet_absence;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.api.timesheet_absence.TimeSheetAbsence_getbypaging_request;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class TimesheetAbsenceService extends AbstractService<TimesheetAbsence> implements ITimesheetAbsenceService{
	@Autowired ITimesheetAbsenceRepository repo;
	
	@Override
	protected JpaRepository<TimesheetAbsence, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<TimesheetAbsence> getbypaging(TimeSheetAbsence_getbypaging_request entity) {
		// TODO Auto-generated method stub
		return repo.getbypaging(
				entity.orgFactory, entity.personnelCode, entity.personnelName,
				GPAYDateFormat.atStartOfDay(entity.datefrom), GPAYDateFormat.atEndOfDay(entity.dateto), entity.timeSheetAbsenceType
				);
	}

}
