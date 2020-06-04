package vn.gpay.gsmart.core.approle;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IAppRoleMenuService extends Operations<AppRoleMenu> {
	public List<String> getmenuid_byrole(Long roleid_link);
	public AppRoleMenu getRoleMenu_by_MenuAndRole(String menuid_link, long roleid_link);
}
