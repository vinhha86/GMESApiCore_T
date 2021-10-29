package vn.gpay.gsmart.core.timesheet_shift_type_org;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimesheetShiftTypeOrgService extends AbstractService<TimesheetShiftTypeOrg> implements ITimesheetShiftTypeOrgService {

	@Autowired TimesheetShiftTypeOrgRepository repo;
	@Override
	protected JpaRepository<TimesheetShiftTypeOrg, Long> getRepository() {
		return repo;
	}
//	@Override
//	public List<TimesheetShiftTypeOrg> getByName(String name) {
//		// TODO Auto-generated method stub
//		return repo.getByName(name);
//	}
	@Override
	public List<TimesheetShiftTypeOrg> getShift1ForAbsence() {
		return repo.getShift1ForAbsence();
	}
	@Override
	public List<TimesheetShiftTypeOrg> getByOrgid_link(Long orgid_link) {
		return repo.getByOrgid_link(orgid_link);
	}
	@Override
	public List<TimesheetShiftTypeOrg> getByOrgid_link_CaAn(Long orgid_link) {
		return repo.getByOrgid_link_CaAn(orgid_link);
	}

}
