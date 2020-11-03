package vn.gpay.gsmart.core.api.users;

import java.util.List;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUserOrg;

public class User_OrgView_Response extends ResponseBase{
	public List<GpayUserOrg> data;
}
