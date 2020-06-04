package vn.gpay.gsmart.core.approle;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IAppRole_User_Service extends Operations<AppRole_User> {
	public List<AppRole_User> getby_user_and_role(Long userid, Long roleid_link);
}
