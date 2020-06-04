package vn.gpay.gsmart.core.approle;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IAppFunctionService extends Operations<AppFunction>{
	public List<AppFunction> getAppFunction_byMenu(String menuid_link);
	public List<AppFunction> getAppFunction_inmenu(String menuid_link, long roleid_link);
}
