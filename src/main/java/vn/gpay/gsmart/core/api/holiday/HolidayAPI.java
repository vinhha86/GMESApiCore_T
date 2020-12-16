package vn.gpay.gsmart.core.api.holiday;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

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
import vn.gpay.gsmart.core.holiday.Holiday;
import vn.gpay.gsmart.core.holiday.IHolidayService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayAPI {
	@Autowired IHolidayService holidayService;
	@Autowired IPOrderGrant_Service grantService;
	@Autowired Common commonService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<Holiday_getall_response> GetAll(HttpServletRequest request ) {
		Holiday_getall_response response = new Holiday_getall_response();
		try {
			
			response.data = holidayService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Holiday_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Holiday_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getallyears",method = RequestMethod.POST)
	public ResponseEntity<Holiday_getallYears_response> GetAllYears(HttpServletRequest request ) {
		Holiday_getallYears_response response = new Holiday_getallYears_response();
		try {
			
			int thisYear = Calendar.getInstance().get(Calendar.YEAR);
			int yearStart = 2020;
			
			List<Integer> years = holidayService.getAllYears(thisYear);
			for(int i=yearStart;i<=thisYear+3;i++) {
				if(!years.contains(i))years.add(i);
			}
			
			Collections.sort(years, Collections.reverseOrder());
			years.add(0, null);
			
			List<Holiday> data = new ArrayList<Holiday>();
			for(Integer year : years) {
				Holiday temp = new Holiday();
				temp.setYear(year);
				data.add(temp);
			}
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Holiday_getallYears_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Holiday_getallYears_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getbyyear",method = RequestMethod.POST)
	public ResponseEntity<Holiday_getall_response> GetByYear(@RequestBody Holiday_getByYear_request entity,HttpServletRequest request ) {
		Holiday_getall_response response = new Holiday_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			response.data = holidayService.getby_year(orgrootid_link, entity.year);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Holiday_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Holiday_getall_response>(response,HttpStatus.OK);
		}
	}
	
//	@RequestMapping(value = "/create",method = RequestMethod.POST)
//	public ResponseEntity<ResponseBase> Create(@RequestBody Holiday_create_request entity,HttpServletRequest request ) {
//		ResponseBase response = new ResponseBase();
//		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			
//			Long startTime = entity.startTime;
//			Long endTime = entity.endTime;
//			String comment = entity.comment;
//			
//			Date date1=new Date(startTime);
//			Date date2=new Date(endTime);
//			
//			LocalDate start = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//			LocalDate end = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
//
////			for (LocalDate date = start; date.isEqual(end) || date.isBefore(end); date = date.plusDays(1)) {
//			    Holiday temp = new Holiday();
//			    temp.setId(0L);
//			    temp.setYear(start.getYear());
//			    temp.setDay(Date.from(start.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//			    temp.setDayto(Date.from(end.atStartOfDay(ZoneId.systemDefault()).toInstant()));
//			    temp.setComment(comment);
//			    temp.setOrgrootid_link(user.getRootorgid_link());
//			    holidayService.save(temp);
////			}
//			
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
//		}catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
//		}
//	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create(@RequestBody Holiday_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Date startDate = entity.startDate;
			Date endDate = entity.endDate;
			String comment = entity.comment;
			
//			System.out.println(startDate);
//			System.out.println(endDate);
//			System.out.println(comment);
			
			LocalDate start = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate end = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			for (LocalDate date = start; !date.isAfter(end); date = date.plusDays(1)) {
			    // Do your job here with `date`.
				Date dateObj = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
//			    System.out.println(dateObj);
				List<Holiday> listHoliday = holidayService.getByDate(dateObj);
				if(listHoliday.size() == 0) {
					Holiday holiday = new Holiday();
					holiday.setId(null);
					holiday.setOrgrootid_link(user.getRootorgid_link());
					holiday.setDay(dateObj);
					holiday.setDayto(dateObj);
					holiday.setComment(comment);
					holiday.setYear(date.getYear());
					holidayService.save(holiday);
					
					//Cap nhat lai ns nhung porder_grant bi anh huong
					List<POrderGrant> list_grant = grantService.getgrant_contain_Day(dateObj);
					for(POrderGrant grant : list_grant) {
						//cap nhat lai duration va nang suat
						int duration = grant.getDuration() - 1;
						int productivity = commonService.getProductivity(grant.getGrantamount(), duration);
						String reason = grant.getReason_change();
						String pattern = "dd/MM/yyyy";
						DateFormat df = new SimpleDateFormat(pattern);
						if(reason!=null && reason.compareTo("") != 0) {		
							if(!reason.contains("Xóa"))
								reason += ";"+df.format(dateObj);
							else
								reason = "Ngày nghỉ: "+df.format(dateObj);
						}
						else {
							reason = "Ngày nghỉ: "+df.format(dateObj);
						}
						grant.setDuration(duration);
						grant.setProductivity(productivity);
						grant.setReason_change(reason);
						
						grantService.save(grant);
					}
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody Holiday_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();

		try {
			for(Holiday h : entity.data) {
				Date date = h.getDay();
				holidayService.delete(h);
				
				//Cap nhat lai ns nhung porder_grant bi anh huong
				List<POrderGrant> list_grant = grantService.getgrant_contain_Day(date);
				for(POrderGrant grant : list_grant) {
					//cap nhat lai duration va nang suat
					int duration = grant.getDuration() + 1;
					int productivity = commonService.getProductivity(grant.getGrantamount(), duration);
					
					String pattern = "dd/MM/yyyy";
					DateFormat df = new SimpleDateFormat(pattern);
					String reason = "Xóa Ngày nghỉ: "+df.format(date);
					
					grant.setDuration(duration);
					grant.setProductivity(productivity);
					grant.setReason_change(reason);
					
					grantService.save(grant);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Save(@RequestBody Holiday_save_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		
		try {
			Holiday holiday = new Holiday();
			
			Long time = entity.time;
			Date date=new Date(time);
			LocalDate localdate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			Long timeto = entity.timeto;
			Date dateto=new Date(timeto);
			LocalDate localdateto = dateto.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			holiday.setId(entity.data.getId());
			holiday.setYear(localdate.getYear());
			holiday.setDay(Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			holiday.setDayto(Date.from(localdateto.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			holiday.setComment(entity.data.getComment());
			holiday.setOrgrootid_link(entity.data.getOrgrootid_link());
			
			holidayService.save(holiday);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/clone", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Clone(@RequestBody Holiday_clone_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			String dateStr1 = entity.year.toString() + "-12-31";
			String dateStr2 = entity.year.toString() + "-01-01";
			String dateStr3 = entity.year.toString() + "-04-30";
			String dateStr4 = entity.year.toString() + "-05-01";
			List<String> listStr = new ArrayList<String>();
			listStr.add(dateStr1);
			listStr.add(dateStr2);
			listStr.add(dateStr3);
			listStr.add(dateStr4);
			
			for(String dateStr : listStr) {
				Date date = GPAYDateFormat.StringToDate(dateStr, "yyyy-MM-dd");
				List<Holiday> l = holidayService.getby_date(date, date);
				if(l.size() == 0) {
					Holiday h = new Holiday();
					h.setId(0L);
					h.setDay(date);
					h.setDayto(date);
					h.setYear(entity.year);
					h.setOrgrootid_link(user.getRootorgid_link());
					h.setComment("Nghỉ lễ");
					holidayService.save(h);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
}
