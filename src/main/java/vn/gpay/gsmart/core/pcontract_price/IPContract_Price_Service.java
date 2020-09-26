package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_Price_Service extends Operations<PContract_Price> {
	public List<PContract_Price> getPrice_ByPO(long pcontract_poid_link);

	public PContract_Price getPrice_CMP(long pcontract_poid_link, long productid_link);
	
	public List<PContract_Price> getPrice_by_product(long pcontract_poid_link, long productid_link);
	
	public List<PContract_Price> getBySizesetNotAll(long pcontract_poid_link);
}
