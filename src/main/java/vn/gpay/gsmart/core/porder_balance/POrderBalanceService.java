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
		return repo;
	}

	@Override
	public List<POrderBalance> getByPorder(Long porderid_link) {
		return repo.getByPorder(porderid_link);
	}

	@Override
	public List<POrderBalance> getByPOrderAndPOrderSewingCost(Long porderid_link, Long pordersewingcostid_link) {
		return repo.getByPOrderAndPOrderSewingCost(porderid_link, pordersewingcostid_link);
	}

	@Override
	public List<POrderBalance> getByBalanceName_POrder(String balance_name, Long porderid_link) {
		return repo.getByBalanceName_POrder(balance_name, porderid_link);
	}
}
