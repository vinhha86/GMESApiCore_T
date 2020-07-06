package vn.gpay.gsmart.core.holiday;

import java.util.List;

import vn.gpay.gsmart.core.base.Operations;

public interface IHolidayService extends Operations<Holiday>{
	public List<Holiday> getby_year(long orgrootid_link, int year);
	
	public List<Integer> getAllYears();
}