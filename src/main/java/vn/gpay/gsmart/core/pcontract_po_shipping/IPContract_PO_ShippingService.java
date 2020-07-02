package vn.gpay.gsmart.core.pcontract_po_shipping;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_PO_ShippingService  extends Operations<PContract_PO_Shipping>{

	List<PContract_PO_Shipping> getByPOID(Long pcontract_poid_link);

}
