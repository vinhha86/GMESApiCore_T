package vn.gpay.gsmart.core.stockout_type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockoutTypeService  extends AbstractService<StockoutType> implements IStockoutTypeService {
	@Autowired IStockoutTypeRepository repo;
	@Override
	protected JpaRepository<StockoutType, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}

}
