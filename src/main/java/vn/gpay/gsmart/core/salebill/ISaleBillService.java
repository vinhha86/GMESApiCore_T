package vn.gpay.gsmart.core.salebill;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;



public interface ISaleBillService extends Operations<SaleBill>{
	
	
	public List<SaleBill> salebill_list(Long orgbillid_link, String billcode,Date salebilldate_from,Date salebilldate_to);
	
}
