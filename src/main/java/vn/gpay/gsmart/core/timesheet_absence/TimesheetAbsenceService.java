package vn.gpay.gsmart.core.timesheet_absence;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.api.timesheet_absence.TimeSheetAbsence_getbypaging_request;
import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;

@Service
public class TimesheetAbsenceService extends AbstractService<TimesheetAbsence> implements ITimesheetAbsenceService {
	@Autowired
	ITimesheetAbsenceRepository repo;

	@Override
	protected JpaRepository<TimesheetAbsence, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<TimesheetAbsence> getbypaging(TimeSheetAbsence_getbypaging_request entity) {
		// TODO Auto-generated method stub
		return repo.getbypaging(entity.orgFactory, entity.personnelCode, entity.personnelName,
				GPAYDateFormat.atStartOfDay(entity.datefrom), GPAYDateFormat.atEndOfDay(entity.dateto),
				entity.timeSheetAbsenceType);
	}

	@Override
	public List<TimesheetAbsence> getbyOrg_grant_id_link(Long Org_grant_id_link, Date datefrom, Date dateto,
			String personnelCode, String personnelName, Long timeSheetAbsenceType) {
		// TODO Auto-generated method stub
		return repo.getbyOrg_grant_id_link(Org_grant_id_link, datefrom, dateto, personnelCode, personnelName,
				timeSheetAbsenceType);
	}

	@Override
	public List<TimesheetAbsence> getAllbydate(Long orgFactory, Date datefrom, Date dateto, String personnelCode,
			String personnelName, Long timeSheetAbsenceType) {
		// TODO Auto-generated method stub
		orgFactory = orgFactory == 0 ? null : orgFactory;
		return repo.getAllbydate(orgFactory, datefrom, dateto, personnelCode, personnelName, timeSheetAbsenceType);
	}

	@Override

	public int GetTimeSheetAbsenceByDate(Long Org_grant_id_link, Date today) {
		// TODO Auto-generated method stub
		return repo.getbyOrg_grant_id_link_Today(Org_grant_id_link, today);
	}

	public List<TimesheetAbsence> getbyOrgid(Long orgFactory, Long org_id, Date datefrom, Date dateto,
			String personnelCode, String personnelName, Long timeSheetAbsenceType) {
		// TODO Auto-generated method stub
		return repo.getbyOrgid(orgFactory, org_id, datefrom, dateto, personnelCode, personnelName,
				timeSheetAbsenceType);
	}

	@Override
	public List<TimesheetAbsence> getByOrgAndDate(Long orgid_link, Date date) {
		// TODO Auto-generated method stub
		Date dateto = Common.Date_Add(date, 1);
		return repo.GetByOrgAndDate(dateto, date, orgid_link);
	}

}
