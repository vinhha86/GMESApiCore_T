package vn.gpay.gsmart.core.timesheet;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessingBinding;

@Service
public class TimeSheet_Service extends AbstractService<TimeSheet> implements ITimeSheet_Service{
	@Autowired TimeSheet_repository repo;
	@Override
	protected JpaRepository<TimeSheet, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	@Override
	public List<TimeSheetBinding> getForRegisterCodeCountChart(Date tenDaysAgo, Date today) {
		
		List<TimeSheetBinding> data = new ArrayList<TimeSheetBinding>();
		List<Object[]> objects = repo.getForRegisterCodeCountChart(tenDaysAgo, today);
		
		for(Object[] row : objects) {
			BigInteger count = (BigInteger) row[0];
			Long c = count.longValue();
			Date date = (Date) row[1];
			TimeSheetBinding temp = new TimeSheetBinding();
			temp.setRegisterCodeCount(c);
			temp.setRegisterDate(date);
			data.add(temp);
		}
		Collections.sort(data, new Comparator<TimeSheetBinding>() {
			  public int compare(TimeSheetBinding o1, TimeSheetBinding o2) {
			      return o1.getRegisterDate().compareTo(o2.getRegisterDate());
			  }
			});
		return data;
	}

}
