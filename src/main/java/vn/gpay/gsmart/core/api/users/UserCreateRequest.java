package vn.gpay.gsmart.core.api.users;

import java.util.List;

import vn.gpay.gsmart.core.base.RequestBase;
import vn.gpay.gsmart.core.security.GpayUser;

public class UserCreateRequest extends RequestBase{
	public GpayUser data;
	public List<MenuId> usermenu;
	
}
