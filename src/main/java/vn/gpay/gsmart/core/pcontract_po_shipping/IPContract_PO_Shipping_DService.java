package vn.gpay.gsmart.core.pcontract_po_shipping;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_PO_Shipping_DService extends Operations<PContract_PO_Shipping_D>{

	List<PContract_PO_Shipping_D> getByShippingID(Long pcontract_po_shippingid_link);

}
