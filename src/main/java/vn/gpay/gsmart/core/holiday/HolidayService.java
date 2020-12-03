package vn.gpay.gsmart.core.holiday;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class HolidayService extends AbstractService<Holiday> implements IHolidayService {
	@Autowired Holiday_repository repo;
	@Override
	protected JpaRepository<Holiday, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<Holiday> getby_year(long orgrootid_link, int year) {
		// TODO Auto-generated method stub
		return repo.getby_year(orgrootid_link, year);
	}
	@Override
	public List<Integer> getAllYears(int year) {
		// TODO Auto-generated method stub
		return repo.getAllYears(year);
	}
	@Override
	public List<Holiday> getby_date(Date day, Date dayto) {
		// TODO Auto-generated method stub
		List<Holiday> list = new ArrayList<Holiday>();
		list = repo.getby_date(day, dayto);
		return list;
	}
	@Override
	public List<Holiday> getby_many_year(long orgrootid_link, List<Integer> year) {
		// TODO Auto-generated method stub
		return repo.getby_many_year(orgrootid_link, year);
	}
	@Override
	public List<Holiday> getByDate(Date day) {
		return repo.getByDate(day);
	}

}
