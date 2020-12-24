package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_Price_D_SKUService  extends Operations<PContract_Price_D_SKU>{
	public List<PContract_Price_D_SKU> getPrice_D_SKU_ByPO(Long pcontract_poid_link);
}
