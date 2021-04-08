package vn.gpay.gsmart.core.stockout_order;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class Stockout_order_coloramount_Service extends AbstractService<Stockout_order_coloramount> implements IStockout_order_coloramount_Service {
	@Autowired Stockout_order_coloramount_Repository repo;
	@Override
	protected JpaRepository<Stockout_order_coloramount, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Stockout_order_coloramount> getby_stockout_Order(Long stockoutorderid_link) {
		// TODO Auto-generated method stub
		return repo.getby_stockout_order(stockoutorderid_link);
	}

}
