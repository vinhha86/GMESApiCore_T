package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface IStockOutPklistService extends Operations<StockOutPklist>{

	public List<StockOutPklist> inv_getbyid(long stockoutid_link);

	List<StockOutPklist> getByStockoutdId(Long stockoutdid_link);

	List<StockOutPklist> getDetailStockoutForCheck(Date stockoutdate_from, Date stockoutdate_to);

	List<Object[]> getSUMStockoutForCheck(Date stockoutdate_from, Date stockoutdate_to);

	List<Object[]> getSUMStockoutForProcessAlone(Date stockoutdate_from, Date stockoutdate_to);

	List<StockOutPklist> getDetailStockoutForProcess(Date stockoutdate_from, Date stockoutdate_to);

	List<Object[]> getSUMStockoutForProcess(Date stockoutdate_from, Date stockoutdate_to);

	List<StockOutPklist> getStockoutedByEpc(Long skuid_link, String epc);

	List<StockOutPklist> getAvailableByEpc(String epc);

	List<StockOutPklist> getAvailableFilter(Long skuid_link, Long skutypeid_link);

	Float getStockoutSumBySkuAndOrdercode(Long skuid_link, String ordercode);

	Float getAvailableYdsprocessedSumBySku(Long skuid_link);

	Float getAvailableYdscheckSumBySku(Long skuid_link);

	List<StockOutPklist> getAvailableBySku(Long skuid_link);

	List<StockOutPklist> getByStockoutIdAndStatus(Long stockoutid_link, Integer status);

	List<StockOutPklist> getByStockoutId(Long stockoutid_link);
}
