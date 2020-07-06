package vn.gpay.gsmart.core.api.holiday;

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
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/holiday")
public class HolidayAPI {
	@Autowired IHolidayService holidayService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<Holiday_getall_response> GetAll(HttpServletRequest request ) {
		Holiday_getall_response response = new Holiday_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
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
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			int thisYear = Calendar.getInstance().get(Calendar.YEAR);
			
			List<Integer> years = holidayService.getAllYears();
			if(!years.contains(thisYear))years.add(thisYear);
			
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
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create(@RequestBody Holiday_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Long startTime = entity.startTime;
			Long endTime = entity.endTime;
			String comment = entity.comment;
			
			Date date1=new Date(startTime);
			Date date2=new Date(endTime);
			
			LocalDate start = date1.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate end = date2.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			for (LocalDate date = start; date.isEqual(end) || date.isBefore(end); date = date.plusDays(1)) {
			    Holiday temp = new Holiday();
			    temp.setId(0L);
			    temp.setYear(date.getYear());
			    temp.setDay(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()));
			    temp.setComment(comment);
			    temp.setOrgrootid_link(user.getRootorgid_link());
			    holidayService.save(temp);
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
				holidayService.deleteById(h.getId());
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
			
			holiday.setId(entity.data.getId());
			holiday.setYear(localdate.getYear());
			holiday.setDay(Date.from(localdate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
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
	
}