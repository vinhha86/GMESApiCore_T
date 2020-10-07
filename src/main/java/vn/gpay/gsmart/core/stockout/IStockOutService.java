package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.base.Operations;



public interface IStockOutService extends Operations<StockOut>{

	public List<StockOut> findByStockinCode(Long orgid_link,String stockoutcode);
	
	public List<Results> test(String stockoutcode);
	
	public List<StockOut> stockout_getone(Long orgid_link,String stockoutcode,String stockcode);
	
	public List<StockOut> stockout_list(Long orgid_link,Integer stockouttypeid_link,String stockoutcode,Long orgid_to_link,Date stockoutdate_from,Date stockoutdate_to);
	
	public List<StockOut> stockout_listByOrgTo(Integer stockouttypeid_link, long orgid_to_link);
	
	public void updateStatusById(long id);
	
	public  Page<StockOut> stockout_list_page(Long orgrootid_link,Integer stockouttypeid_link,String stockoutcode, Long orgid_from_link,Long orgid_to_link, Date stockoutdate_from,
				Date stockoutdate_to,int page, int limit);
}
