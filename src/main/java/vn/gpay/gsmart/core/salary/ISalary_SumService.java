package vn.gpay.gsmart.core.salary;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;


public interface ISalary_SumService extends Operations<Salary_Sum> {
	public List<Salary_Sum> getall_byorg(long orgid_link, int year, int month);

	void saveWithCheck(Salary_Sum entity);
}
