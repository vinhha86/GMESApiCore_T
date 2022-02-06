package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_Price_DService extends Operations<PContract_Price_D> {
	public List<PContract_Price_D> getPrice_D_ByPO(long pcontract_poid_link);

	public List<PContract_Price_D> getPrice_D_ByFobPriceAndPContractPrice(Long pcontractpriceid_link,
			Long fobpriceid_link);

	public List<PContract_Price_D> getPrice_D_ByPContractPrice(Long pcontractpriceid_link);

	public List<PContract_Price_D> getbypo_product(Long pcontract_poid_link, Long productid_link);

	public List<PContract_Price_D> getPrice_D_ByFobPriceNameAndPContractPrice(Long pcontractpriceid_link,
			String fobprice_name);

	List<PContract_PriceFOB_Data> getPrice_FOB(Long pcontractid_link, Long pcontract_poid_link);
}
