package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface IStockOutService extends Operations<StockOut>{

	public List<StockOut> findByStockinCode(Long orgid_link,String stockoutcode);
	
	public List<Results> test(String stockoutcode);
	
	public List<StockOut> stockout_getone(Long orgid_link,String stockoutcode,String stockcode);
	
	public List<StockOut> stockout_list(Long orgid_link,Integer stockouttypeid_link,String stockoutcode,Long orgid_to_link,Date stockoutdate_from,Date stockoutdate_to);
	
	public List<StockOut> stockout_listByOrgTo(Integer stockouttypeid_link, long orgid_to_link);
	
	public void updateStatusById(long id);
	
	 List<StockOut> stockout_list_test(Long orgid_link,Integer stockouttypeid_link,String stockoutcode, Long orgid_from_link,Long orgid_to_link, Date stockoutdate_from,
				Date stockoutdate_to,int status);

	List<StockOut> getAll();

	List<StockOut> getBySkucode(Integer stockouttypeid_link, String p_skucode);

	List<StockOut> getByDateAndSkucode(Date stockoutdate, Integer stockouttypeid_link, String p_skucode);

	List<StockOut> getByDate(Integer stockouttypeid_link, Date timecreate_from, Date timecreate_to);

	List<StockOut> getByTypeAndOrderCode(Integer stockouttypeid_link, String ordercode);

	List<StockOut> getByDateAndSkuID(Date stockoutdate, Integer stockouttypeid_link, Long p_skuid_link);

	List<StockOut> getByTypeAndOrderID(Integer stockouttypeid_link, Long stockoutorderid_link);
}
