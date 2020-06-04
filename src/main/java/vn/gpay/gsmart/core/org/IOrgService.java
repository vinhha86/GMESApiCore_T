package vn.gpay.gsmart.core.org;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IOrgService extends Operations<Org>{

	public List<Org> findOrgByType(long root_orgid ,long orgid,long type);
	
	public List<Org> findStoreByType(long root_orgid,Long orgid,long type);
	
	public List<Org> findOrgInvCheckByType(long orgid);
	
	public List<Org> findRootOrgInvCheckByType(long orgid);
	
	public List<Org> findOrgAllByRoot(long orgid);
	
	public List<Org> findAllorgByTypeId(int orgtypeid_link, long orgrootid);
	
	public List<Org> findOrgAllByRoot(long orgrootid, long orgid, List<String> list_typeid, boolean isincludeorg);
}
