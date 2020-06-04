package vn.gpay.gsmart.core.stockout;

import java.util.Date;
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
	@Override
	public List<StockOutPklist>getByStockoutdId(Long stockoutdid_link){
		return repository.getByStockoutdId(stockoutdid_link);
	}
	
	@Override
	public List<StockOutPklist>getByStockoutId(Long stockoutid_link){
		return repository.getByStockoutId(stockoutid_link);
	}
	
	@Override
	public List<StockOutPklist>getByStockoutIdAndStatus(Long stockoutid_link, Integer status){
		return repository.getByStockoutIdAndStatus(stockoutid_link, status);
	}
	
	@Override
	public List<StockOutPklist>getAvailableBySku(Long skuid_link){
		return repository.getAvailableBySku(skuid_link);
	}
	
	@Override
	public Float getAvailableYdscheckSumBySku(Long skuid_link){
		return repository.getAvailableYdscheckSumBySku(skuid_link);
	}

	@Override
	public Float getAvailableYdsprocessedSumBySku(Long skuid_link){
		return repository.getAvailableYdsprocessedSumBySku(skuid_link);
	}

	@Override
	public Float getStockoutSumBySkuAndOrdercode(Long skuid_link, String ordercode){
		return repository.getStockoutSumBySkuAndOrdercode(skuid_link, ordercode);
	}

	@Override
	public List<StockOutPklist>getAvailableFilter(Long skuid_link,Long skutypeid_link){
		return repository.getAvailableFilter(skuid_link, skutypeid_link);
	}

	@Override
	public List<StockOutPklist>getAvailableByEpc(String epc){
		return repository.getAvailableByEpc(epc);
	}

	@Override
	public List<StockOutPklist>getStockoutedByEpc(Long skuid_link, String epc){
		return repository.getStockoutedByEpc(skuid_link, epc);
	}

	@Override
	public List<Object[]> getSUMStockoutForProcess(Date stockoutdate_from,  Date stockoutdate_to){
		return repository.getSUMStockoutForProcess(stockoutdate_from, stockoutdate_to);
	}
	
	@Override
	public List<StockOutPklist> getDetailStockoutForProcess( Date stockoutdate_from, Date stockoutdate_to){
		return repository.getDetailStockoutForProcess(stockoutdate_from, stockoutdate_to);
	}

	@Override
	public List<Object[]> getSUMStockoutForProcessAlone(Date stockoutdate_from, Date stockoutdate_to){
		return repository.getSUMStockoutForProcessAlone(stockoutdate_from, stockoutdate_to);
	}

	@Override
	public List<Object[]> getSUMStockoutForCheck(Date stockoutdate_from, Date stockoutdate_to){
		return repository.getSUMStockoutForCheck(stockoutdate_from, stockoutdate_to);
	}

	@Override
	public List<StockOutPklist> getDetailStockoutForCheck(Date stockoutdate_from, Date stockoutdate_to){
		return repository.getDetailStockoutForCheck(stockoutdate_from, stockoutdate_to);
	}
}
