package vn.gpay.gsmart.core.approle;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IAppRoleFunctionService extends Operations<AppRoleFunction> {
	public List<Long> getfunction_byrole(Long roleid_link);
	public AppRoleFunction getby_role_and_function(Long roleid_link, Long functionid_link);
}
