package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface IOrgSal_ComService extends Operations<OrgSal_Com> {
	public List<OrgSal_Com> getall_byorg(long orgid_link);
}
