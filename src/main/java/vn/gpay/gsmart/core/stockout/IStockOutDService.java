package vn.gpay.gsmart.core.stockout;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IStockOutDService extends Operations<StockOutD>{

	List<StockOutD> getByDateAndType(Integer stockouttypeid_link, Date stockoutdate_from, Date stockoutdate_to);

	List<StockOutD> getByStockoutId(Long stockoutid_link);

	List<StockOutD> getByDate(Date stockoutdate_from, Date stockoutdate_to);

	List<StockOutD> getByDateAndSkucode(Integer stockouttypeid_link, Date stockoutdate_from, Date stockoutdate_to,
			Long skuid_link);

}
