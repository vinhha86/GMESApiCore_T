package vn.gpay.gsmart.core.security;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IGpayUserOrgService extends Operations<GpayUserOrg>{

	List<GpayUserOrg> getall_byuser(Long userid_link);

	List<GpayUserOrg> getby_user_org(Long userid_link, Long orgid_link);

	List<GpayUserOrg> getall_byuser_andtype(Long userid_link, Integer orgtypeid_link);
	
	List<GpayUserOrg> getall_byuser_andtypelist(Long userid_link, List<Integer> orgtypeid_link_list);

}
