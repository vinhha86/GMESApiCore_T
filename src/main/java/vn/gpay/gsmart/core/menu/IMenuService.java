package vn.gpay.gsmart.core.menu;

import java.util.List;

import vn.gpay.gsmart.core.base.StringOperations;



public interface IMenuService extends StringOperations<Menu>{
	
	public List<Menu>findByUserid(long userid);
	public List<MenuTree> createTree( List<Menu> nodes);
	public List<Menu> getby_parentid(String menu_parentid);
	public List<Menu> getMenu_byRole(long roleid_link);
	public List<Menu> getListMenu();
}
