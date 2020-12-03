package vn.gpay.gsmart.core.porder_grant_timesheet;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
@Service
public class POrderGrantTimesheetService extends AbstractService<POrderGrantTimesheet> implements IPOrderGrantTimesheetService{
	@Autowired IPOrderGrantTimesheetRepository repo;

	@Override
	protected JpaRepository<POrderGrantTimesheet, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<POrderGrantTimesheet> getByPorderGrantAndPorderBalanceAndPersonnel(Long pordergrantid_link,
			Long porderbalanceid_link, Long personnelid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorderGrantAndPorderBalanceAndPersonnel(pordergrantid_link, porderbalanceid_link, personnelid_link);
	}

	@Override
	public List<POrderGrantTimesheet> getByPorderGrantAndPorderBalance(Long pordergrantid_link,
			Long porderbalanceid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorderGrantAndPorderBalance(pordergrantid_link, porderbalanceid_link);
	}

	@Override
	public List<POrderGrantTimesheet> getByPorderGrant(Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorderGrant(pordergrantid_link);
	}
}
