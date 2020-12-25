package vn.gpay.gsmart.core.pcontract_po_productivity;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_PO_Productivity_Service extends Operations<PContract_PO_Productivity>{

	Integer getProductivityByPOAndProduct(Long pcontract_poid_link, Long productid_link);
	List<PContract_PO_Productivity> getbypo(Long pcontract_poid_link);
	List<PContract_PO_Productivity> getbypo_and_product(Long pcontract_poid_link, Long productid_link);
}
