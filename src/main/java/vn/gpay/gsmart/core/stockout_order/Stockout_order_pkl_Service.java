package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

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
	@Override
	public List<Stockout_order_pkl> getby_detail(Long stockout_orderdid_link) {
		// TODO Auto-generated method stub
		return repo.getby_detail(stockout_orderdid_link);
	}

}
