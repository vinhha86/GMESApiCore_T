package vn.gpay.gsmart.core.porder_balance_process;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class POrderBalanceProcessService extends AbstractService<POrderBalanceProcess> implements IPOrderBalanceProcessService{

	@Autowired IPOrderBalanceProcessRepository repo;
	
	@Override
	protected JpaRepository<POrderBalanceProcess, Long> getRepository() {
		return repo;
	}

	@Override
	public List<Long> getPOrderBalanceProcessIdByPorder(Long porderid_link) {
		return repo.getPOrderBalanceProcessIdByPorder(porderid_link);
	}

}
