package vn.gpay.gsmart.core.reports;

import java.util.List;

public interface ICMP_Service {

	List<CMP_Data> getData_3Month(Long userrootorgid_link, Long userorgid_link, int month, int year, int reportmonths);

}
