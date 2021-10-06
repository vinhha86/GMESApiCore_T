package vn.gpay.gsmart.core.porder_grant;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPOrderGrant_Service extends Operations<POrderGrant> {

	List<POrderGrant> getByOrderCodeAndOrg(Long granttoorgid_link, String ordercode);

	void deleteByOrderId(Long porderid_link);

	List<POrderGrant> get_granted_bygolivedate(Date golivedate_from, Date golivedate_to, Long granttoorgid_link,
			String POBuyer, String contractcode, Long orgbuyerid_link, Long orgvendorid_link);

	POrderGrant getByOrderIDAndOrg(Long granttoorgid_link, Long porderid_link);

	List<POrderGrant> get_porder_test(Date golivedate_from, Date golivedate_to, Long granttoorgid_link, String POBuyer,
			String contractcode, Long orgbuyerid_link, Long orgvendorid_link);

	List<POrderGrant> getByOrderId(Long porderid_link);

	List<POrderGrant> getByOrgId(Long granttoorgid_link);

	List<POrderGrant> getProcessingByOrgId(Long granttoorgid_link);

	List<POrderGrant> getbyporder_andpo(Long porderid_link, Long pcontract_poid_link);

	void deleteAll(POrderGrant pordergrant);

	public List<POrderGrant> getgrant_contain_Day(Date holiday);

	List<POrderGrant> get_grant_change(Long orgid_link);

	public Integer getProductivity_PO(Long pordergrantid_link);

	public List<POrderGrant> getByOfferAndOrg(Long parentpoid_link, List<Long> orgs);
	
	List<Long> getToSXIdByPcontractPO(Long pcontract_poid_link);
}
