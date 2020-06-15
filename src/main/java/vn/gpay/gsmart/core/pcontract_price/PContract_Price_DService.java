package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class PContract_Price_DService extends AbstractService<PContract_Price_D> implements IPContract_Price_DService {
	@Autowired IPContract_Price_DRepository repo;
	@Override
	protected JpaRepository<PContract_Price_D, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<PContract_Price_D> getPrice_D_ByPO(long pcontract_poid_link) {
		// TODO Auto-generated method stub
		return repo.getPrice_D_ByPO(pcontract_poid_link);
	}

}
