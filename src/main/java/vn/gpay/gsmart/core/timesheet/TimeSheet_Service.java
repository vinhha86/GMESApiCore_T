package vn.gpay.gsmart.core.timesheet;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheet_Service extends AbstractService<TimeSheet> implements ITimeSheet_Service{
	@Autowired TimeSheet_repository repo;
	
	@Override
	protected JpaRepository<TimeSheet, Long> getRepository() {
		// TODO Auto-generated method stub
		return repo;
	}
	
//	@Override
//	public List<TimeSheetBinding> getForRegisterCodeCountChart(Date tenDaysAgo, Date today) {
//		
//		List<TimeSheetBinding> data = new ArrayList<TimeSheetBinding>();
//		List<Object[]> objects = repo.getForRegisterCodeCountChart(tenDaysAgo, today);
//		
//		for(Object[] row : objects) {
//			BigInteger count = (BigInteger) row[0];
//			Long c = count.longValue();
//			Date date = (Date) row[1];
//			TimeSheetBinding temp = new TimeSheetBinding();
//			temp.setRegisterCodeCount(c);
//			temp.setRegisterDate(date);
//			data.add(temp);
//		}
//		Collections.sort(data, new Comparator<TimeSheetBinding>() {
//			  public int compare(TimeSheetBinding o1, TimeSheetBinding o2) {
//			      return o1.getRegisterDate().compareTo(o2.getRegisterDate());
//			  }
//			});
//		return data;
//	}
	
	@Override
	public List<TimeSheetBinding> getForRegisterCodeCountChart(
			Date tenDaysAgo, Date today
			) {
		List<TimeSheetBinding> data = new ArrayList<TimeSheetBinding>();
		Map<Date, TimeSheetBinding> mapTmp = new HashMap<>();
		List<Object[]> objects = repo.getForRegisterCodeCountChart(
				tenDaysAgo, today
				);
		
		LocalDate start = tenDaysAgo.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		LocalDate end = today.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
		    // Do your job here with `date`.
			Date dateObj = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
			
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateObj);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			dateObj = cal.getTime();
			
			TimeSheetBinding temp = new TimeSheetBinding();
			temp.setRegisterDate(dateObj);
			temp.setDataDHA(0L);
			temp.setDataNV(0L);
			temp.setDataBN1(0L);
			temp.setDataBN2(0L);
			temp.setDataBN3(0L);
			mapTmp.put(dateObj, temp);
		}
		
		for(Object[] row : objects) {
			Long count = (Long) row[0];
//			Long c = count.longValue();
			Integer year = (Integer) row[1];
			Integer month = (Integer) row[2];
			Integer day = (Integer) row[3];
			Long orgmanagerid_link = (Long) row[4];
			
			// Tao Date obj tu year, month, day
			Calendar cal = Calendar.getInstance();
			cal.set(year, month - 1, day); // month begin at 0, AAAAAAHHHHHHHHHH
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			Date date = cal.getTime();
			
			if(mapTmp.containsKey(date)) { // neu da co ngay, sua
				TimeSheetBinding temp = mapTmp.get(date);
				switch(orgmanagerid_link.toString()) {
					case "2": // DHA
						temp.setDataDHA(count);
						break;
					case "7": // NV
						temp.setDataNV(count);
						break;
					case "8": // BN1
						temp.setDataBN1(count);
						break;
					case "10": // BN2
						temp.setDataBN2(count);
						break;
					case "11": // BN3
						temp.setDataBN3(count);
						break;
				}
				mapTmp.put(date, temp);
			}
//			else { // neu chua co ngay, them
//				TimeSheetBinding temp = new TimeSheetBinding();
//				temp.setRegisterDate(date);
//				temp.setDataDHA(0L);
//				temp.setDataNV(0L);
//				temp.setDataBN1(0L);
//				temp.setDataBN2(0L);
//				temp.setDataBN3(0L);
//				switch(orgmanagerid_link.toString()) {
//					case "2": // DHA
//						temp.setDataDHA(count);
//						break;
//					case "7": // NV
//						temp.setDataNV(count);
//						break;
//					case "8": // BN1
//						temp.setDataBN1(count);
//						break;
//					case "10": // BN2
//						temp.setDataBN2(count);
//						break;
//					case "11": // BN3
//						temp.setDataBN3(count);
//						break;
//				}
//				mapTmp.put(date, temp);
//			}
		}
		data = new ArrayList<TimeSheetBinding>(mapTmp.values());
		Collections.sort(data, new Comparator<TimeSheetBinding>() {
			  public int compare(TimeSheetBinding o1, TimeSheetBinding o2) {
			      return o1.getRegisterDate().compareTo(o2.getRegisterDate());
			  }
			});
		return data;
	}

}
