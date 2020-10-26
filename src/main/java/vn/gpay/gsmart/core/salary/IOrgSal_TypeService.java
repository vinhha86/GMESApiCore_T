package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_TypeService extends Operations<OrgSal_Type> {
	public List<OrgSal_Type> getall_byorg(Long orgid_link, Integer typeid_link);
}
