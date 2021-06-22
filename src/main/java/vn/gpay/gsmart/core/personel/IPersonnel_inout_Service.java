package vn.gpay.gsmart.core.personel;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IPersonnel_inout_Service extends Operations<Personnel_inout> {
	List<Personnel_inout> getlist_not_checkout(String personnel_code);
	List<Personnel_inout> getby_bikenumber_and_timein(String bike_number, Date timein);
}
