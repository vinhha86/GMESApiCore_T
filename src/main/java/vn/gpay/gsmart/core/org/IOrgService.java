package vn.gpay.gsmart.core.org;

import java.util.List;

import org.springframework.data.repository.query.Param;

import vn.gpay.gsmart.core.base.Operations;
import vn.gpay.gsmart.core.org.Org;

public interface IOrgService extends Operations<Org>{

	public List<Org> findOrgByType(long root_orgid ,long orgid,long type);
	
	public List<Org> findStoreByType(long root_orgid,Long orgid,long type);
	
	public List<Org> findOrgInvCheckByType(long orgid);
	
	public List<Org> findRootOrgInvCheckByType(long orgid);
	
	public List<Org> findOrgAllByRoot(long orgid);
	
	public List<Org> findAllorgByTypeId(int orgtypeid_link, long orgrootid);
	
	public List<Org> findOrgAllByRoot(long orgrootid, long orgid, List<String> list_typeid, boolean isincludeorg);
	
	public List<OrgTree> createTree( List<Org> nodes);
	
	public List<Org> findOrgByTypeForMenuOrg();
	
	public List<Org> getorgChildrenbyOrg(long orgid_link, List<String> list_typeid);

	List<Org> findChildByType(long orgrootid_link, long parentid_link, long type);
		
	public List<Org> findOrgByOrgTypeString(List<String> list_typeid, Long parentid_link);
	
	public List<Org> getOrgByPorderIdLink(Long porderid_link);
	
	public List<Org> findOrgByTypeForInvCheckDeviceMenuOrg();
	
	public List<Org> getbycode(String orgcode, Long orgrootid_link);
	
	public List<Org> getByCodeAndType(String code, Integer orgtypeid_link);
	
	public List<Org> getByNameAndType(String name, Integer orgtypeid_link);
	
	List<Org> getOrgForContractBuyerBuyerList(List<Long> buyerIds);
	
	public List<Org> getOrgForVendorStoreByBuyerId(Long buyerid_link);
	
	List<Org> getOrgReq_by_po(Long pcontract_poid_link);

	List<Org> findChildByListType(long orgrootid_link, long parentid_link, List<String> list_typeid);
	
	List<Org> findOrgByTypeKho();
	
	List<Org> findOrgByOrgType(List<Integer> orgtypestring);

	List<Org> findOrgByOrgType(List<Integer> orgtypestring, Long parentid_link);
}
