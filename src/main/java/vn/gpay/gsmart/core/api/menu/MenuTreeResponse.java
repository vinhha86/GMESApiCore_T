package vn.gpay.gsmart.core.api.menu;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.menu.MenuTree;
import vn.gpay.gsmart.core.security.GpayUser;

public class MenuTreeResponse extends ResponseBase{
	public List<MenuTree> children;
	public GpayUser data;
//	public MenuTree data;
}
