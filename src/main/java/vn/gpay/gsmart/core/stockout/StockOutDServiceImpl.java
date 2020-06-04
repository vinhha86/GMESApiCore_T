package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import vn.gpay.gsmart.core.base.AbstractService;
@Service
public class StockOutDServiceImpl extends AbstractService<StockOutD> implements IStockOutDService{

	@Autowired
	StockOutDRepository repository; 
	@Override
	protected JpaRepository<StockOutD, Long> getRepository() {
		// TODO Auto-generated method stub
		return repository;
	}
	@Override
	public List<StockOutD>getByDateAndType(Integer stockouttypeid_link, Date stockoutdate_from, Date stockoutdate_to){
		return repository.getByDateAndType(stockouttypeid_link,stockoutdate_from,stockoutdate_to);
	}

	@Override
	public List<StockOutD>getByStockoutId(Long stockoutid_link){
		return repository.getByStockoutId(stockoutid_link);
	}

	@Override
	public List<StockOutD>getByDate(Date stockoutdate_from, Date stockoutdate_to){
		return repository.getByDate(stockoutdate_from, stockoutdate_to);
	}

	@Override
	public List<StockOutD>getByDateAndSkucode(Integer stockouttypeid_link, Date stockoutdate_from, Date stockoutdate_to, Long skuid_link){
		return repository.getByDateAndSkucode(stockouttypeid_link, stockoutdate_from, stockoutdate_to, skuid_link);
	}

}
