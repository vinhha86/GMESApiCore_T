package vn.gpay.gsmart.core.org;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IOrgTypeService extends Operations<OrgType>{
	public List<OrgType> findOrgTypeForMenuOrg();
}
