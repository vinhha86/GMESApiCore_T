package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_BasicService extends Operations<OrgSal_Basic> {
	public List<OrgSal_Basic> getall_byorg(long orgid_link);

	OrgSal_Basic getone_byorg(long orgid_link);
}
