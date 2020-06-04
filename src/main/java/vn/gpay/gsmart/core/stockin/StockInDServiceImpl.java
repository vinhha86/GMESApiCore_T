package vn.gpay.gsmart.core.stockin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;
@Service
public class StockInDServiceImpl extends AbstractService<StockInD> implements IStockInDService{

	@Autowired
	StockInDRepository repository; 
	@Override
	protected JpaRepository<StockInD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

}
