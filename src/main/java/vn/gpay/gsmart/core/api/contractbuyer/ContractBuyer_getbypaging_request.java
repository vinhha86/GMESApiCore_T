package vn.gpay.gsmart.core.api.contractbuyer;

import java.util.Date;

import vn.gpay.gsmart.core.base.RequestBase;

public class ContractBuyer_getbypaging_request extends RequestBase {
	public String contract_code;
	public Integer contract_year;
	public Date contract_datefrom;
	public Date contract_dateto;
	public long buyerid_link;
	public long vendorid_link;
	public int limit;
	public int page;
}
