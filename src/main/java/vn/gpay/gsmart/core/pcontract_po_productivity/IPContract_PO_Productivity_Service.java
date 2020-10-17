package vn.gpay.gsmart.core.pcontract_po_productivity;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_PO_Productivity_Service extends Operations<PContract_PO_Productivity>{

	Integer getProductivityByPOAndProduct(Long pcontract_poid_link, Long productid_link);

}
