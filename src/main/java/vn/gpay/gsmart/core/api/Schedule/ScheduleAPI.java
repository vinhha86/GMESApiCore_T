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
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder.POrder_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleAPI {
	@Autowired IHolidayService holidayService;
	@Autowired OrgServiceImpl orgService;
	@Autowired POrder_Service porderService;
	@Autowired POrderGrant_Service granttService;
	
	@RequestMapping(value = "/getplan",method = RequestMethod.POST)
	public ResponseEntity<get_schedule_porder_response> GetAll(HttpServletRequest request,
			@RequestParam String listid) throws ParseException{
		get_schedule_porder_response response = new get_schedule_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int year = Calendar.getInstance().get(Calendar.YEAR);
		long orgid_link = user.getOrgid_link();
		
		String sDate1="01/06/2020";  
	    Date startdate = new SimpleDateFormat("dd/MM/yyyy").parse(sDate1);  
	    
	    String stodate="01/12/2020";  
	    Date toDate = new SimpleDateFormat("dd/MM/yyyy").parse(stodate);  
		
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
				sch_holiday.setEndDate(holiday.getDay());
				
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
					
					sch_org.getChildren().add(sch_org_grant);
					id++;
					
					//Lấy các lệnh của các tổ
					
					List<POrderGrant> list_porder = granttService.get_granted_bygolivedate(startdate, toDate, org_grant.getId());
					for(POrderGrant pordergrant : list_porder) {
						Schedule_porder sch_porder = new Schedule_porder();
						sch_porder.setCls(pordergrant.getCls());
						sch_porder.setEndDate(pordergrant.getGolivedate());
						sch_porder.setId_origin(pordergrant.getId());
						sch_porder.setMahang(pordergrant.getMaHang());
						sch_porder.setName(pordergrant.getMaHang());
						sch_porder.setResourceId(sch_org_grant.getId());
						sch_porder.setStartDate(pordergrant.getProductiondate());
						
						response.events.rows.add(sch_porder);
					}
				}
				
				//Lay nhung lenh chua phan chuyen cua moi nha may
				Schedule_plan porder_free = new Schedule_plan();
				porder_free.setExpanded(false);
				porder_free.setId(id);
				porder_free.setId_origin(0);
				porder_free.setLeaf(false);
				porder_free.setName("Chưa phân chuyền");
				porder_free.setIconCls("x-fa fa-file-o");
				porder_free.setParentid_origin(org_factory.getId());
				id++;
				
				List<POrder> listporder_free = porderService.get_free_bygolivedate(startdate, toDate, org_factory.getId());
				for(POrder porderfree : listporder_free) {
					Schedule_plan sch_porderfree = new Schedule_plan();
					
					sch_porderfree.setExpanded(false);
					sch_porderfree.setId(id);
					sch_porderfree.setId_origin(0);
					sch_porderfree.setLeaf(true);
					sch_porderfree.setName(porderfree.getOrdercode());
					sch_porderfree.setIconCls("x-fa fa-industry");
					sch_porderfree.setParentid_origin(org_factory.getId());
					
					porder_free.getChildren().add(sch_porderfree);
					id++;
				}
				
				sch_org.getChildren().add(porder_free);
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
}
