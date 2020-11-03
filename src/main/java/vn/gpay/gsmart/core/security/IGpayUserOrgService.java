package vn.gpay.gsmart.core.security;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IGpayUserOrgService extends Operations<GpayUserOrg>{

	List<GpayUserOrg> getall_byuser(Long userid_link);

}
