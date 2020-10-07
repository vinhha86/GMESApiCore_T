package vn.gpay.gsmart.core.stockout;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockOutPklistServiceImpl extends AbstractService<StockOutPklist> implements IStockOutPklistService{

	@Autowired
	StockOutPklistRepository repository; 
	
	@Override
	protected JpaRepository<StockOutPklist, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<StockOutPklist> inv_getbyid(long stockoutid_link) {
		// TODO Auto-generated method stub
		return repository.inv_getbyid(stockoutid_link);
	}
	

}
