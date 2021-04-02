package vn.gpay.gsmart.core.stockout_order;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Stockout_order_pkl_Service extends AbstractService<Stockout_order_pkl> implements IStockout_order_pkl_Service{
	@Autowired Stockout_order_pkl_repository repo;
	@Override
	protected JpaRepository<Stockout_order_pkl, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
