package vn.gpay.gsmart.core.stockingunique;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockingUniqueService extends AbstractService<StockingUniqueCode> implements IStockingUniqueService {
	@Autowired StockingUniqueRepository repo;
	@Override
	protected JpaRepository<StockingUniqueCode, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public StockingUniqueCode getby_type(Integer type) {
		// TODO Auto-generated method stub
		return repo.getby_type(type).get(0);
	}

}
