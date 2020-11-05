package vn.gpay.gsmart.core.api.pcontract_price;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.fob_price.FOBPrice;

public class PContractPriceD_createPContractPriceD_request extends RequestBase{
	public List<FOBPrice> data;
	public Long pcontract_poid_link;
}
