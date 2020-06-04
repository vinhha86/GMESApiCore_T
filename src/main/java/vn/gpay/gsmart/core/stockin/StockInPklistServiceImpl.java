package vn.gpay.gsmart.core.stockin;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class StockInPklistServiceImpl extends AbstractService<StockInPklist> implements IStockInPklistService{

	@Autowired
	StockInPklistRepository repository; 
	
	@Override
	protected JpaRepository<StockInPklist, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}

	@Override
	public List<StockInPklist> inv_getbyid(long stockinid_link) {
		// TODO Auto-generated method stub
		return repository.inv_getbyid(stockinid_link);
	}
	

}
