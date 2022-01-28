package vn.gpay.gsmart.core.pcontract_po;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPContract_PO_NoLink_Service extends Operations<PContract_PO_NoLink> {

	List<PContract_PO_NoLink> getPO_HavetoShip(Long orgrootid_link, Date shipdate_from, Date shipdate_to, Long orgbuyerid_link);
}
