package vn.gpay.gsmart.core.porder_grant;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;
public interface IPOrderGrant_Service extends Operations<POrderGrant>{

	List<POrderGrant> getByOrderCodeAndOrg(Long granttoorgid_link, String ordercode);

	void deleteByOrderId(Long porderid_link);

}
