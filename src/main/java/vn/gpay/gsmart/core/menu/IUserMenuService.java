package vn.gpay.gsmart.core.menu;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IUserMenuService extends Operations<UserMenu>{

	public List<UserMenu>findByUserid(long userid);
}
