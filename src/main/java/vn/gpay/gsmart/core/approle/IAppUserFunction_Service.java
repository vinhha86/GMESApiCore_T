package vn.gpay.gsmart.core.approle;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IAppUserFunction_Service extends Operations<AppUserFunction> {
	public List<AppUserFunction> getby_function_and_user(Long functionid_link, Long userid_link);
}
