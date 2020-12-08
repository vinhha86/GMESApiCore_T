package vn.gpay.gsmart.core.porder_grant;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;
public interface IPOrderGrant_SKUService extends Operations<POrderGrant_SKU>{

	List<POrderGrant_SKU> getPOrderGrant_SKU(Long pordergrantid_link);
	
//	POrderGrant_SKU getPOrderGrant_SKUbySKUid_link(Long skuid_link);
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId(Long skuid_link, Long pordergrantid_link);
	public POrderGrant_SKU getPOrderGrant_SKUbySKUid_linkAndGrantId_andPO(Long skuid_link, Long pordergrantid_link, Long pcontract_poid_link);
	public List<POrderGrant_SKU>getByPContractPOAndSKU(Long pcontract_poid_link, Long skuid_link);
	public List<String> getlistmau_by_grant(Long pordergrantid_link);
	public List<POrderGrant_SKU> getlistco_by_grant_andmau(Long pordergrantid_link, long colorid_link);
	public List<String> getlistco(Long porderid_link);
	public POrderGrant_SKU getPOrderGrant_SKUbySKUAndGrantAndPcontractPo(
			Long skuid_link, 
			Long pordergrantid_link, 
			Long pcontract_poid_link
			);
}
