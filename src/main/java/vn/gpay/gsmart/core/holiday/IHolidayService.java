package vn.gpay.gsmart.core.holiday;

import java.util.Date;
import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHolidayService extends Operations<Holiday>{
	public List<Holiday> getby_year(long orgrootid_link, int year);
	
	public List<Holiday> getby_many_year(long orgrootid_link, List<Integer> year);
	
	public List<Integer> getAllYears(int year);
	
	public List<Holiday> getby_date(Date day, Date dayto);
}
