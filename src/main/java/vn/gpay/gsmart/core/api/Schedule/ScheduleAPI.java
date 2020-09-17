package vn.gpay.gsmart.core.api.Schedule;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.time.DateUtils;
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
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract.PContract;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.task_object.Task_Object;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleAPI {
	@Autowired IHolidayService holidayService;
	@Autowired OrgServiceImpl orgService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPOrderGrant_Service granttService;
	@Autowired Common commonService;
	@Autowired IPOrderProcessing_Service processService;
	@Autowired IPOrder_Req_Service reqService;
	@Autowired IPContractService pcontractService;
	@Autowired IPContract_POService poService;
	@Autowired ITask_Object_Service taskobjectService;
	@Autowired ITask_CheckList_Service checklistService;
	@Autowired ITask_Service taskService;
	@Autowired IPOrderGrant_SKUService grantskuService;
	
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
		 
	    Date startdate = commonService.getBeginOfDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate.substring(0,10))); 
	    Date toDate = commonService.getEndOfDate(new SimpleDateFormat("yyyy-MM-dd").parse(endDate.substring(0,10)));  
		
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
			
			//Lay ngay chu nhat
			List<Date> list_sunday = commonService.getList_SunDay_byYear(year);
			for(Date date : list_sunday) {
				Schedule_holiday sch_sunday = new Schedule_holiday();
				sch_sunday.setComment("");
				sch_sunday.setStartDate(date);
				sch_sunday.setEndDate(commonService.Date_Add(date, 1));
				sch_sunday.setCls("sunday");
				
				response.zones.rows.add(sch_sunday);
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
				sch_org.setExpanded(false);
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
					sch_org_grant.setLeaf(false);
					sch_org_grant.setName(org_grant.getName());
					sch_org_grant.setOrgtypeid_link(org_grant.getOrgtypeid_link());
					sch_org_grant.setParentid_origin(org_grant.getParentid_link());
					sch_org_grant.setType(1);
					
					id++;
					
					//Them dong thong tin tien do
					Schedule_plan sch_process = new Schedule_plan();
					sch_process.setId(id);
					sch_process.setExpanded(false);
					sch_process.setIconCls("x-fa fa-line-chart");
					sch_process.setLeaf(true);
					sch_process.setName("Tiến độ");
					sch_process.setType(2);
					sch_org_grant.getChildren().add(sch_process);
					id++;
					
					//Them dong thong tin du doan tien do
					Schedule_plan sch_estimation = new Schedule_plan();
					sch_estimation.setId(id);
					sch_estimation.setExpanded(false);
					sch_estimation.setIconCls("x-fa fa-binoculars");
					sch_estimation.setLeaf(true);
					sch_estimation.setName("Dự báo");
					sch_estimation.setType(3);
					sch_org_grant.getChildren().add(sch_estimation);
					id++;					
					
					//Lấy các lệnh của các tổ

					List<POrderGrant> list_porder = granttService.get_granted_bygolivedate(startdate, toDate, org_grant.getId(),
							PO_code, orgbuyerid_link, orgvendorid_link);
					
					int day_grant = 0;
					Date date_end = null;
					Date date_start = null;
					
					for(POrderGrant pordergrant : list_porder) {
//						total_day = commonService.getDuration(startdate, toDate, orgrootid_link, year);
						Date start = commonService.getBeginOfDate(pordergrant.getStart_date_plan());
						Date end = commonService.getEndOfDate(pordergrant.getFinish_date_plan());
						Date start_free = start, end_free = end;
						
						if(start_free.before(startdate))
							start_free = startdate;
						
						if(end_free.after(toDate))
							end_free = toDate;
						
						if(date_end == null)
							date_end = end_free;						
						else if(end_free.after(date_end))
							date_end = end_free;
						
						if(date_start == null)
							date_start = start_free;
						else if (start_free.before(date_start))
							date_start = start_free;
						
						int duration = pordergrant.getDuration();
						int productivity = pordergrant.getProductivity();
						
						Schedule_porder sch_porder = new Schedule_porder();
						sch_porder.setCls(pordergrant.getCls());
						sch_porder.setEndDate(end);
						sch_porder.setId_origin(pordergrant.getPorderid_link());
						sch_porder.setPorderid_link(pordergrant.getPorderid_link());
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
						sch_porder.setStatus(pordergrant.getStatus());
						sch_porder.setPorder_grantid_link(pordergrant.getId());
						sch_porder.setProductid_link(pordergrant.getProductid_link());
						sch_porder.setPcontract_poid_link(pordergrant.getPcontract_poid_link());
						sch_porder.setPcontractid_link(pordergrant.getPcontractid_link());
						
						int d = commonService.getDuration(start_free, end_free, orgrootid_link, year);
						day_grant += d;
						
						response.events.rows.add(sch_porder);
					}

					//Xac dinh so ngay lam viec trong khoang thoi gian dang xem
					if(date_end != null  && date_start != null) {
						int total_day = commonService.getDuration(date_start, date_end, orgrootid_link, year);
						
						String cls = (total_day - day_grant) <= 0 ? "" : "free";
						sch_org_grant.setCls(cls);
					}
					

					//Lay thong tin tien do thuc te cua lenh
//					ArrayList<Thread> arrThreads = new ArrayList<Thread>();
					for(POrderGrant pordergrant : list_porder) {
//						Thread thread = new Thread(){
//						public void run(){						
							//ngay dau va ngay cuoi cua lenh co trang thai > 3 (dang sx)
							POrderGrant theProcessing = processService.get_processing_bygolivedate(pordergrant.getPorderid_link(), pordergrant.getId());
							
							if (null != theProcessing){
								Date start = commonService.getBeginOfDate(theProcessing.getStart_date_plan());
								Date end = commonService.getEndOfDate(theProcessing.getFinish_date_plan());
								int duration = commonService.getDuration(start, end, orgrootid_link, year);
								
								Schedule_porder sch_porder_process = new Schedule_porder();
								sch_porder_process.setCls(pordergrant.getCls());
								sch_porder_process.setEndDate(end);
								sch_porder_process.setId_origin(pordergrant.getPorderid_link());
								sch_porder_process.setPorderid_link(pordergrant.getPorderid_link());
								sch_porder_process.setMahang(pordergrant.getMaHang());
								sch_porder_process.setName(pordergrant.getMaHang());
								sch_porder_process.setResourceId(sch_process.getId());
								sch_porder_process.setStartDate(start);
								sch_porder_process.setDuration(duration);
								
								//Gia tri ra chyen luy ke tinh den ngay cuoi cung
								sch_porder_process.setTotalpackage(pordergrant.getGrantamount());
								//Gia tri ra chuyen cua ngay cuoi
								sch_porder_process.setProductivity(theProcessing.getAmountcutsum());
								
								sch_porder_process.setVendorname(pordergrant.getVendorname());
								sch_porder_process.setBuyername(pordergrant.getBuyername());
								sch_porder_process.setPordercode(pordergrant.getpordercode());
								sch_porder_process.setParentid_origin(orgid);
								sch_porder_process.setStatus(pordergrant.getStatus());
								sch_porder_process.setPorder_grantid_link(pordergrant.getId());
								sch_porder_process.setProductid_link(pordergrant.getProductid_link());
								sch_porder_process.setPcontract_poid_link(pordergrant.getPcontract_poid_link());
								sch_porder_process.setPcontractid_link(pordergrant.getPcontractid_link());
								
								response.events.rows.add(sch_porder_process);		
								
								//Thong tin du bao
								Schedule_porder sch_porder_estimation = new Schedule_porder();
								int daystoend = 0;
								if (sch_porder_process.getProductivity() > 0){
									daystoend = (int) Math.ceil((double)pordergrant.getGrantamount() / sch_porder_process.getProductivity()); 
								} else {
									if (duration > 0){
										int avarageProductivyty = (int) Math.ceil((double)theProcessing.getGrantamount() / duration); 
										if (avarageProductivyty > 0)
											daystoend = (int) Math.ceil((double)pordergrant.getGrantamount() / avarageProductivyty);
										else
											daystoend = 0;
									}
									else
										daystoend = 0;
								}
								Date end_estimation = commonService.getEndOfDate(DateUtils.addDays(start, daystoend));
								int duration_estimation = commonService.getDuration(start, end_estimation, orgrootid_link, year);
								
								sch_porder_estimation.setCls(pordergrant.getCls());
								sch_porder_estimation.setStartDate(start);
								sch_porder_estimation.setEndDate(end_estimation);
								sch_porder_estimation.setDuration(duration_estimation);
								sch_porder_estimation.setTotalpackage(pordergrant.getGrantamount());
								sch_porder_estimation.setProductivity(sch_porder_process.getProductivity());
	
								sch_porder_estimation.setResourceId(sch_estimation.getId());
								sch_porder_estimation.setMahang(pordergrant.getMaHang());
								sch_porder_estimation.setPordercode(pordergrant.getpordercode());
								
								response.events.rows.add(sch_porder_estimation);	
							} 
//					    }};
//						thread.start();
//						arrThreads.add(thread);						
					}
					
//		            for (int i = 0; i < arrThreads.size(); i++) 
//		            {
//		                arrThreads.get(i).join(); 
//		            }
		            
					//Lay cac lenh dang thu
					List<POrderGrant> list_porder_test = granttService.get_porder_test(startdate, toDate, 
							org_grant.getId(), PO_code, orgbuyerid_link, orgvendorid_link);
					
					for(POrderGrant pordergrant : list_porder_test) {
						Date start = commonService.getBeginOfDate(pordergrant.getStart_date_plan());
						Date end = commonService.getEndOfDate(pordergrant.getFinish_date_plan());
						int duration = pordergrant.getDuration();
						int productivity = pordergrant.getProductivity(); 
						
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
						sch_porder.setPorder_grantid_link(pordergrant.getId());
						sch_porder.setProductid_link(pordergrant.getProductid_link());
						sch_porder.setPcontract_poid_link(pordergrant.getPcontract_poid_link());
						sch_porder.setPorderid_link(pordergrant.getPorderid_link());
						
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
			int productivity = commonService.getProductivity(event.getTotalpackage(), duration); 
			event.setDuration(duration);
			event.setProductivity(productivity);
			event.setStartDate(commonService.getBeginOfDate(entity.data.getStartDate()));
			event.setEndDate(commonService.getEndOfDate(entity.data.getEndDate()));
			
			//update vao grant
			long pordergrantid_link = entity.data.getPorder_grantid_link();
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			grant.setStart_date_plan(entity.data.getStartDate());
			grant.setFinish_date_plan(commonService.getPrevious(entity.data.getEndDate()));
			grant.setDuration(duration);
			grant.setProductivity(productivity);
			granttService.save(grant);
			
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
			
			//update vao grantt
			long pordergrantid_link = entity.pordergrantid_link;
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			int duration = commonService.getDuration(entity.StartDate, entity.EndDate, orgrootid_link, year);
			int productivity = commonService.getProductivity(grant.getGrantamount(), response.duration);
			
			grant.setStart_date_plan(entity.StartDate);
			grant.setFinish_date_plan(entity.EndDate);
			grant.setDuration(duration);
			grant.setProductivity(productivity);
			granttService.save(grant);
			
			response.duration = duration;
			response.productivity = productivity;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_porder_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/move_porder",method = RequestMethod.POST)
	public ResponseEntity<move_porder_response> MovePorder(HttpServletRequest request,
			@RequestBody move_porder_request entity) {
		move_porder_response response = new move_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			int year = Calendar.getInstance().get(Calendar.YEAR);
			long porderid_link = entity.porderid_link;
			long pordergrantid_link = entity.pordergrant_id_link;
			
			//Cap nhat lai grant
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			
			//Giu lai grantorg cu de update Porder_processing
//			long granttoorgid_link_old = grant.getGranttoorgid_link();
			int duration = entity.schedule.getDuration();
			Date end_date = commonService.Date_Add_with_holiday(entity.startdate, duration - 1, orgrootid_link, year);
			
			grant.setGranttoorgid_link(entity.orggrant_toid_link);
			grant.setStart_date_plan(commonService.getBeginOfDate(entity.startdate));
			grant.setFinish_date_plan(commonService.getEndOfDate(end_date));
			grant = granttService.save(grant);
			
			//Cap nhat lai Porder_processing
			List<POrderProcessing> lsProcessing = processService.getByOrderId_and_GrantId(porderid_link, pordergrantid_link);
			if (lsProcessing.size() > 0){
				POrderProcessing theProcess = lsProcessing.get(0);
				theProcess.setGranttoorgid_link(entity.orggrant_toid_link);
				processService.save(theProcess);
			} else {
				//Tao moi POrderProcessing
				POrderProcessing pp = new POrderProcessing();
//				pp.setOrdercode(grant.getOrdercode());
				pp.setOrderdate(grant.getOrderdate());
				pp.setOrgrootid_link(orgrootid_link);
				pp.setPorderid_link(grant.getPorderid_link());
				pp.setTotalorder(grant.getGrantamount());
				pp.setUsercreatedid_link(user.getId());
				pp.setStatus(1);
				pp.setGranttoorgid_link(entity.orggrant_toid_link);
				pp.setProcessingdate(new Date());
				pp.setTimecreated(new Date());
				pp.setPordergrantid_link(grant.getId());
				
				processService.save(pp);				
			}
			//Cap nhat lai porder			
//			POrder porder = porderService.findOne(porderid_link);
//			porder.setProductiondate_plan(entity.startdate);
//			porder.setFinishdate_plan(entity.enddate);
////			porder.setGranttoorgid_link(entity.orggrant_toid_link);			
//			porderService.save(porder);
			
			Schedule_porder sch = entity.schedule;
			sch.setEndDate(commonService.getEndOfDate(end_date));
			sch.setStartDate(grant.getStart_date_plan());
//			sch.setDuration(commonService.getDuration(grant.getStart_date_plan(), grant.getFinish_date_plan(), orgrootid_link, year));
//			sch.setProductivity(commonService.getProductivity(grant.getGrantamount(), sch.getDuration()));
			sch.setPorder_grantid_link(entity.pordergrant_id_link);
			
			response.data = sch;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<move_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<move_porder_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update_porder_productivity",method = RequestMethod.POST)
	public ResponseEntity<update_porder_productivity_response> UpdatePorderProductivity(HttpServletRequest request,
			@RequestBody update_porder_productivity_request entity) {
		update_porder_productivity_response response = new update_porder_productivity_response();
		try {
//			long porderid_link = entity.data.getId_origin();
//			POrder porder = porderService.findOne(porderid_link);
//			porder.setProductiondate_plan(entity.data.getStartDate());
//			porder.setFinishdate_plan(commonService.getEndOfDate(entity.data.getEndDate()));
//			porderService.save(porder);
			
			long pordergrantid_link = entity.data.getPorder_grantid_link();
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			grant.setStart_date_plan(commonService.getBeginOfDate(entity.data.getStartDate()));
			grant.setFinish_date_plan(commonService.getEndOfDate(entity.data.getEndDate()));
			grant.setDuration(entity.data.getDuration());
			grant.setProductivity(entity.data.getProductivity());
			granttService.save(grant);
			
			Schedule_porder sch = entity.data;
			sch.setEndDate(grant.getFinish_date_plan());
			
			response.data = sch;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_porder_productivity_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_porder_productivity_response>(response, HttpStatus.OK);
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

			Date startDate = commonService.getBeginOfDate(porder.getProductiondate_plan());
			Date endDate = commonService.getEndOfDate(porder.getFinishdate_plan());
			int duration = commonService.getDuration(startDate, endDate, orgrootid_link, year);
			int productivity = commonService.getProductivity(porder.getTotalorder(), duration);
			
			porder.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
			porderService.save(porder);
			
			//Tao POrder_grant
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
			pg.setStart_date_plan(porder.getProductiondate_plan());
			pg.setFinish_date_plan(porder.getFinishdate_plan());
			pg.setProductivity(productivity);
			pg.setDuration(duration);
			
			pg = granttService.save(pg);
			
			//Lay toan bo SKU tu POrder sang POrder_grant_sku
			for(POrder_Product_SKU pSKU: porder.getPorder_product_sku()){
				POrderGrant_SKU pgSKU = new POrderGrant_SKU();
				pgSKU.setOrgrootid_link(orgrootid_link);
				pgSKU.setSkuid_link(pSKU.getSkuid_link());
				pgSKU.setGrantamount(pSKU.getPquantity_total());
				pgSKU.setPordergrantid_link(pg.getId());
				grantskuService.save(pgSKU);
			}
			
			POrderProcessing pp = new POrderProcessing();
			pp.setOrdercode(porder.getOrdercode());
			pp.setOrderdate(porder.getOrderdate());
			pp.setOrgrootid_link(orgrootid_link);
			pp.setPorderid_link(porder.getId());
			pp.setTotalorder(porder.getTotalorder());
			pp.setUsercreatedid_link(user.getId());
			pp.setStatus(1);
			pp.setGranttoorgid_link(entity.orggrantto);
			pp.setProcessingdate(new Date());
			pp.setTimecreated(new Date());
			pp.setPordergrantid_link(pg.getId());
			
			processService.save(pp);
			
			
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
			sch.setResourceId(entity.resourceid);
			sch.setStartDate(startDate);
			sch.setStatus(1);
			sch.setTotalpackage(porder.getTotalorder());
			sch.setVendorname(porder.getVendorname());
			sch.setPorder_grantid_link(pg.getId());
			
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
	
	@RequestMapping(value = "/create_pordergrant_test",method = RequestMethod.POST)
	public ResponseEntity<create_pordergrant_response> CreatePorderGrantTest(HttpServletRequest request,
			@RequestBody create_porder_test_request entity) {
		create_pordergrant_response response = new create_pordergrant_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {

			POrder_Req req = reqService.findOne(entity.porder_reqid_link);
			req.setStatus(1);
			reqService.save(req);
			
			String po_code = req.getPo_buyer().length() > 0?req.getPo_vendor():req.getPo_buyer();
			POrder porder = new POrder();
			porder.setOrdercode(po_code);
			porder.setFinishdate_plan(req.getShipdate());
			porder.setGolivedate(req.getShipdate());
			porder.setStatus(-1);
			porder.setGranttoorgid_link(req.getGranttoorgid_link());
			porder.setId(null);
			porder.setOrgrootid_link(orgrootid_link);
			porder.setPcontract_poid_link(req.getPcontract_poid_link());
			porder.setPcontractid_link(req.getPcontractid_link());
			porder.setProductiondate(req.getPO_Productiondate());
			porder.setUsercreatedid_link(user.getId());
			porder.setTimecreated(new Date());
			porder.setProductiondate_plan(req.getPO_Productiondate());
			porder.setPorderreqid_link(entity.porder_reqid_link);
			porder.setTotalorder(req.getTotalorder());
			porder.setProductid_link(req.getProductid_link());
			porder = porderService.saveAndFlush(porder);
			
			Date startDate = commonService.getBeginOfDate(porder.getProductiondate_plan());
			Date endDate = commonService.getEndOfDate(porder.getFinishdate_plan());
			int duration = commonService.getDuration(startDate, endDate, orgrootid_link, year);
			int productivity = commonService.getProductivity(porder.getTotalorder(), duration);
			
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
			pg.setStatus(-1);
			pg.setStart_date_plan(req.getPO_Productiondate());
			pg.setFinish_date_plan(req.getShipdate());
			pg.setProductivity(productivity);
			pg.setDuration(duration);
			pg = granttService.save(pg);
			
			PContract contract = req.getPcontract();
			PContract_PO po = req.getPcontract_po();
			
			String name = "";
			int total = pg.getGrantamount() == null ? 0 : pg.getGrantamount();
			float totalPO = po == null ? 0 : po.getPo_quantity();
			
			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);
			
			if(contract != null && po!=null) {
				String ST = contract.getBuyername() == null ? "" : contract.getBuyername();
				String PO = po.getPo_buyer() == null ? "" : po.getPo_vendor();
				name += "#"+ST+"-PO: "+PO+"-"+decimalFormat.format(total)+"/"+decimalFormat.format(totalPO);
			}
			
			Schedule_porder sch = new Schedule_porder();
			sch.setDuration(duration);
			sch.setProductivity(productivity);
			sch.setBuyername(contract.getBuyername());
			sch.setCls(porder.getCls());
			sch.setDuration(duration);
			sch.setEndDate(endDate);
			sch.setId_origin(porder.getId());
			sch.setMahang(name);
			sch.setName(name);
			sch.setParentid_origin(entity.parentid_origin);
			sch.setPordercode(porder.getOrdercode());
			sch.setProductivity(productivity);
			sch.setResourceId(entity.resourceid);
			sch.setStartDate(startDate);
			sch.setStatus(-1);
			sch.setTotalpackage(porder.getTotalorder());
			sch.setVendorname(contract.getVendorname());
			sch.setPorder_grantid_link(pg.getId());
			sch.setPorderid_link(porder.getId());
			
			response.data = sch;
			
			//Cap nhat lai check list trong taskboard
			long objecttypeid_link = 8;
			List<Task_Object> listobj = taskobjectService.getbyObjectType_and_objectid_link(objecttypeid_link, entity.porder_reqid_link);
			if(listobj.size()>0) {
				Task_Object obj = listobj.get(0);
				long taskid_link = obj.getTaskid_link();
				long tasktype_checklits_id_link = 1; // id trong DB
				List<Task_CheckList> checklist = checklistService.getby_taskid_link_and_typechecklist(taskid_link, tasktype_checklits_id_link);
				if(checklist.size()>0) {
					Task_CheckList subTask = checklist.get(0);
					String description = subTask.getDescription();
					description += " ("+user.getFullname()+")";
					subTask.setDone(true);
					subTask.setDatefinished(new Date());
					subTask.setUserfinishedid_link(user.getId());
					subTask.setDescription(description);
					checklistService.save(subTask);
					
					int status = 1;
					//Kiem tra cong viec hoan thanh het chua
					List<Task_CheckList> list_sub = checklistService.getby_taskid_link(taskid_link);
					list_sub.removeIf(c-> c.getDone() == true);
					if(list_sub.size() == 0)
						status = 2;
					Task task = taskService.findOne(taskid_link);
					task.setStatusid_link(status);
					if(status == 2) {
						task.setDatefinished(new Date());
					}
					taskService.save(task);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_pordergrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_pordergrant_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/delete_porder_test",method = RequestMethod.POST)
	public ResponseEntity<delete_porder_req_response> DeletePorderTest(HttpServletRequest request,
			@RequestBody delete_porder_req_request entity) {
		delete_porder_req_response response = new delete_porder_req_response();
		long porderid_link = entity.porderid_link;
		long pordergrantid_link = entity.pordergrantid_link;
		POrder porder = porderService.findOne(porderid_link);
		try {
			//xoa trong bang poder_grant
			granttService.deleteById(pordergrantid_link);
			
			//Xoa trong bang porder
			porderService.deleteById(porderid_link);
			
			//Cap nhat lai bang porder_req
			POrder_Req req = reqService.findOne(porder.getPorderreqid_link());
			req.setStatus(0);
			reqService.save(req);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_porder_req_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_porder_req_response>(response, HttpStatus.OK);
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
			int productivity = commonService.getProductivity(entity.data.getTotalpackage(), duration);
			
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
			int productivity = commonService.getProductivity(entity.data.getTotalpackage(), entity.data.getDuration());
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
	
	@RequestMapping(value = "/merger_porder",method = RequestMethod.POST)
	public ResponseEntity<merger_porder_response> MergerPorder(HttpServletRequest request,
			@RequestBody merger_porder_request entity) {
		merger_porder_response response = new merger_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		try {
			POrderGrant grant_src = granttService.findOne(entity.pordergrantid_link_src);
			POrderGrant grant_des = granttService.findOne(entity.pordergrantid_link_des);
			
			int total = grant_des.getGrantamount() + grant_src.getGrantamount();
			
			Date start = grant_des.getStart_date_plan();
			start = commonService.getBeginOfDate(start);
			
			int productivity = grant_des.getProductivity();
									
			int duration = commonService.getDuration_byProductivity(total, productivity);

			Date end = commonService.Date_Add_with_holiday(start, duration - 1, orgrootid_link, year);
			end = commonService.getEndOfDate(end);
			
			//Xoa grant nguon va processing nguon
			List<POrderProcessing> list_process = processService.getByOrderId_and_GrantId(grant_src.getPorderid_link(), entity.pordergrantid_link_src);
			for(POrderProcessing process : list_process) {
				processService.delete(process);
			}
			
			
			//Cap nhat grant dich
			grant_des.setGrantamount(total);
			grant_des.setStart_date_plan(start);
			grant_des.setFinish_date_plan(end);
			grant_des.setGranttoorgid_link(grant_des.getGranttoorgid_link());
			grant_des.setDuration(duration);
			grant_des = granttService.save(grant_des);
			
			Schedule_porder sch = entity.sch;
			sch.setStartDate(start);
			sch.setEndDate(end);
			sch.setDuration(duration);
			sch.setProductivity(productivity);
			sch.setName(grant_des.getMaHang());
			sch.setMahang(grant_des.getMaHang());
			sch.setTotalpackage(total);
			
			//chuyen sku cua grant_src sang grant_des
			List<POrderGrant_SKU> list_sku_src = grantskuService.getPOrderGrant_SKU(entity.pordergrantid_link_src);
			
			for (POrderGrant_SKU pOrderGrant_SKU : list_sku_src) {
				POrderGrant_SKU sku = grantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId(pOrderGrant_SKU.getSkuid_link(), entity.pordergrantid_link_des);
				if(sku == null) {
					POrderGrant_SKU grantsku = new POrderGrant_SKU();
					grantsku.setGrantamount(pOrderGrant_SKU.getGrantamount());
					pOrderGrant_SKU.setId(null);
					pOrderGrant_SKU.setOrgrootid_link(orgrootid_link);
					pOrderGrant_SKU.setPordergrantid_link(entity.pordergrantid_link_des);
					pOrderGrant_SKU.setSkuid_link(pOrderGrant_SKU.getSkuid_link());
					grantskuService.save(pOrderGrant_SKU);
				} 
				else {
					sku.setGrantamount(sku.getGrantamount() + pOrderGrant_SKU.getGrantamount());
					grantskuService.save(sku);
				}
			}
			
			granttService.deleteById(entity.pordergrantid_link_src);
			
			response.data = sch;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<merger_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<merger_porder_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/cancel_pordergrant",method = RequestMethod.POST)
	public ResponseEntity<cancel_pordergrant_response> CancelPorderGrant(HttpServletRequest request,
			@RequestBody cancel_pordergrant_request entity) {
		cancel_pordergrant_response response = new cancel_pordergrant_response();
		
		try {
			POrderGrant grant = granttService.findOne(entity.porder_grantid_link);
			long porderid_link = grant.getPorderid_link();
			POrder porder = porderService.findOne(porderid_link);
			if(porder.getTotal_process() > 0) {
				response.mes = "Lệnh sản xuất đã vào chuyền không thể hủy!";
			}
			else {
				granttService.delete(grant);
				processService.deleteByOrderID(porderid_link);
				
				List<POrderGrant> list_grant = granttService.getByOrderId(porderid_link);
				if(list_grant.size() == 0) {
					porder.setStatus(POrderStatus.PORDER_STATUS_FREE);
					porderService.save(porder);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<cancel_pordergrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<cancel_pordergrant_response>(response, HttpStatus.OK);
		}
	} 
	
	@RequestMapping(value = "/break_porder",method = RequestMethod.POST)
	public ResponseEntity<break_porder_response> BreakPorder(HttpServletRequest request,
			@RequestBody break_porder_request entity) {
		break_porder_response response = new break_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		int producttivity = entity.producttivity;
		response.mes = "";
		try {
			//Kiem tra so luong tung sku xem co bi vuot qua khong
			List<POrderGrant_SKU> list_sku = entity.data;
			for (POrderGrant_SKU pOrderGrant_SKU : list_sku) {
				POrderGrant_SKU sku = grantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId(pOrderGrant_SKU.getSkuid_link(), pOrderGrant_SKU.getPordergrantid_link());
				if(pOrderGrant_SKU.getGrantamount() > sku.getGrantamount()) {
					response.mes = "Bạn không được tách vượt quá số lượng đang được giao cho tổ!";
					response.sku = grantskuService.getPOrderGrant_SKU(pOrderGrant_SKU.getPordergrantid_link());
					break;
				}
			}
			
			if(response.mes == "") {
				//Cập nhật lại grant cũ sau khi tách
				POrderGrant grant_old = granttService.findOne(entity.pordergrant_id_link);
				
				int total = grant_old.getGrantamount();
				int totalorder_old = grant_old.getGrantamount() - entity.quantity;
				Date start_old = grant_old.getStart_date_plan();
				start_old = commonService.getBeginOfDate(start_old);
				int duration_old = commonService.getDuration_byProductivity(totalorder_old, producttivity);
				
				Date end_old = commonService.Date_Add_with_holiday(start_old, duration_old - 1, orgrootid_link, year);
				end_old = commonService.getEndOfDate(end_old);
//				Date end_new = grant_old.getFinish_date_plan();
//				int productivity_old = commonService.getProductivity(totalorder_old, duration_old);
							
				grant_old.setGrantamount(totalorder_old);
				grant_old.setFinish_date_plan(end_old);
				grant_old.setDuration(duration_old);
				grant_old = granttService.save(grant_old);
				
				//Cap nhat lai Processing cu sau khi tach
				List<POrderProcessing> lsProcessing = processService.getByOrderId_and_GrantId(grant_old.getPorderid_link(), grant_old.getId());
				for(POrderProcessing process : lsProcessing) {
					process.setTotalorder(totalorder_old);
					processService.save(process);
				}
				
				Schedule_porder old = new Schedule_porder();
				old.setStartDate(start_old);
				old.setEndDate(end_old);
				old.setDuration(duration_old);
				old.setProductivity(producttivity);
				old.setName(grant_old.getMaHang());
				old.setMahang(grant_old.getMaHang());
				response.old_data = old;
				
				//Sinh grant moi
				Date start_new = commonService.Date_Add_with_holiday(end_old, 1, orgrootid_link, year);
				start_new= commonService.getBeginOfDate(start_new);
				
				int total_new = total - totalorder_old;
				int duration_new = commonService.getDuration_byProductivity(total_new, producttivity);
				Date end_new = commonService.Date_Add_with_holiday(start_new, duration_new - 1, orgrootid_link, year);
				end_new = commonService.getEndOfDate(end_new);
				
				POrderGrant grant = new POrderGrant();
				grant.setGranttoorgid_link(grant_old.getGranttoorgid_link());
				grant.setId(null);
				grant.setOrdercode(grant_old.getOrdercode());
				grant.setOrgrootid_link(orgrootid_link);
				grant.setPorderid_link(grant_old.getPorderid_link());
				grant.setTimecreated(new Date());
				grant.setUsercreatedid_link(user.getId());
				grant.setGrantdate(new Date());
				grant.setGrantamount(total_new);
				grant.setStatus(1);
				grant.setOrgrootid_link(orgrootid_link);
				grant.setStart_date_plan(start_new);
				grant.setFinish_date_plan(end_new);
				grant.setProductivity(producttivity);
				grant.setDuration(duration_new);
				grant = granttService.save(grant);
				
				POrder porder = porderService.findOne(entity.porderid_link);
				
				//Sinh 1 dong moi trong Processing
				POrderProcessing process = new POrderProcessing();
				process.setId(null);
//				process.setOrdercode(porder.getOrdercode());
				process.setOrderdate(porder.getOrderdate());
				process.setOrgrootid_link(orgrootid_link);
				process.setPorderid_link(porder.getId());
				process.setPordergrantid_link(grant.getId());
				process.setProcessingdate(new Date());
				process.setGranttoorgid_link(grant.getGranttoorgid_link());
				process.setTotalorder(grant.getGrantamount());
				process.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
				process.setUsercreatedid_link(user.getId());
				process.setTimecreated(new Date());
				processService.save(process);
				
				Schedule_porder new_data = new Schedule_porder();
				new_data.setCls(grant_old.getCls());
				new_data.setEndDate(end_new);
				new_data.setId_origin(grant_old.getPorderid_link());
				new_data.setMahang(grant.getMaHang(porder));
				new_data.setName(grant.getMaHang(porder));
				new_data.setResourceId(entity.resourceid);
				new_data.setStartDate(start_new);
				new_data.setDuration(duration_new);
				new_data.setTotalpackage(total_new);
				new_data.setProductivity(producttivity);
				new_data.setVendorname(grant_old.getVendorname());
				new_data.setBuyername(grant_old.getBuyername());
				new_data.setPordercode(grant_old.getOrdercode());
				new_data.setParentid_origin(entity.parentid_origin);
				new_data.setStatus(1);
				new_data.setPorder_grantid_link(grant.getId());
				new_data.setPorderid_link(grant.getPorderid_link());
				new_data.setPcontract_poid_link(grant_old.getPcontract_poid_link());
				new_data.setProductid_link(grant_old.getProductid_link());
				new_data.setPcontractid_link(grant_old.getPcontractid_link());
				response.new_data = new_data;
				 
				//gan sku vao grant moi sinh ra va tru sku o grant tach
				
				for (POrderGrant_SKU pOrderGrant_SKU : list_sku) {
					POrderGrant_SKU sku = new POrderGrant_SKU();
					sku.setGrantamount(pOrderGrant_SKU.getGrantamount());
					sku.setId(null);
					sku.setOrgrootid_link(orgrootid_link);
					sku.setPordergrantid_link(grant.getId());
					sku.setSkuid_link(pOrderGrant_SKU.getSkuid_link());
					grantskuService.save(sku);
					
					POrderGrant_SKU sku_old = grantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId(pOrderGrant_SKU.getSkuid_link(), pOrderGrant_SKU.getPordergrantid_link());
					sku_old.setGrantamount(sku_old.getGrantamount() - pOrderGrant_SKU.getGrantamount());
					grantskuService.save(sku_old);
				}
			}
			
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
