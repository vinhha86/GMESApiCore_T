package vn.gpay.gsmart.core.reports;

import java.util.List;

public interface ISalaryFund_Service {

	List<SalaryFund_Data> getData_ByMonth(Long userrootorgid_link, Long userorgid_link, int month, int year, int reportmonths);

}
