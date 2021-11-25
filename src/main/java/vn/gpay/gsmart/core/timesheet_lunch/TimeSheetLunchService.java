package vn.gpay.gsmart.core.timesheet_lunch;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import vn.gpay.gsmart.core.base.AbstractService;

@Service
public class TimeSheetLunchService extends AbstractService<TimeSheetLunch> implements ITimeSheetLunchService{
	@Autowired ITimeSheetLunchRepository repo;
	
	@Override
	protected JpaRepository<TimeSheetLunch, Long> getRepository() {
		return repo;
	}

	@Override
	public List<TimeSheetLunch> getForTimeSheetLunch(Long orgid_link, Date workingdate) {
		// TODO Auto-generated method stub
		return repo.getForTimeSheetLunch(orgid_link, workingdate);
	}

	@Override
	public List<TimeSheetLunch> getByPersonnelDateAndShift(Long personnelid_link, Date workingdate,
			Integer shifttypeid_link) {
		return repo.getByPersonnelDateAndShift(personnelid_link, workingdate, shifttypeid_link);
	}

	@Override
	public List<TimeSheetLunch> getByPersonnelDate(Long personnelid_link, Date workingdate_start, Date workingdate_end) {
		return repo.getByPersonnelDate(personnelid_link, workingdate_start,workingdate_end);
	}

	@Override
	public List<TimeSheetLunch> getForUpdateStatusTimeSheetLunch(Long orgid_link, Date workingdate) {
		return repo.getForUpdateStatusTimeSheetLunch(orgid_link, workingdate);
	}

	@Override
	public List<TimeSheetLunch> getForTimeSheetLunchByGrant(Long orgid_link, Date workingdate) {
		return repo.getForTimeSheetLunchByGrant(orgid_link, workingdate);
	}

	@Override
	public List<TimeSheetLunch> getForTimeSheetLunch_byOrg_Date(Long orgid_link, Date workingdate) {
		return repo.getForTimeSheetLunch_byOrg_Date(orgid_link, workingdate);
	}

	@Override
	public List<TimeSheetLunch> getByConfirmStatus(Long timesheetShiftTypeOrg_id, Long orgid_link, Date workingdate, Integer status) {
		return repo.getByConfirmStatus(timesheetShiftTypeOrg_id, orgid_link, workingdate, status);
	}

	@Override
	public List<TimeSheetLunch> getBy_isworking_islunch(Boolean isworking, Boolean islunch) {
		return repo.getBy_isworking_islunch(isworking, islunch);
	}

	@Override
	public List<TimeSheetLunch> getBy_multiShift(Long orgid_link, Date workingdate, List<Long> listIds) {
		return repo.getBy_multiShift(orgid_link, workingdate, listIds);
	}
	
//	@Override
//	public List<TimeSheetLunchBinding> getForTimeSheetLunch(Long orgid_link, Date workingdate) {
//		
//		List<TimeSheetLunchBinding> data = new ArrayList<TimeSheetLunchBinding>();
//		Map<Long, TimeSheetLunchBinding> mapTmp = new HashMap<>();
//		List<Object[]> objects = repo.getForTimeSheetLunch(orgid_link, workingdate);
//		
//		for(Object[] row : objects) {
//			System.out.println("-----");
//			System.out.println((Long) row[0]);
//			System.out.println((String) row[1]);
//			System.out.println((String) row[2]);
//			System.out.println((Date) row[3]);
//			System.out.println((Long) row[4]);
//			System.out.println(row[5]);
//			System.out.println(row[6]);
//			
//			boolean isworking, islunch;
//			
//			Long personnelid_link = (Long) row[0];
//			String personnelCode = (String) row[1];
//			String personnelFullname = (String) row[2];
//			Date date = (Date) row[3];
//			Long shifttypeid_link = (Long) row[4] == null ? 0L : (Long) row[4];
//			if(row[5] == null) {
//				isworking = false;
//			}else {
//				isworking = (boolean) row[5];
//			}
//			if(row[6] == null) {
//				islunch = false;
//			}else {
//				islunch = (boolean) row[6];
//			}
//			
//			if(mapTmp.containsKey(personnelid_link)) {
//				TimeSheetLunchBinding temp = mapTmp.get(personnelid_link);
//				switch(shifttypeid_link.toString()) {
//					case "1":
//						temp.setWorkingShift1(isworking);
//						temp.setLunchShift1(islunch);
//						break;
//					case "2":
//						temp.setWorkingShift2(isworking);
//						temp.setLunchShift2(islunch);
//						break;
//					case "3":
//						temp.setWorkingShift3(isworking);
//						temp.setLunchShift3(islunch);
//						break;
//				}
//				mapTmp.put(personnelid_link, temp);
//			}else {
//				System.out.println("here ---");
//				TimeSheetLunchBinding temp = new TimeSheetLunchBinding();
//				temp.setPersonnelid_link(personnelid_link);
//				temp.setPersonnelCode(personnelCode);
//				temp.setPersonnelFullname(personnelFullname);
//				temp.setWorkingdate(workingdate);
//				temp.setLunchShift1(false);
//				temp.setLunchShift2(false);
//				temp.setLunchShift3(false);
//				temp.setWorkingShift1(false);
//				temp.setWorkingShift2(false);
//				temp.setWorkingShift3(false);
//				switch(shifttypeid_link.toString()) {
//					case "1":
//						System.out.println("here 111");
//						temp.setWorkingShift1(isworking);
//						temp.setLunchShift1(islunch);
//						break;
//					case "2":
//						System.out.println("here 222");
//						temp.setWorkingShift2(isworking);
//						temp.setLunchShift2(islunch);
//						break;
//					case "3":
//						System.out.println("here 333");
//						temp.setWorkingShift3(isworking);
//						temp.setLunchShift3(islunch);
//						break;
//				}
//				System.out.println("here 444");
//				mapTmp.put(personnelid_link, temp);
//				System.out.println("555");
//			}
//		}
//		data = new ArrayList<TimeSheetLunchBinding>(mapTmp.values());
//		return data;
//	}

}
