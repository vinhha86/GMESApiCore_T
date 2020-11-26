package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface ISalary_Sum_POrdersService extends Operations<Salary_Sum_POrders> {
	public List<Salary_Sum_POrders> getall_byorg(long orgid_link, int year, int month);
	
	void saveWithCheck(Salary_Sum_POrders entity);

	List<Salary_Sum_POrders> getall_bypersonnel(long personnelid_link, int year, int month);
}
