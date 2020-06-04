package vn.gpay.gsmart.core.approle;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IAppUserMenu_Service extends Operations<AppUserMenu>{
	public List<AppUserMenu> getuser_menu_by_menuid_and_userid(String menuid_link, long userid);
}
