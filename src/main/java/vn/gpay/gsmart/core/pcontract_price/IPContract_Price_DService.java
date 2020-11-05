package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_Price_DService extends Operations<PContract_Price_D> {
	public List<PContract_Price_D> getPrice_D_ByPO(long pcontract_poid_link);
	public List<PContract_Price_D> getPrice_D_ByFobPriceAndPContractPrice(Long pcontractpriceid_link, Long fobpriceid_link);
}
