package vn.gpay.gsmart.core.api.users;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUser;


public class UserResponse extends ResponseBase{
	public List<GpayUser> data;
}
