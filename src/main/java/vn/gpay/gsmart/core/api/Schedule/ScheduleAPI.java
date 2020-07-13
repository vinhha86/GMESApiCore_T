package vn.gpay.gsmart.core.api.Schedule;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.Schedule.Schedule_holiday;
import vn.gpay.gsmart.core.Schedule.Schedule_plan;
import vn.gpay.gsmart.core.Schedule.Schedule_porder;
import vn.gpay.gsmart.core.holiday.Holiday;
import vn.gpay.gsmart.core.holiday.IHolidayService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.org.OrgServiceImpl;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleAPI {
	@Autowired IHolidayService holidayService;
	@Autowired OrgServiceImpl orgService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPOrderGrant_Service granttService;
	@Autowired Common commonService;
	
	@RequestMapping(value = "/getplan",method = RequestMethod.POST)
	public ResponseEntity<get_schedule_porder_response> GetAll(HttpServletRequest request,
			@RequestParam String listid, @RequestParam String startDate , @RequestParam String endDate,
			@RequestParam String PO_code, @RequestParam String Buyer, @RequestParam String Vendor, 
			 @RequestParam Boolean isReqPorder, @RequestParam Boolean isAllgrant) throws ParseException{
		get_schedule_porder_response response = new get_schedule_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long orgid_link = user.getOrgid_link();
		long orgbuyerid_link = Buyer == "" ? (long)0 : Long.parseLong(Buyer);
		long orgvendorid_link = Vendor == "" ? (long)0 : Long.parseLong(Vendor);
		 
	    Date startdate = new SimpleDateFormat("yyyy-MM-dd").parse(startDate.substring(0,10));  
	    
	    Date toDate = new SimpleDateFormat("yyyy-MM-dd").parse(endDate.substring(0,10));  
		
		String[] listtype = listid.split(",");
		List<String> list = new ArrayList<String>();
		for (String string : listtype) {
			list.add(string);
		}
		
		try { 
			//Lay danh sach ngày nghỉ
			List<Holiday> list_holiday = holidayService.getby_year(orgrootid_link, year);
			for(Holiday holiday : list_holiday) {
				Schedule_holiday sch_holiday = new Schedule_holiday();
				sch_holiday.setComment(holiday.getComment());
				sch_holiday.setStartDate(holiday.getDay());
				sch_holiday.setEndDate(commonService.Date_Add(holiday.getDay(), 1));
				sch_holiday.setCls("holiday");
				
				response.zones.rows.add(sch_holiday);
			}
			
			//Lay danh sach nha may va to
			List<Schedule_plan> list_sch_plan = new ArrayList<Schedule_plan>();
			
			Org orgroot = orgService.findOne(orgid_link);
			
			//Lay danh sach nhung don vi duoc phep xem cua user dang dang nhap
			List<Org> listorg = new ArrayList<Org>();
			if(orgroot.getOrgtypeid_link() ==1 )
				listorg = orgService.getorgChildrenbyOrg(orgid_link, list);
			else 
				listorg.add(orgroot);
			
			long id = 1;
			for(Org org_factory : listorg) {
				long orgid = org_factory.getId();
				
				Schedule_plan sch_org = new Schedule_plan();
				sch_org.setId(id);
				sch_org.setCode(org_factory.getCode());
				sch_org.setExpanded(true);
				sch_org.setIconCls("x-fa fa-industry");
				sch_org.setId_origin(org_factory.getId());
				sch_org.setLeaf(false);
				sch_org.setName(org_factory.getName());
				sch_org.setOrgtypeid_link(org_factory.getOrgtypeid_link());
				sch_org.setParentid_origin(org_factory.getParentid_link());
				sch_org.setType(0);
				
				id++;
				
				//Lấy các tổ của nhà máy
				List<Org> listorg_grantt = orgService.getorgChildrenbyOrg(orgid, list);
				for(Org org_grant : listorg_grantt) {
					Schedule_plan sch_org_grant = new Schedule_plan();
					
					sch_org_grant.setId(id);
					sch_org_grant.setCode(org_grant.getCode());
					sch_org_grant.setExpanded(false);
					sch_org_grant.setIconCls("x-fa fa-sliders");
					sch_org_grant.setId_origin(org_grant.getId());
					sch_org_grant.setLeaf(true);
					sch_org_grant.setName(org_grant.getName());
					sch_org_grant.setOrgtypeid_link(org_grant.getOrgtypeid_link());
					sch_org_grant.setParentid_origin(org_grant.getParentid_link());
					sch_org_grant.setType(1);
					
					id++;
					
					//Lấy các lệnh của các tổ
					
					List<POrderGrant> list_porder = granttService.get_granted_bygolivedate(startdate, toDate, org_grant.getId(),
							PO_code, orgbuyerid_link, orgvendorid_link);
					for(POrderGrant pordergrant : list_porder) {
						Date start = pordergrant.getProductiondate_plan();
						Date end = commonService.getEndOfDate(pordergrant.getEndDatePlan());
						int duration = commonService.getDuration(start, end, orgrootid_link, year);
						int productivity = pordergrant.getTotalpackage() / duration; 
						
						Schedule_porder sch_porder = new Schedule_porder();
						sch_porder.setCls(pordergrant.getCls());
						sch_porder.setEndDate(end);
						sch_porder.setId_origin(pordergrant.getPorderid_link());
						sch_porder.setMahang(pordergrant.getMaHang());
						sch_porder.setName(pordergrant.getMaHang());
						sch_porder.setResourceId(sch_org_grant.getId());
						sch_porder.setStartDate(start);
						sch_porder.setDuration(duration);
						sch_porder.setTotalpackage(pordergrant.getTotalpackage());
						sch_porder.setProductivity(productivity);
						sch_porder.setVendorname(pordergrant.getVendorname());
						sch_porder.setBuyername(pordergrant.getBuyername());
						sch_porder.setPordercode(pordergrant.getpordercode());
						sch_porder.setParentid_origin(orgid);
						sch_porder.setStatus(-1);
						
						response.events.rows.add(sch_porder);
					}
					
					//Lay cac lenh dang thu
					List<POrderGrant> list_porder_test = granttService.get_porder_test(startdate, toDate, 
							org_grant.getId(), PO_code, orgbuyerid_link, orgvendorid_link);
					
					for(POrderGrant pordergrant : list_porder_test) {
						Date start = pordergrant.getProductiondate_plan();
						Date end = commonService.getEndOfDate(pordergrant.getEndDatePlan());
						int duration = commonService.getDuration(start, end, orgrootid_link, year);
						int productivity = pordergrant.getTotalpackage() / duration; 
						
						Schedule_porder sch_porder = new Schedule_porder();
						sch_porder.setCls(pordergrant.getCls());
						sch_porder.setEndDate(end);
						sch_porder.setId_origin(pordergrant.getPorderid_link());
						sch_porder.setMahang(pordergrant.getMaHang());
						sch_porder.setName(pordergrant.getMaHang());
						sch_porder.setResourceId(sch_org_grant.getId());
						sch_porder.setStartDate(start);
						sch_porder.setDuration(duration);
						sch_porder.setTotalpackage(pordergrant.getGrantamount());
						sch_porder.setProductivity(productivity);
						sch_porder.setVendorname(pordergrant.getVendorname());
						sch_porder.setBuyername(pordergrant.getBuyername());
						sch_porder.setPordercode(pordergrant.getpordercode());
						sch_porder.setParentid_origin(orgid);
						sch_porder.setStatus(pordergrant.getStatusPorder());
						
						response.events.rows.add(sch_porder);
					}
					
					if(isAllgrant) {
						sch_org.getChildren().add(sch_org_grant);
					}
					else {
						if(list_porder.size() > 0) {
							sch_org.getChildren().add(sch_org_grant);
						}
					}
				}
				
				//Lay nhung lenh chua phan chuyen cua moi nha may
//				Schedule_plan porder_free = new Schedule_plan();
//				porder_free.setExpanded(false);
//				porder_free.setId(id);
//				porder_free.setId_origin(0);
//				porder_free.setLeaf(false);
//				porder_free.setName("Chưa phân chuyền");
//				porder_free.setIconCls("x-fa fa-file-o");
//				porder_free.setParentid_origin(org_factory.getId());
//				id++;
//				
//				List<POrder> listporder_free = porderService.get_free_bygolivedate(startdate, toDate, org_factory.getId(),
//						PO_code, orgbuyerid_link, orgvendorid_link);
//				for(POrder porderfree : listporder_free) {
//					Schedule_plan sch_porderfree = new Schedule_plan();
//					
//					sch_porderfree.setExpanded(false);
//					sch_porderfree.setId(id);
//					sch_porderfree.setId_origin(0);
//					sch_porderfree.setLeaf(true);
//					sch_porderfree.setName(porderfree.getOrdercode());
//					sch_porderfree.setIconCls("x-fa fa-industry");
//					sch_porderfree.setParentid_origin(org_factory.getId());
//										
//					porder_free.getChildren().add(sch_porderfree);
//					
//					Schedule_porder sch_porder = new Schedule_porder();
//					Date _end = porderfree.getFinishdate_plan();
//					
//					sch_porder.setCls(porderfree.getCls());
//					sch_porder.setEndDate(commonService.getEndOfDate(_end));
//					sch_porder.setId_origin(porderfree.getId());
//					sch_porder.setMahang(porderfree.getMaHang());
//					sch_porder.setName(porderfree.getMaHang());
//					sch_porder.setResourceId(id);
//					sch_porder.setStartDate(porderfree.getProductiondate_plan());
//					sch_porder.setDuration(0);
//					sch_porder.setTotalpackage(porderfree.getTotalorder());
//					sch_porder.setProductivity(0);
//					sch_porder.setVendorname(porderfree.getVendorname());
//					sch_porder.setBuyername(porderfree.getBuyername());
//					sch_porder.setPordercode(porderfree.getOrdercode());
//					sch_porder.setParentid_origin(org_factory.getId());
//					sch_porder.setStatus(0);
//					
//					response.events.rows.add(sch_porder);
//					id++;
//				}
//				if(isAllgrant) {
//					sch_org.getChildren().add(porder_free);
//				}
//				else {
//					if(listporder_free.size() > 0) {
//						sch_org.getChildren().add(porder_free);
//					}
//				}
				
				list_sch_plan.add(sch_org);
			}
			
			response.resources.rows.addAll(list_sch_plan);
			response.success = true;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		
		return new ResponseEntity<get_schedule_porder_response>(response,HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<update_schedule_response> Update(HttpServletRequest request,
			@RequestBody update_schedule_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		update_schedule_response response = new update_schedule_response();
		try {
			Schedule_porder event = entity.data;
			Date start = event.getStartDate();
			Date end = event.getEndDate();
			long orgrootid_link = user.getRootorgid_link();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			
			int duration = commonService.getDuration(start, end, orgrootid_link, year);
			int productivity = event.getTotalpackage() / duration; 
			event.setDuration(duration);
			event.setProductivity(productivity);
			
			//update vao lenh
			long porderid_link = entity.data.getId_origin();
			POrder porder = porderService.findOne(porderid_link);
			porder.setProductiondate_plan(entity.data.getStartDate());
			porder.setFinishdate_plan(commonService.getPrevious(entity.data.getEndDate()));
			porderService.save(porder);
			
			response.data = event;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_schedule_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_schedule_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/update_porder",method = RequestMethod.POST)
	public ResponseEntity<update_porder_response> UpdatePorder(HttpServletRequest request,
			@RequestBody update_porder_request entity) {
		update_porder_response response = new update_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			//update vao lenh
			long porderid_link = entity.porderid_link;
			POrder porder = porderService.findOne(porderid_link);
			porder.setProductiondate_plan(entity.StartDate);
			porder.setFinishdate_plan(entity.EndDate);
			
			
			if(entity.grant_to_orgid_link!=0) {
				POrderGrant grant = new POrderGrant();
				grant.setGranttoorgid_link(entity.grant_to_orgid_link);
				grant.setId(null);
				grant.setOrdercode(porder.getOrdercode());
				grant.setOrgrootid_link(orgrootid_link);
				grant.setPorderid_link(porderid_link);
				grant.setTimecreated(new Date());
				grant.setUsercreatedid_link(user.getId());
				grant.setGrantdate(porder.getOrderdate());
				grant.setGrantamount(porder.getTotalorder());
				grant.setStatus(1);
				granttService.save(grant);
				
				porder.setStatus(1);
				porder.setGranttoorgid_link(entity.grant_to_orgid_link);
			}
			porderService.save(porder);
			
			response.duration = commonService.getDuration(entity.StartDate, entity.EndDate, orgrootid_link, year);
			response.productivity = porder.getTotalorder() / response.duration;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_porder_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create_pordergrant",method = RequestMethod.POST)
	public ResponseEntity<create_pordergrant_response> CreatePorderGrant(HttpServletRequest request,
			@RequestBody create_pordergrant_request entity) {
		create_pordergrant_response response = new create_pordergrant_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			POrder porder = porderService.findOne(entity.porderid_link);
			porder.setStatus(1);
			porderService.save(porder);
			
			POrderGrant pg = new POrderGrant();
			pg.setId(null);
			pg.setUsercreatedid_link(user.getId());
			pg.setTimecreated(new Date());
			pg.setGrantdate(new Date());
			pg.setGrantamount(porder.getTotalorder());
			pg.setGranttoorgid_link(entity.orggrantto);
			pg.setOrdercode(porder.getOrdercode());
			pg.setPorderid_link(porder.getId());
			pg.setOrgrootid_link(orgrootid_link);
			pg.setStatus(1);
			granttService.save(pg);
			
			Date startDate = porder.getProductiondate_plan();
			Date endDate = porder.getFinishdate_plan();
			int duration = commonService.getDuration(startDate, endDate, orgrootid_link, year);
			int productivity = (int)Math.ceil(porder.getTotalorder() / duration);
			
			Schedule_porder sch = new Schedule_porder();
			sch.setDuration(duration);
			sch.setProductivity(productivity);
			sch.setBuyername(porder.getBuyername());
			sch.setCls(porder.getCls());
			sch.setDuration(duration);
			sch.setEndDate(endDate);
			sch.setId_origin(porder.getId());
			sch.setMahang(porder.getMaHang());
			sch.setName(porder.getMaHang());
			sch.setParentid_origin(entity.parentid_origin);
			sch.setPordercode(porder.getOrdercode());
			sch.setProductivity(productivity);
			sch.setResourceId(entity.resourceid);
			sch.setStartDate(startDate);
			sch.setStatus(1);
			sch.setTotalpackage(porder.getTotalorder());
			sch.setVendorname(porder.getVendorname());
			
			response.data = sch;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_pordergrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_pordergrant_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/update_date",method = RequestMethod.POST)
	public ResponseEntity<update_duration_porder_response> UpdateStartDate(HttpServletRequest request,
			@RequestBody update_duration_porder_request entity) {
		update_duration_porder_response response = new update_duration_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			int duration = commonService.getDuration(entity.data.getStartDate(), entity.data.getEndDate(), orgrootid_link, year);
			int productivity = entity.data.getTotalpackage() / duration;
			
			Schedule_porder sch = entity.data;
			sch.setDuration(duration);
			sch.setProductivity(productivity);
			
			response.data = sch;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/update_duration",method = RequestMethod.POST)
	public ResponseEntity<update_duration_porder_response> UpdatDuration(HttpServletRequest request,
			@RequestBody update_duration_porder_request entity) {
		update_duration_porder_response response = new update_duration_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			Date end = commonService.Date_Add_with_holiday(entity.data.getStartDate(), entity.data.getDuration(), orgrootid_link, year);
			int productivity = entity.data.getTotalpackage() / entity.data.getDuration();
			
			Schedule_porder sch = entity.data;
			sch.setEndDate(end);
			sch.setProductivity(productivity);
			
			response.data = sch;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/update_productivity",method = RequestMethod.POST)
	public ResponseEntity<update_duration_porder_response> UpdatProductivity(HttpServletRequest request,
			@RequestBody update_duration_porder_request entity) {
		update_duration_porder_response response = new update_duration_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			int duration = entity.data.getTotalpackage() / entity.data.getProductivity();			
			Date end = commonService.Date_Add_with_holiday(entity.data.getStartDate(), duration, orgrootid_link, year);
			
			Schedule_porder sch = entity.data;
			sch.setEndDate(end);
			sch.setDuration(duration);
			
			response.data = sch;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/get_duration",method = RequestMethod.POST)
	public ResponseEntity<getduration_response> GetDuration(HttpServletRequest request,
			@RequestBody getduration_request entity) {
		getduration_response response = new getduration_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			int duration = commonService.getDuration(entity.StartDate, entity.EndDate, orgrootid_link, year);
			
			response.duration = duration;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<getduration_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<getduration_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/break_porder",method = RequestMethod.POST)
	public ResponseEntity<break_porder_response> BreakPorder(HttpServletRequest request,
			@RequestBody break_porder_request entity) {
		break_porder_response response = new break_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long porderid_link = entity.porderid_link;
		int producttivity = entity.producttivity;
		
		try {
			//Cập nhật lại lệnh cũ sau khi tách
			POrder porder_old = porderService.findOne(porderid_link);
			int totalorder_old = porder_old.getTotalorder() - entity.quantity;
			Date start_old = porder_old.getProductiondate_plan();
			int duration_old = (int)Math.ceil(totalorder_old/producttivity);
			Date end_old = commonService.Date_Add_with_holiday(start_old, duration_old, orgrootid_link, year);
			Date end_new = porder_old.getFinishdate_plan();
			
			porder_old.setFinishdate_plan(end_old);
			porder_old.setTotalorder(totalorder_old);
			porderService.save(porder_old);
			
			Schedule_porder old = new Schedule_porder();
			old.setEndDate(end_old);
			old.setDuration(duration_old);
			response.old_data = old;
			
			//Sinh lenh moi
			POrder porder_new  = new POrder();
			Date start_new = commonService.Date_Add(end_old, 1);
			int duration_new = commonService.getDuration(start_new, end_new, orgrootid_link, year);
		
			porder_new.setId(null);
			porder_new.setTotalorder(entity.quantity);
			porder_new.setProductiondate_plan(start_new);
			porder_new.setFinishdate_plan(end_new);
			porder_new.setOrderdate(new Date());
			porder_new.setProductiondate(start_new);
			porder_new.setGolivedate(end_new);
			porder_new.setUsercreatedid_link(user.getId());
			porder_new.setTimecreated(new Date());
			porder_new.setOrdercode(porder_old.getOrdercode());
			porder_new.setOrderdate(new Date());
			porder_new.setProductid_link(porder_old.getProductid_link());
			porder_new.setPcontractid_link(porder_old.getPcontractid_link());
			porder_new.setPcontract_poid_link(porder_old.getPcontract_poid_link());
			porder_new.setGranttoorgid_link(porder_old.getGranttoorgid_link());
			
			porder_new = porderService.save(porder_new);
			
			POrderGrant grant = new POrderGrant();
			grant.setGranttoorgid_link(porder_new.getGranttoorgid_link());
			grant.setId(null);
			grant.setOrdercode(porder_new.getOrdercode());
			grant.setOrgrootid_link(orgrootid_link);
			grant.setPorderid_link(porderid_link);
			grant.setTimecreated(new Date());
			grant.setUsercreatedid_link(user.getId());
			grant.setGrantdate(porder_new.getOrderdate());
			grant.setGrantamount(porder_new.getTotalorder());
			grant.setStatus(1);
			granttService.save(grant);
			
			Schedule_porder new_data = new Schedule_porder();
			new_data.setCls(porder_new.getCls());
			new_data.setEndDate(end_new);
			new_data.setId_origin(porder_new.getId());
			new_data.setMahang(porder_new.getMaHang());
			new_data.setName(porder_new.getMaHang());
			new_data.setResourceId(entity.resourceid);
			new_data.setStartDate(start_new);
			new_data.setDuration(duration_new);
			new_data.setTotalpackage(entity.quantity);
			new_data.setProductivity(entity.producttivity);
			new_data.setVendorname(porder_new.getVendorname());
			new_data.setBuyername(porder_new.getBuyername());
			new_data.setPordercode(porder_new.getOrdercode());
			new_data.setParentid_origin(entity.parentid_origin);
			new_data.setStatus(1);
			
			response.new_data = new_data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<break_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<break_porder_response>(response, HttpStatus.OK);
		}
	} 
}
