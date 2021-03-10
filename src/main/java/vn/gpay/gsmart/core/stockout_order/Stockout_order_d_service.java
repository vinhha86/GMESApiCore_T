package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Stockout_order_d_service extends AbstractService<Stockout_order_d> implements IStockout_order_d_service {
	@Autowired Stockout_order_d_repository repo;
	@Override
	protected JpaRepository<Stockout_order_d, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Stockout_order_d> getby_Stockout_order(Long Stockout_order_id_link) {
		// TODO Auto-generated method stub
		return repo.getby_stockout_order(Stockout_order_id_link);
	}

}
