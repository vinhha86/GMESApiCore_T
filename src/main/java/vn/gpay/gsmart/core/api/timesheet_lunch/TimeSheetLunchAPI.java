package vn.gpay.gsmart.core.api.timesheet_lunch;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.personel.Personel;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.timesheet_lunch.ITimeSheetLunchService;
import vn.gpay.gsmart.core.timesheet_lunch.TimeSheetLunch;
import vn.gpay.gsmart.core.timesheet_lunch.TimeSheetLunchBinding;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/timesheetlunch")
public class TimeSheetLunchAPI {

	@Autowired private ITimeSheetLunchService timeSheetLunchService;
	@Autowired private IPersonnel_Service personnelService;
	
//	@RequestMapping(value = "/getForTimeSheetLunch",method = RequestMethod.POST)
//	public ResponseEntity<TimeSheetLunch_response> getForTimeSheetLunch(HttpServletRequest request) {
//		TimeSheetLunch_response response = new TimeSheetLunch_response();
//		try {
////			Calendar cal = new GregorianCalendar();
////			cal.add(Calendar.DAY_OF_MONTH, -20);
////			Date twentyDaysAgo = cal.getTime();
//			Date today = new Date();
//			Long l = (long) 175;
//			
////			List<TimeSheetLunchBinding> list = timeSheetLunchService.getForTimeSheetLunch(l, today);
//			System.out.println(list.size());
//			
//			response.data = list;
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
//			return new ResponseEntity<TimeSheetLunch_response>(response,HttpStatus.OK);
//		}catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());			
//		    return new ResponseEntity<TimeSheetLunch_response>(HttpStatus.OK);
//		}    			
//	}
	
	@RequestMapping(value = "/getForTimeSheetLunch",method = RequestMethod.POST)
	public ResponseEntity<TimeSheetLunch_response> getForTimeSheetLunch(@RequestBody TimeSheetLunch_request entity ,HttpServletRequest request) {
		TimeSheetLunch_response response = new TimeSheetLunch_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			
//			Calendar cal = new GregorianCalendar();
//			cal.add(Calendar.DAY_OF_MONTH, -20);
//			Date twentyDaysAgo = cal.getTime();
			Date date = entity.date;
			Long orgid_link = entity.orgid_link;
			
			List<TimeSheetLunchBinding> list = new ArrayList<TimeSheetLunchBinding>();
			List<Personel> listPersonnel = personnelService.getby_org(orgid_link, orgrootid_link); // OK
//			System.out.println(listPersonnel.size());
			List<TimeSheetLunch> listTimeSheetLunch = timeSheetLunchService.getForTimeSheetLunch(orgid_link, date); // OK
//			System.out.println(today);
//			System.out.println(listTimeSheetLunch.size());
			Map<Long, TimeSheetLunchBinding> mapTmp = new HashMap<>();
			
			for(Personel personnel : listPersonnel) { // add personnel to map
				TimeSheetLunchBinding temp = new TimeSheetLunchBinding();
				temp.setPersonnelid_link(personnel.getId());
				temp.setPersonnelCode(personnel.getCode());
				temp.setPersonnelFullname(personnel.getFullname());
				temp.setWorkingdate(date);
				temp.setLunchShift1(false);
				temp.setLunchShift2(false);
				temp.setLunchShift3(false);
				temp.setWorkingShift1(false);
				temp.setWorkingShift2(false);
				temp.setWorkingShift3(false);
				mapTmp.put(personnel.getId(), temp);
			}
			
			for(TimeSheetLunch timeSheetLunch : listTimeSheetLunch) {
				if(mapTmp.containsKey(timeSheetLunch.getPersonnelid_link())) {
					TimeSheetLunchBinding temp = mapTmp.get(timeSheetLunch.getPersonnelid_link());
					switch(timeSheetLunch.getShifttypeid_link().toString()) {
						case "1":
//							System.out.println("here 111");
							temp.setWorkingShift1(timeSheetLunch.isIsworking());
							temp.setLunchShift1(timeSheetLunch.isIslunch());
							break;
						case "2":
//							System.out.println("here 222");
							temp.setWorkingShift2(timeSheetLunch.isIsworking());
							temp.setLunchShift2(timeSheetLunch.isIslunch());
							break;
						case "3":
//							System.out.println("here 333");
							temp.setWorkingShift3(timeSheetLunch.isIsworking());
							temp.setLunchShift3(timeSheetLunch.isIslunch());
							break;
					}
					mapTmp.put(timeSheetLunch.getPersonnelid_link(), temp);
				}
			}
			list = new ArrayList<TimeSheetLunchBinding>(mapTmp.values());
			response.data = list;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<TimeSheetLunch_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<TimeSheetLunch_response>(HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/save",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> save(@RequestBody TimeSheetLunch_save_request entity ,HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();

			TimeSheetLunchBinding temp = entity.data;
			String dataIndex = entity.dataIndex;
			
			Long personnelid_link = temp.getPersonnelid_link();
			Date workingdate = temp.getWorkingdate();
			
			if(dataIndex.equals("workingShift1") || dataIndex.equals("lunchShift1")) {
				Integer shifttypeid_link = 1;
				boolean isWorkingShift = temp.isWorkingShift1();
				boolean isLunchShift = temp.isLunchShift1();
				List<TimeSheetLunch> list = timeSheetLunchService.getByPersonnelDateAndShift(
						personnelid_link, workingdate, shifttypeid_link);
				
				if(list.size() > 0) {
					TimeSheetLunch timeSheetLunch = list.get(0);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				}else {
					TimeSheetLunch timeSheetLunch = new TimeSheetLunch();
					timeSheetLunch.setId(0L);
					timeSheetLunch.setOrgrootid_link(orgrootid_link);
					timeSheetLunch.setPersonnelid_link(personnelid_link);
					timeSheetLunch.setShifttypeid_link(1);
					timeSheetLunch.setUsercreatedid_link(user.getId());
					timeSheetLunch.setTimecreated(new Date());
					timeSheetLunch.setWorkingdate(workingdate);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				}
			}
			if(dataIndex.equals("workingShift2") || dataIndex.equals("lunchShift2")) {
				Integer shifttypeid_link = 2;
				boolean isWorkingShift = temp.isWorkingShift2();
				boolean isLunchShift = temp.isLunchShift2();
				List<TimeSheetLunch> list = timeSheetLunchService.getByPersonnelDateAndShift(
						personnelid_link, workingdate, shifttypeid_link);
				
				if(list.size() > 0) {
					TimeSheetLunch timeSheetLunch = list.get(0);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				}else {
					TimeSheetLunch timeSheetLunch = new TimeSheetLunch();
					timeSheetLunch.setId(0L);
					timeSheetLunch.setOrgrootid_link(orgrootid_link);
					timeSheetLunch.setPersonnelid_link(personnelid_link);
					timeSheetLunch.setShifttypeid_link(2);
					timeSheetLunch.setUsercreatedid_link(user.getId());
					timeSheetLunch.setTimecreated(new Date());
					timeSheetLunch.setWorkingdate(workingdate);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				}
			}
			if(dataIndex.equals("workingShift3") || dataIndex.equals("lunchShift3")) {
				Integer shifttypeid_link = 3;
				boolean isWorkingShift = temp.isWorkingShift3();
				boolean isLunchShift = temp.isLunchShift3();
				List<TimeSheetLunch> list = timeSheetLunchService.getByPersonnelDateAndShift(
						personnelid_link, workingdate, shifttypeid_link);
				
				if(list.size() > 0) {
					TimeSheetLunch timeSheetLunch = list.get(0);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				}else {
					TimeSheetLunch timeSheetLunch = new TimeSheetLunch();
					timeSheetLunch.setId(0L);
					timeSheetLunch.setOrgrootid_link(orgrootid_link);
					timeSheetLunch.setPersonnelid_link(personnelid_link);
					timeSheetLunch.setShifttypeid_link(3);
					timeSheetLunch.setUsercreatedid_link(user.getId());
					timeSheetLunch.setTimecreated(new Date());
					timeSheetLunch.setWorkingdate(workingdate);
					timeSheetLunch.setIsworking(isWorkingShift);
					timeSheetLunch.setIslunch(isLunchShift);
					timeSheetLunchService.save(timeSheetLunch);
				}
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}    			
	}
}
