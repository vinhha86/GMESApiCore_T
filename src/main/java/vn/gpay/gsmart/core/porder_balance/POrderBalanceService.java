package vn.gpay.gsmart.core.porder_balance;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderBalanceService extends AbstractService<POrderBalance> implements IPOrderBalanceService{
	
	@Autowired IPOrderBalanceRepository repo;

	@Override
	protected JpaRepository<POrderBalance, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

	@Override
	public List<POrderBalance> getByPorder(Long porderid_link) {
		// TODO Auto-generated method stub
		return repo.getByPorder(porderid_link);
	}
}
