package vn.gpay.gsmart.core.stockin;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import vn.gpay.gsmart.core.base.Operations;



public interface IStockInService extends Operations<StockIn>{

	public List<StockIn> findByStockinCode(Long orgid_link,String stockincode);
	
	public List<StockIn> stockin_getone(Long orgid_link,String stockincode,String stockcode);
	
	public List<StockIn> stocin_list(Long orgid_link,Long stockouttypeid_link,String stockincode,Long orgid_from_link, Long orgid_to_link,Date stockindate_from,Date stockindate_to,int status);
	
	public List<Results> test(String stockincode);
	
	public Page<StockIn> stockin_page(Long orgrootid_link,Long stockintypeid_link,Long orgid_from_link,
			Long orgid_to_link,Date stockindate_from,Date stockindate_to, int limit, int page);
	
	List<StockIn> findByPO_Type_Status(Long pcontract_poid_link, Integer stockintypeid_link, Integer status);
}
