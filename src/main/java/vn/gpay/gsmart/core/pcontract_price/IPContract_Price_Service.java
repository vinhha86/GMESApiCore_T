package vn.gpay.gsmart.core.pcontract_price;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_Price_Service extends Operations<PContract_Price> {
	public List<PContract_Price> getby_pcontractpo_id_link(long pcontract_poid_link);
}
