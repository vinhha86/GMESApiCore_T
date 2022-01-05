package vn.gpay.gsmart.core.porder_grant;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderGrant_NoLink_Service extends Operations<POrderGrant_NoLink> {

	List<POrderGrant_NoLink> getPOrderGrantBySearch(String stylebuyer, String pobuyer, Long buyerid, Long vendorid,
			String contractcode);
}
