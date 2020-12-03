package vn.gpay.gsmart.core.porder_grant_balance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderGrantBalanceService extends AbstractService<POrderGrantBalance> implements IPOrderGrantBalanceService{
	@Autowired IPOrderGrantBalanceRepository repo;

	@Override
	protected JpaRepository<POrderGrantBalance, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<POrderGrantBalance> getByPorderGrantAndPorderBalance(Long pordergrantid_link,
			Long porderbalanceid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorderGrantAndPorderBalance(pordergrantid_link, porderbalanceid_link);
	}
	
	@Override
	public List<POrderGrantBalance> getByPorderGrant(Long pordergrantid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorderGrant(pordergrantid_link);
	}

	@Override
	public List<POrderGrantBalance> getByPorderGrantAndPersonnel(Long pordergrantid_link, Long personnelid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorderGrantAndPersonnel(pordergrantid_link, personnelid_link);
	}

}
