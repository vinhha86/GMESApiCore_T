package vn.gpay.gsmart.core.api.Schedule;

import java.text.DateFormat;
//import java.io.File;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;

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

import vn.gpay.gsmart.core.Log4jCommon;
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
import vn.gpay.gsmart.core.pcontract_po.PContractPO_Shipping;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.personel.IPersonnel_Service;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.porders_poline.IPOrder_POLine_Service;
import vn.gpay.gsmart.core.porders_poline.POrder_POLine;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.security.GpayUserOrg;
import vn.gpay.gsmart.core.security.IGpayUserOrgService;
import vn.gpay.gsmart.core.sizesetattributevalue.ISizeSetAttributeRepository;
import vn.gpay.gsmart.core.sizesetattributevalue.SizeSetAttributeValue;
import vn.gpay.gsmart.core.sku.ISKU_AttValue_Repository;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.task_object.Task_Object;
import vn.gpay.gsmart.core.timesheet_absence.ITimesheetAbsenceService;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.OrgType;
import vn.gpay.gsmart.core.utils.POType;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/schedule")
public class ScheduleAPI {
	@Autowired
	IHolidayService holidayService;
	@Autowired
	OrgServiceImpl orgService;
	@Autowired
	IPOrder_Service porderService;
	@Autowired
	IPOrder_Product_SKU_Service porderSkuService;
	@Autowired
	IPOrderGrant_Service granttService;
	@Autowired
	Common commonService;
	@Autowired
	IPOrderProcessing_Service processService;
	@Autowired
	IPOrder_Req_Service reqService;
	@Autowired
	IPContractService pcontractService;
	@Autowired
	IPContract_POService poService;
	@Autowired
	ITask_Object_Service taskobjectService;
	@Autowired
	ITask_CheckList_Service checklistService;
	@Autowired
	ITask_Service taskService;
	@Autowired
	IPOrderGrant_SKUService grantskuService;
	@Autowired
	IPContractProductSKUService poskuService;
	@Autowired
	IPOrder_POLine_Service porderlineService;

	@Autowired
	IGpayUserOrgService userOrgService;
	@Autowired
	IPersonnel_Service personnelService;
	@Autowired
	ITimesheetAbsenceService timesheet_absence_Service;
	@Autowired
	IProductService productService;
	@Autowired
	ISizeSetAttributeRepository sizesetatt_repo;
	@Autowired
	ISKU_AttValue_Repository sku_av_repo;
	@Autowired
	Log4jCommon logcommon;

	@RequestMapping(value = "/getplan", method = RequestMethod.POST)
	public ResponseEntity<get_schedule_porder_response> GetAll(HttpServletRequest request, @RequestParam String listid,
			@RequestParam String startDate, @RequestParam String endDate, @RequestParam String PO_code,
			@RequestParam String contractcode, @RequestParam String Buyer, @RequestParam String Vendor,
			@RequestParam Boolean isReqPorder, @RequestParam Boolean isAllgrant) throws ParseException {
		get_schedule_porder_response response = new get_schedule_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		// int year = Calendar.getInstance().get(Calendar.YEAR);
		long orgid_link = user.getOrgid_link();
		long orgbuyerid_link = Buyer == "" ? (long) 0 : Long.parseLong(Buyer);
		long orgvendorid_link = Vendor == "" ? (long) 0 : Long.parseLong(Vendor);

		// Lay danh sach Vendor duoc phep quan ly (overwrite dieu kien seach)
		List<GpayUserOrg> lsVendor = userOrgService.getall_byuser_andtype(user.getId(), OrgType.ORG_TYPE_VENDOR);
		if (lsVendor.size() > 0) {
			orgvendorid_link = lsVendor.get(0).getOrgid_link();
		}

		Date startdate = commonService
				.getBeginOfDate(new SimpleDateFormat("yyyy-MM-dd").parse(startDate.substring(0, 10)));
		Date toDate = commonService.getEndOfDate(new SimpleDateFormat("yyyy-MM-dd").parse(endDate.substring(0, 10)));

		String[] listtype = listid.split(",");
		List<String> list = new ArrayList<String>();
		for (String string : listtype) {
			list.add(string);
		}

		try {
			// Lay danh sach ngày nghỉ
			List<Integer> list_year = new ArrayList<Integer>();
			Calendar c_startdate = Calendar.getInstance();
			c_startdate.setTime(startdate);
			list_year.add(c_startdate.get(Calendar.YEAR));

			Calendar c_todate = Calendar.getInstance();
			c_todate.setTime(toDate);
			list_year.add(c_todate.get(Calendar.YEAR));

			List<Holiday> list_holiday = holidayService.getby_many_year(orgrootid_link, list_year);
			for (Holiday holiday : list_holiday) {
				Schedule_holiday sch_holiday = new Schedule_holiday();
				sch_holiday.setComment(holiday.getComment());
				sch_holiday.setStartDate(holiday.getDay());
				sch_holiday.setEndDate(Common.Date_Add(holiday.getDay(), 1));
				sch_holiday.setCls("holiday");

				response.zones.rows.add(sch_holiday);
			}

			// Lay ngay chu nhat
			List<Date> list_sunday = commonService.getList_SunDay_between_time(startdate, toDate);
			for (Date date : list_sunday) {
				Schedule_holiday sch_sunday = new Schedule_holiday();
				sch_sunday.setComment("");
				sch_sunday.setStartDate(date);
				sch_sunday.setEndDate(Common.Date_Add(date, 1));
				sch_sunday.setCls("sunday");

				response.zones.rows.add(sch_sunday);
			}

			// Lay danh sach nha may va to
			List<Schedule_plan> list_sch_plan = new ArrayList<Schedule_plan>();
			orgid_link = orgid_link == 0 ? 1 : orgid_link;
			Org orgroot = orgService.findOne(orgid_link);

			// Lay danh sach nhung don vi duoc phep xem cua user dang dang nhap
			List<Org> listorg = new ArrayList<Org>();
			if (orgroot.getOrgtypeid_link() == 1)
				listorg = orgService.getorgChildrenbyOrg(orgid_link, list);
			else {
				// Them vao danh sach cac don vi trong danh sach phan xuong duoc phan quyen cho
				// nguoi dung
				for (GpayUserOrg userorg : userOrgService.getall_byuser_andtype(user.getId(),
						OrgType.ORG_TYPE_FACTORY)) {
					listorg.add(orgService.findOne(userorg.getOrgid_link()));
				}
				if (!listorg.contains(orgroot))
					listorg.add(orgroot);
			}

			long id = 1;

			for (Org org_factory : listorg) {
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
				// Lấy các tổ của nhà máy
				List<Org> listorg_grantt = orgService.getorgChildrenbyOrg(orgid, list);

				CountDownLatch latch = new CountDownLatch(listorg_grantt.size());
				List<Schedule_porder> rows_grant = new ArrayList<Schedule_porder>();

				for (Org org_grant : listorg_grantt) {
					Schedule_plan sch_org_grant = new Schedule_plan();

					// Lấy lao động định biên
					int total_personnel = personnelService.GetSizePersonnelByGrant(org_grant.getId(), null);

					// Lấy số lao động nghỉ trong ngày
					int total_absence = timesheet_absence_Service.GetTimeSheetAbsenceByDate(org_grant.getId(),
							new Date());
					int total_working = total_personnel - total_absence;

					sch_org_grant.setId(id);
					sch_org_grant.setCode(org_grant.getCode());
					sch_org_grant.setExpanded(false);
					sch_org_grant.setIconCls("x-fa fa-sliders");
					sch_org_grant.setId_origin(org_grant.getId());
					sch_org_grant.setLeaf(false);
					sch_org_grant.setName(org_grant.getName() + " (" + total_working + "/" + total_personnel + ")");
					sch_org_grant.setOrgtypeid_link(org_grant.getOrgtypeid_link());
					sch_org_grant.setParentid_origin(org_grant.getParentid_link());
					sch_org_grant.setType(1);

					id++;

					// Them dong thong tin tien do
					Schedule_plan sch_process = new Schedule_plan();
					sch_process.setId(id);
					sch_process.setExpanded(false);
					sch_process.setIconCls("x-fa fa-line-chart");
					sch_process.setLeaf(true);
					sch_process.setName("Tiến độ");
					sch_process.setType(2);
					sch_org_grant.getChildren().add(sch_process);
					id++;

					// Them dong thong tin du doan tien do
					Schedule_plan sch_estimation = new Schedule_plan();
					sch_estimation.setId(id);
					sch_estimation.setExpanded(false);
					sch_estimation.setIconCls("x-fa fa-binoculars");
					sch_estimation.setLeaf(true);
					sch_estimation.setName("Dự báo");
					sch_estimation.setType(3);
					sch_org_grant.getChildren().add(sch_estimation);
					id++;

					sch_org.getChildren().add(sch_org_grant);

					ScheduleOrgGrant theScheduleOrgGrant = new ScheduleOrgGrant(sch_org_grant, sch_process,
							sch_estimation, org_grant, startdate, toDate, PO_code, contractcode, orgbuyerid_link,
							orgvendorid_link, orgid, orgrootid_link, rows_grant, granttService, commonService,
							processService, latch);
					theScheduleOrgGrant.start();

				}
				latch.await();
				response.events.rows.addAll(rows_grant);

				// Lay nhung lenh chua phan chuyen cua moi nha may
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
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<get_schedule_porder_response>(response, HttpStatus.OK);
	}

	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<update_schedule_response> Update(HttpServletRequest request,
			@RequestBody update_schedule_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		update_schedule_response response = new update_schedule_response();
		try {
			Schedule_porder event = entity.data;
			Date start = event.getStartDate();
			Date end = event.getEndDate();
			long orgrootid_link = user.getRootorgid_link();

			int type = 0;
			List<PContract_PO> list_po = porderSkuService.getListPO_ByGrant(entity.data.getPorder_grantid_link());
			Date shipdate = list_po.get(0).getShipdate();
			for (PContract_PO po : list_po) {
				if (po.getShipdate().after(shipdate))
					shipdate = po.getShipdate();
			}
			if (end.after(shipdate))
				type = 1;

			int duration = commonService.getDuration(start, end, orgrootid_link);
			int productivity = commonService.getProductivity(event.getTotalpackage(), duration);
			event.setDuration(duration);
			event.setProductivity(productivity);
			event.setStartDate(commonService.getBeginOfDate(entity.data.getStartDate()));
			event.setEndDate(commonService.getEndOfDate(entity.data.getEndDate()));
			event.setGrant_type(type);

			// update vao grant
			long pordergrantid_link = entity.data.getPorder_grantid_link();
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			grant.setStart_date_plan(entity.data.getStartDate());
			grant.setFinish_date_plan(commonService.getPrevious(entity.data.getEndDate()));
			grant.setDuration(duration);
			grant.setProductivity(productivity);
			grant.setReason_change(null);
			grant.setType(type);
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

	@RequestMapping(value = "/showhide_image", method = RequestMethod.POST)
	public ResponseEntity<ShowHideImage_response> ShowHideImage(HttpServletRequest request,
			@RequestBody ShowHideImage_request entity) {
		ShowHideImage_response response = new ShowHideImage_response();
		try {
			long id = entity.pordergrantid_link;
			POrderGrant grant = granttService.findOne(id);
			grant.setIs_show_image(entity.is_show);
			granttService.save(grant);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ShowHideImage_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ShowHideImage_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update_porder", method = RequestMethod.POST)
	public ResponseEntity<update_porder_response> UpdatePorder(HttpServletRequest request,
			@RequestBody update_porder_request entity) {
		update_porder_response response = new update_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();

			// update vao grantt
			long pordergrantid_link = entity.pordergrantid_link;
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			int duration = commonService.getDuration(entity.StartDate, entity.EndDate, orgrootid_link);
			int productivity = commonService.getProductivity(grant.getGrantamount(), response.duration);

			POrder req = porderService.findOne(grant.getPorderid_link());
			int type = 0;
			if (entity.EndDate.after(req.getShipdate()))
				type = 1;

			grant.setStart_date_plan(entity.StartDate);
			grant.setFinish_date_plan(entity.EndDate);
			grant.setDuration(duration);
			grant.setProductivity(productivity);
			grant.setReason_change(null);
			grant.setType(type);
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

	@RequestMapping(value = "/move_porder", method = RequestMethod.POST)
	public ResponseEntity<move_porder_response> MovePorder(HttpServletRequest request,
			@RequestBody move_porder_request entity) {
		move_porder_response response = new move_porder_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long porderid_link = entity.porderid_link;
			long pordergrantid_link = entity.pordergrant_id_link;

			// Cap nhat lai grant
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			// Giu lai grantorg cu de update Porder_processing
			int duration = entity.schedule.getDuration();
			Date end_date = commonService.Date_Add_with_holiday(entity.startdate, duration - 1, orgrootid_link);

			int type = 0;
			Date shipdate_max = null;

			if (grant.getStatus() == 2) {
				List<PContract_PO> list_po = porderSkuService.getListPO_ByGrant(pordergrantid_link);
				shipdate_max = list_po.get(0).getShipdate();
				for (PContract_PO po : list_po) {
					if (po.getShipdate().after(shipdate_max))
						shipdate_max = po.getShipdate();
				}
			} else {
				POrder porder = porderService.findOne(grant.getPorderid_link());
				shipdate_max = porder.getShipdate();
			}

			if (end_date.after(shipdate_max))
				type = 1;

			grant.setGranttoorgid_link(entity.orggrant_toid_link);
			grant.setStart_date_plan(commonService.getBeginOfDate(entity.startdate));
			grant.setFinish_date_plan(commonService.getEndOfDate(end_date));
			grant.setReason_change(null);
			grant.setType(type);
			grant = granttService.save(grant);

			// log lai
			Log4jCommon.move_pordergrant(user.getUsername(), grant.getId(), entity.orggrant_toid_link,
					entity.orggrant_toid_link, "move_porder");

			// Cap nhat lai Porder_processing
			List<POrderProcessing> lsProcessing = processService.getByOrderId_and_GrantId(porderid_link,
					pordergrantid_link);
			if (lsProcessing.size() > 0) {
				POrderProcessing theProcess = lsProcessing.get(0);
				theProcess.setGranttoorgid_link(entity.orggrant_toid_link);
				processService.save(theProcess);
			} else {
				// Tao moi POrderProcessing
				POrderProcessing pp = new POrderProcessing();
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

			Schedule_porder sch = entity.schedule;
			sch.setEndDate(commonService.getEndOfDate(end_date));
			sch.setStartDate(grant.getStart_date_plan());
			sch.setPorder_grantid_link(entity.pordergrant_id_link);
			sch.setGrant_type(type);

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

	@RequestMapping(value = "/update_porder_productivity", method = RequestMethod.POST)
	public ResponseEntity<update_porder_productivity_response> UpdatePorderProductivity(HttpServletRequest request,
			@RequestBody update_porder_productivity_request entity) {
		update_porder_productivity_response response = new update_porder_productivity_response();
		try {
			Date end_date = commonService.getEndOfDate(entity.data.getEndDate());
			int type = 0;
			Date shipdate = null;

			if (entity.data.getStatus() == 2) {
				List<PContract_PO> list_po = porderSkuService.getListPO_ByGrant(entity.data.getPorder_grantid_link());
				if (list_po.size() > 0) {
					shipdate = list_po.get(0).getShipdate();
					for (PContract_PO po : list_po) {
						if (po.getShipdate().after(shipdate))
							shipdate = po.getShipdate();
					}
				}

			} else {
				POrder porder = porderService.findOne(entity.data.getPorderid_link());
				shipdate = porder.getShipdate();
			}

			if (shipdate != null && end_date.after(shipdate))
				type = 1;
//			porder.setProductiondate_plan(entity.data.getStartDate());
//			porder.setFinishdate_plan(commonService.getEndOfDate(entity.data.getEndDate()));
//			porderService.save(porder);

			long pordergrantid_link = entity.data.getPorder_grantid_link();
			POrderGrant grant = granttService.findOne(pordergrantid_link);
			grant.setStart_date_plan(commonService.getBeginOfDate(entity.data.getStartDate()));
			grant.setFinish_date_plan(end_date);
			grant.setDuration(entity.data.getDuration());
			grant.setProductivity(entity.data.getProductivity());
			grant.setReason_change(null);
			grant.setType(type);
			granttService.save(grant);

			Schedule_porder sch = entity.data;
			sch.setEndDate(end_date);
			sch.setGrant_type(type);

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

	@RequestMapping(value = "/create_pordergrant", method = RequestMethod.POST)
	public ResponseEntity<create_pordergrant_response> CreatePorderGrant(HttpServletRequest request,
			@RequestBody create_pordergrant_request entity) {
		create_pordergrant_response response = new create_pordergrant_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			POrder porder = porderService.findOne(entity.porderid_link);

			Date startDate = commonService.getBeginOfDate(porder.getProductiondate_plan());
			Date endDate = commonService.getEndOfDate(porder.getFinishdate_plan());

			int duration = commonService.getDuration(startDate, endDate, orgrootid_link);
			int productivity = porder.getPlan_productivity();

			if (productivity == 0) {
				productivity = commonService.getProductivity(porder.getTotalorder(), duration);
			}

			porder.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
			porderService.save(porder);
			int type = endDate.after(porder.getShipdate()) ? 1 : 0;
			// Tao POrder_grant
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
			pg.setTotalamount_tt(porder.getTotalorder());
			pg.setDuration(duration);
			pg.setType(type);

			pg = granttService.save(pg);

			// log lai
			Log4jCommon.move_pordergrant(user.getUsername(), pg.getId(), entity.orggrantto, entity.orggrantto,
					"create_pordergrant");

			// Lay toan bo SKU tu POrder sang POrder_grant_sku
			for (POrder_Product_SKU pSKU : porder.getPorder_product_sku()) {
				POrderGrant_SKU pgSKU = new POrderGrant_SKU();
				pgSKU.setOrgrootid_link(orgrootid_link);
				pgSKU.setSkuid_link(pSKU.getSkuid_link());
				pgSKU.setGrantamount(pSKU.getPquantity_total());
				pgSKU.setPordergrantid_link(pg.getId());
				pgSKU.setPcontract_poid_link(pSKU.getPcontract_poid_link());
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
			sch.setProductivity_po(porder.getProductivity_po());
			sch.setProductivity_porder(0);
			sch.setPcontract_poid_link(porder.getPcontract_poid_link());
			sch.setPcontractid_link(porder.getPcontractid_link());
			sch.setProductbuyercode(porder.getProductcode());
			sch.setPorderid_link(porder.getId());
			sch.setProductid_link(porder.getProductid_link());
			sch.setGrant_type(type);

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

	@RequestMapping(value = "/create_pordergrant_test", method = RequestMethod.POST)
	public ResponseEntity<create_pordergrant_response> CreatePorderGrantTest(HttpServletRequest request,
			@RequestBody create_porder_test_request entity) {
		create_pordergrant_response response = new create_pordergrant_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {

			POrder_Req req = reqService.findOne(entity.porder_reqid_link);
			req.setStatus(1);
			reqService.save(req);

			int productivity = req.get_ProductivityPO();
			Date startDate = commonService.getBeginOfDate(req.getPO_Productiondate());
			Calendar c_startdate = Calendar.getInstance();
			c_startdate.setTime(startDate);
			// Kiem tra ngay bat dau ma la ngay nghi thi tang len ngay di lam tiep theo
			if (commonService.check_dayoff(c_startdate, orgrootid_link)) {
				startDate = commonService.Date_Add_with_holiday(startDate, 1, orgrootid_link);
				startDate = commonService.getBeginOfDate(startDate);
			}
			Date endDate = commonService.Date_Add_with_holiday(startDate, req.getDuration() - 1, orgrootid_link);
			endDate = commonService.getEndOfDate(endDate);

			int duration = req.getDuration();

			if (productivity == 0) {
				productivity = commonService.getProductivity(req.getTotalorder(), duration);
				duration = commonService.getDuration_byProductivity(req.getTotalorder(), productivity);
				endDate = commonService.Date_Add_with_holiday(startDate, duration - 1, orgrootid_link);
			}
			int type = endDate.after(req.getShipdate()) ? 1 : 0;

			String po_code = req.getPo_buyer().length() > 0 ? req.getPo_vendor() : req.getPo_buyer();
			POrder porder = new POrder();
			porder.setOrdercode(po_code);
			porder.setFinishdate_plan(endDate);
			porder.setGolivedate(req.getShipdate());
			porder.setStatus(-1);
			porder.setGranttoorgid_link(req.getGranttoorgid_link());
			porder.setId(null);
			porder.setOrgrootid_link(orgrootid_link);
			porder.setPcontract_poid_link(req.getPcontract_poid_link());
			porder.setPcontractid_link(req.getPcontractid_link());
			porder.setProductiondate(startDate);
			porder.setUsercreatedid_link(user.getId());
			porder.setTimecreated(new Date());
			porder.setProductiondate_plan(req.getPO_Productiondate());
			porder.setPorderreqid_link(entity.porder_reqid_link);
			porder.setTotalorder(req.getTotalorder());
			porder.setProductid_link(req.getProductid_link());
			porder.setPlan_productivity(productivity);
			porder = porderService.saveAndFlush(porder);

//			Date endDate = commonService.getEndOfDate(porder.getFinishdate_plan());
//			int duration = commonService.getDuration(startDate, endDate, orgrootid_link, year);

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
			pg.setProductivity(productivity);
			pg.setDuration(duration);
			pg.setStart_date_plan(startDate);
			pg.setFinish_date_plan(endDate);
			pg.setTotalamount_tt(req.getTotalorder());
			pg.setType(type);
			pg = granttService.save(pg);

			// log lai
			Log4jCommon.move_pordergrant(user.getUsername(), pg.getId(), entity.orggrantto, entity.orggrantto,
					"create_pordergrant_test");

			PContract contract = req.getPcontract();
			PContract_PO po = req.getPcontract_po();
			Product product = req.getProduct();

			String name = "";
			int total = pg.getGrantamount() == null ? 0 : pg.getGrantamount();
			float totalPO = po == null ? 0 : po.getPo_quantity();

			DecimalFormat decimalFormat = new DecimalFormat("#,###");
			decimalFormat.setGroupingSize(3);

			if (po != null && product != null) {
				String productcode = product.getBuyercode();
				name += productcode + "/" + decimalFormat.format(total) + "/" + decimalFormat.format(totalPO);
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
			sch.setResourceId(entity.resourceid);
			sch.setStartDate(startDate);
			sch.setStatus(-1);
			sch.setTotalpackage(porder.getTotalorder());
			sch.setVendorname(contract.getVendorname());
			sch.setPorder_grantid_link(pg.getId());
			sch.setPorderid_link(porder.getId());
			sch.setProductivity_po(productivity);
			sch.setProductivity_porder(productivity);
			sch.setPcontract_poid_link(req.getPcontract_poid_link());
			sch.setProductid_link(req.getProductid_link());
			sch.setPcontractid_link(porder.getPcontractid_link());
			sch.setGrant_type(type);

			response.data = sch;

			// Cap nhat lai check list trong taskboard
			long objecttypeid_link = 8;
			List<Task_Object> listobj = taskobjectService.getbyObjectType_and_objectid_link(objecttypeid_link,
					entity.porder_reqid_link);
			if (listobj.size() > 0) {
				Task_Object obj = listobj.get(0);
				long taskid_link = obj.getTaskid_link();
				long tasktype_checklits_id_link = 1; // id trong DB
				List<Task_CheckList> checklist = checklistService.getby_taskid_link_and_typechecklist(taskid_link,
						tasktype_checklits_id_link);
				if (checklist.size() > 0) {
					Task_CheckList subTask = checklist.get(0);
					String description = subTask.getDescription();
					description += " (" + user.getFullname() + ")";
					subTask.setDone(true);
					subTask.setDatefinished(new Date());
					subTask.setUserfinishedid_link(user.getId());
					subTask.setDescription(description);
					checklistService.save(subTask);

					int status = 1;
					// Kiem tra cong viec hoan thanh het chua
					List<Task_CheckList> list_sub = checklistService.getby_taskid_link(taskid_link);
					list_sub.removeIf(c -> c.getDone() == true);
					if (list_sub.size() == 0)
						status = 2;
					Task task = taskService.findOne(taskid_link);
					task.setStatusid_link(status);
					if (status == 2) {
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

	@RequestMapping(value = "/create_many_pordergrant_test", method = RequestMethod.POST)
	public ResponseEntity<create_many_pordergrant_test_response> Create_Many_PorderGrantTest(HttpServletRequest request,
			@RequestBody create_many_pordertest_request entity) {
		create_many_pordergrant_test_response response = new create_many_pordergrant_test_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			List<POrder_Req> list_req = new ArrayList<POrder_Req>();
			long pcontractpo_id_link = entity.pcontract_poid_link;
			long productid_link = entity.productid_link;
			long orgid_link = entity.orgid_link;

			list_req = reqService.getbyOffer_and_Product(pcontractpo_id_link, productid_link, orgid_link);

			List<Schedule_porder> list = new ArrayList<Schedule_porder>();
			Date start_before = null, end_before = null;

			for (POrder_Req req : list_req) {
				req.setStatus(1);
				reqService.save(req);

				int productivity = req.get_ProductivityPO();
				Date startDate = null;
				if (start_before == null) {
					startDate = commonService.getBeginOfDate(req.getPO_Productiondate());
					Calendar c_startdate = Calendar.getInstance();
					c_startdate.setTime(startDate);
					if (commonService.check_dayoff(c_startdate, orgrootid_link)) {
						startDate = commonService.Date_Add_with_holiday(startDate, 1, orgrootid_link);
					}
				} else {
					startDate = commonService.Date_Add_with_holiday(end_before, 1, orgrootid_link);
				}
				// Kiem tra ngay bat dau ma la ngay nghi thi tang len ngay di lam tiep theo

				startDate = commonService.getBeginOfDate(startDate);

				Date endDate = null;
				Integer duration = req.getDuration();
				if (duration != null) {
					endDate = commonService.Date_Add_with_holiday(startDate, req.getDuration() - 1, orgrootid_link);
					endDate = commonService.getEndOfDate(endDate);
				}

				if (productivity == 0) {
					productivity = commonService.getProductivity(req.getTotalorder(), duration);
					duration = commonService.getDuration_byProductivity(req.getTotalorder(), productivity);
					endDate = commonService.Date_Add_with_holiday(startDate, duration - 1, orgrootid_link);
				}

				start_before = startDate;
				end_before = endDate;

				int type = endDate.after(req.getShipdate()) ? 1 : 0;

				String po_code = req.getPo_buyer().length() > 0 ? req.getPo_vendor() : req.getPo_buyer();
				POrder porder = new POrder();
				porder.setOrdercode(po_code);
				porder.setFinishdate_plan(endDate);
				porder.setGolivedate(req.getShipdate());
				porder.setStatus(-1);
				porder.setGranttoorgid_link(req.getGranttoorgid_link());
				porder.setId(null);
				porder.setOrgrootid_link(orgrootid_link);
				porder.setPcontract_poid_link(req.getPcontract_poid_link());
				porder.setPcontractid_link(req.getPcontractid_link());
				porder.setProductiondate(startDate);
				porder.setUsercreatedid_link(user.getId());
				porder.setTimecreated(new Date());
				porder.setProductiondate_plan(req.getPO_Productiondate());
				porder.setPorderreqid_link(req.getId());
				porder.setTotalorder(req.getTotalorder());
				porder.setProductid_link(req.getProductid_link());
				porder.setPlan_productivity(productivity);
				porder.setStatus(POrderStatus.PORDER_STATUS_UNCONFIRM);
				porder = porderService.save(porder);

//				Date endDate = commonService.getEndOfDate(porder.getFinishdate_plan());
//				int duration = commonService.getDuration(startDate, endDate, orgrootid_link, year);

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
				pg.setProductivity(productivity);
				pg.setDuration(duration);
				pg.setStart_date_plan(startDate);
				pg.setFinish_date_plan(endDate);
				pg.setTotalamount_tt(req.getTotalorder());
				pg.setType(type);
				pg = granttService.save(pg);

				// log lai
				Log4jCommon.move_pordergrant(user.getUsername(), pg.getId(), entity.orggrantto, entity.orggrantto,
						"create_many_pordergrant_test");

				PContract contract = req.getPcontract();
				PContract_PO po = req.getPcontract_po();
				Product product = req.getProduct();

				String name = "";
				int total = pg.getGrantamount() == null ? 0 : pg.getGrantamount();
				float totalPO = po == null ? 0 : po.getPo_quantity();

				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				decimalFormat.setGroupingSize(3);

				if (po != null && product != null) {
					String productcode = product.getBuyercode();
					String PO = po.getPo_buyer() == null ? "" : po.getPo_vendor();
					name += productcode + "/" + PO + "/" + decimalFormat.format(total) + "/"
							+ decimalFormat.format(totalPO);
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
				sch.setResourceId(entity.resourceid);
				sch.setStartDate(startDate);
				sch.setStatus(-1);
				sch.setTotalpackage(porder.getTotalorder());
				sch.setVendorname(contract.getVendorname());
				sch.setPorder_grantid_link(pg.getId());
				sch.setPorderid_link(porder.getId());
				sch.setProductivity_po(productivity);
				sch.setProductivity_porder(productivity);
				sch.setPcontract_poid_link(req.getPcontract_poid_link());
				sch.setProductid_link(req.getProductid_link());
				sch.setPcontractid_link(porder.getPcontractid_link());
				sch.setGrant_type(type);

				list.add(sch);
			}

			response.data = list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_many_pordergrant_test_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_many_pordergrant_test_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/create_many_pordergrant", method = RequestMethod.POST)
	public ResponseEntity<create_many_porder_response> Create_Many_PorderGrant(HttpServletRequest request,
			@RequestBody create_many_pordertest_request entity) {
		create_many_porder_response response = new create_many_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			List<POrder> list_porder = new ArrayList<POrder>();
			long pcontractpo_id_link = entity.pcontract_poid_link;
			long productid_link = entity.productid_link;
			long orgid_link = entity.orgid_link;

			list_porder = porderService.getby_offer(pcontractpo_id_link, productid_link, orgid_link);

			List<Schedule_porder> list = new ArrayList<Schedule_porder>();
			Date start_before = null, end_before = null;

			for (POrder porder : list_porder) {
				Date startDate = null;
				if (start_before == null) {
					Date productiondate_plan = porder.getProductiondate_plan();
					productiondate_plan = productiondate_plan == null
							? commonService.Date_Add_with_holiday(porder.getFinishdate_plan(), -30, orgrootid_link)
							: productiondate_plan;
					startDate = commonService.getBeginOfDate(productiondate_plan);
					Calendar c_startdate = Calendar.getInstance();
					c_startdate.setTime(startDate);
					if (commonService.check_dayoff(c_startdate, orgrootid_link)) {
						startDate = commonService.Date_Add_with_holiday(startDate, 1, orgrootid_link);
					}
				} else {
					startDate = commonService.Date_Add_with_holiday(end_before, 1, orgrootid_link);
				}
				// Kiem tra ngay bat dau ma la ngay nghi thi tang len ngay di lam tiep theo
				Date endDate = null;
				int duration = porder.getDuration();
				startDate = commonService.getBeginOfDate(startDate);
				if (duration != -1)
					endDate = commonService.Date_Add_with_holiday(startDate, porder.getDuration() - 1, orgrootid_link);
				else
					endDate = porder.getFinishdate_plan();
				endDate = commonService.getEndOfDate(endDate);

				start_before = startDate;
				end_before = endDate;

//				Date startDate = commonService.getBeginOfDate(porder.getProductiondate_plan());
//				Date endDate = commonService.getEndOfDate(porder.getFinishdate_plan());

				int productivity = porder.getPlan_productivity();

				if (productivity == 0) {
					productivity = commonService.getProductivity(porder.getTotalorder(), duration);
				}

				porder.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
				porderService.save(porder);
				int type = endDate.after(porder.getShipdate()) ? 1 : 0;
				// Tao POrder_grant
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
				pg.setStart_date_plan(startDate);
				pg.setFinish_date_plan(endDate);
				pg.setProductivity(productivity);
				pg.setTotalamount_tt(porder.getTotalorder());
				pg.setDuration(duration);
				pg.setType(type);

				pg = granttService.save(pg);

				// log lai
				Log4jCommon.move_pordergrant(user.getUsername(), pg.getId(), entity.orggrantto, entity.orggrantto,
						"create_many_pordergrant");

				// Lay toan bo SKU tu POrder sang POrder_grant_sku
				for (POrder_Product_SKU pSKU : porder.getPorder_product_sku()) {
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
				sch.setProductivity_po(porder.getProductivity_po());
				sch.setProductivity_porder(0);
				sch.setPcontract_poid_link(porder.getPcontract_poid_link());
				sch.setPcontractid_link(porder.getPcontractid_link());
				sch.setProductbuyercode(porder.getProductcode());
				sch.setPorderid_link(porder.getId());
				sch.setGrant_type(type);

				list.add(sch);
			}

			response.data = list;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_many_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_many_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/delete_porder_test", method = RequestMethod.POST)
	public ResponseEntity<delete_porder_req_response> DeletePorderTest(HttpServletRequest request,
			@RequestBody delete_porder_req_request entity) {
		delete_porder_req_response response = new delete_porder_req_response();
		long porderid_link = entity.porderid_link;
		long pordergrantid_link = entity.pordergrantid_link;
		POrder porder = porderService.findOne(porderid_link);
		try {
			// xoa trong bang poder_grant
			granttService.deleteById(pordergrantid_link);

			// Xoa trong bang porder neu porder khong con grant
			List<POrderGrant> list_grant = granttService.getByOrderId(porderid_link);
			if (list_grant.size() == 0) {
				porderService.deleteById(porderid_link);

				POrder_Req req = reqService.findOne(porder.getPorderreqid_link());
				req.setStatus(POrderReqStatus.STATUS_FREE);
				reqService.save(req);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<delete_porder_req_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<delete_porder_req_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update_date", method = RequestMethod.POST)
	public ResponseEntity<update_duration_porder_response> UpdateStartDate(HttpServletRequest request,
			@RequestBody update_duration_porder_request entity) {
		update_duration_porder_response response = new update_duration_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			int duration = commonService.getDuration(entity.data.getStartDate(), entity.data.getEndDate(),
					orgrootid_link);
			int productivity = commonService.getProductivity(entity.data.getTotalpackage(), duration);

			Schedule_porder sch = entity.data;
			sch.setDuration(duration);
			sch.setProductivity(productivity);

			response.data = sch;

			// Xoa grant khoi danh sach bi thay doi nang suat boi ngay nghi le
			POrderGrant grant = granttService.findOne(entity.data.getPorder_grantid_link());
			grant.setReason_change(null);
			granttService.save(grant);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/duration_change", method = RequestMethod.POST)
	public ResponseEntity<duration_change_response> DurationChange(HttpServletRequest request,
			@RequestBody duration_change_request entity) {
		duration_change_response response = new duration_change_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			Date _enddate = commonService.Date_Add_with_holiday(entity.startdate, entity.duration, orgrootid_link);
			response.enddate = commonService.getEndOfDate(_enddate);
			response.productivity = commonService.getProductivity(entity.quantity, entity.duration);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<duration_change_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<duration_change_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/productivity_change", method = RequestMethod.POST)
	public ResponseEntity<productivity_change_response> ProductivityChange(HttpServletRequest request,
			@RequestBody productivity_change_request entity) {
		productivity_change_response response = new productivity_change_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			response.duration = commonService.getDuration_byProductivity(entity.quantity, entity.productivity);

			Date _enddate = commonService.Date_Add_with_holiday(entity.startdate, response.duration, orgrootid_link);
			response.enddate = commonService.getEndOfDate(_enddate);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<productivity_change_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<productivity_change_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/get_duration_and_productivity", method = RequestMethod.POST)
	public ResponseEntity<get_duration_and_productivity_response> GetDurationAndProductivity(HttpServletRequest request,
			@RequestBody get_duration_and_productivity_request entity) {
		get_duration_and_productivity_response response = new get_duration_and_productivity_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			int duration = commonService.getDuration(entity.startdate, entity.enddate, orgrootid_link);
			int productivity = commonService.getProductivity(entity.quantity, duration);

			response.duration = duration;
			response.productivity = productivity;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_duration_and_productivity_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_duration_and_productivity_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update_duration", method = RequestMethod.POST)
	public ResponseEntity<update_duration_porder_response> UpdatDuration(HttpServletRequest request,
			@RequestBody update_duration_porder_request entity) {
		update_duration_porder_response response = new update_duration_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			Date end = commonService.Date_Add_with_holiday(entity.data.getStartDate(), entity.data.getDuration() - 1,
					orgrootid_link);
			int productivity = commonService.getProductivity(entity.data.getTotalpackage(), entity.data.getDuration());

			POrder req = porderService.findOne(entity.data.getPorderid_link());

			int type = end.after(req.getShipdate()) ? 1 : 0;
			Schedule_porder sch = entity.data;
			sch.setEndDate(end);
			sch.setProductivity(productivity);
			sch.setGrant_type(type);

			response.data = sch;

			// Xoa grant khoi danh sach bi thay doi nang suat boi ngay nghi le
			POrderGrant grant = granttService.findOne(entity.data.getPorder_grantid_link());
			grant.setReason_change(null);
			granttService.save(grant);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/update_productivity", method = RequestMethod.POST)
	public ResponseEntity<update_duration_porder_response> UpdatProductivity(HttpServletRequest request,
			@RequestBody update_duration_porder_request entity) {
		update_duration_porder_response response = new update_duration_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			int duration = entity.data.getTotalpackage() / entity.data.getProductivity();
			duration = duration == 0 ? 1 : duration;
			Date end = commonService.Date_Add_with_holiday(entity.data.getStartDate(), duration, orgrootid_link);
			Date shipdate = null;

			if (entity.data.getStatus() == 2) {
				List<PContract_PO> list_po = porderSkuService.getListPO_ByGrant(entity.data.getPorder_grantid_link());
				if (list_po.size() > 0) {
					shipdate = list_po.get(0).getShipdate();
					for (PContract_PO po : list_po) {
						if (po.getShipdate().after(shipdate))
							shipdate = po.getShipdate();
					}
				}
			} else {
				POrder porder = porderService.findOne(entity.data.getPorderid_link());
				shipdate = porder.getShipdate();
			}

			int type = 0;
			if (shipdate != null) {
				type = end.after(shipdate) ? 1 : 0;
			}
			Schedule_porder sch = entity.data;
			sch.setEndDate(end);
			sch.setDuration(duration);
			sch.setGrant_type(type);

			response.data = sch;

			// Xoa grant khoi danh sach bi thay doi nang suat boi ngay nghi le
			POrderGrant grant = granttService.findOne(entity.data.getPorder_grantid_link());
			grant.setReason_change(null);
			granttService.save(grant);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<update_duration_porder_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/get_duration", method = RequestMethod.POST)
	public ResponseEntity<getduration_response> GetDuration(HttpServletRequest request,
			@RequestBody getduration_request entity) {
		getduration_response response = new getduration_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			Date StartDate = commonService.getBeginOfDate(entity.StartDate);
			Date EndDate = commonService.getEndOfDate(entity.EndDate);
			int duration = commonService.getDuration(StartDate, EndDate, orgrootid_link);

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

	@RequestMapping(value = "/get_duration_from_matdate", method = RequestMethod.POST)
	public ResponseEntity<get_duration_from_matdate_response> GetDurationMatDate(HttpServletRequest request,
			@RequestBody get_duration_from_matdate_request entity) {
		get_duration_from_matdate_response response = new get_duration_from_matdate_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			Date StartDate = commonService.Date_Add_with_holiday(entity.MatDate, 7, orgrootid_link);
			StartDate = commonService.getBeginOfDate(StartDate);
			Date EndDate = commonService.getEndOfDate(entity.EndDate);
			int duration = commonService.getDuration(StartDate, EndDate, orgrootid_link);

			response.duration = duration;
			response.production_date = StartDate;

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_duration_from_matdate_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_duration_from_matdate_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/merger_porder", method = RequestMethod.POST)
	public ResponseEntity<merger_porder_response> MergerPorder(HttpServletRequest request,
			@RequestBody merger_porder_request entity) {
		merger_porder_response response = new merger_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();

		try {
			POrderGrant grant_src = granttService.findOne(entity.pordergrantid_link_src);
			POrderGrant grant_des = granttService.findOne(entity.pordergrantid_link_des);

			int total = grant_des.getGrantamount() + grant_src.getGrantamount();

			Date start = grant_des.getStart_date_plan();
			start = commonService.getBeginOfDate(start);

			int productivity = grant_des.getProductivity();

			int duration = commonService.getDuration_byProductivity(total, productivity);

			Date end = commonService.Date_Add_with_holiday(start, duration - 1, orgrootid_link);
			end = commonService.getEndOfDate(end);

			int type = 0;
			List<PContract_PO> list_po = porderSkuService.getListPO_ByGrant(entity.pordergrantid_link_des);
			Date shipdate = list_po.get(0).getShipdate();

			String lineinfo = "";
			DateFormat df = new SimpleDateFormat("dd/MM/YYYY");

			for (PContract_PO po : list_po) {
				if (lineinfo == "") {
					lineinfo = po.getPo_buyer() + "-" + po.getPo_quantity() + "-" + df.format(po.getShipdate());
				} else {
					lineinfo = ";" + po.getPo_buyer() + "-" + po.getPo_quantity() + "-" + df.format(po.getShipdate());
				}

				if (po.getShipdate().after(shipdate))
					shipdate = po.getShipdate();
			}

			if (end.after(shipdate))
				type = 1;
			// Xoa grant nguon va processing nguon
			List<POrderProcessing> list_process = processService.getByOrderId_and_GrantId(grant_src.getPorderid_link(),
					entity.pordergrantid_link_src);
			for (POrderProcessing process : list_process) {
				processService.delete(process);
			}

			// Cap nhat grant dich
			grant_des.setGrantamount(total);
			grant_des.setStart_date_plan(start);
			grant_des.setFinish_date_plan(end);
			grant_des.setGranttoorgid_link(grant_des.getGranttoorgid_link());
			grant_des.setDuration(duration);
			grant_des.setTotalamount_tt(grant_des.getTotalamount_tt() + grant_src.getTotalamount_tt());
			grant_des.setReason_change(null);
			grant_des.setType(type);
			grant_des = granttService.save(grant_des);

			// log lai
			Log4jCommon.move_pordergrant(user.getUsername(), grant_des.getId(), grant_des.getGranttoorgid_link(),
					grant_des.getGranttoorgid_link(), "merger_porder/1551");

			Schedule_porder sch = entity.sch;
			sch.setStartDate(start);
			sch.setEndDate(end);
			sch.setDuration(duration);
			sch.setProductivity(productivity);
			sch.setName(grant_des.getMaHang());
			sch.setMahang(grant_des.getMaHang());
			sch.setTotalpackage(total);
			sch.setGrant_type(type);
			sch.setLineinfo(lineinfo);

			// chuyen sku cua grant_src sang grant_des
			List<POrderGrant_SKU> list_sku_src = grantskuService.getPOrderGrant_SKU(entity.pordergrantid_link_src);

			for (POrderGrant_SKU pOrderGrant_SKU : list_sku_src) {
				POrderGrant_SKU sku = grantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId_andPO(
						pOrderGrant_SKU.getSkuid_link(), entity.pordergrantid_link_des,
						pOrderGrant_SKU.getPcontract_poid_link());
				if (sku == null) {
					POrderGrant_SKU grantsku = new POrderGrant_SKU();
					grantsku.setGrantamount(pOrderGrant_SKU.getGrantamount());
					grantsku.setId(null);
					grantsku.setOrgrootid_link(orgrootid_link);
					grantsku.setPordergrantid_link(entity.pordergrantid_link_des);
					grantsku.setSkuid_link(pOrderGrant_SKU.getSkuid_link());
					grantsku.setPcontract_poid_link(pOrderGrant_SKU.getPcontract_poid_link());
					grantskuService.save(grantsku);
				} else {
					sku.setGrantamount(sku.getGrantamount() + pOrderGrant_SKU.getGrantamount());
					grantskuService.save(sku);
				}
			}

//			 .deleteById(entity.pordergrantid_link_src);
			granttService.deleteAll(grant_src);

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

	@RequestMapping(value = "/cancel_pordergrant", method = RequestMethod.POST)
	public ResponseEntity<cancel_pordergrant_response> CancelPorderGrant(HttpServletRequest request,
			@RequestBody cancel_pordergrant_request entity) {
		cancel_pordergrant_response response = new cancel_pordergrant_response();

		try {
			POrderGrant grant = granttService.findOne(entity.porder_grantid_link);
			long porderid_link = grant.getPorderid_link();
			POrder porder = porderService.findOne(porderid_link);
			if (porder.getTotal_process() > 0) {
				response.mes = "Lệnh sản xuất đã vào chuyền không thể hủy!";
			} else {
//				granttService.delete(grant);
				granttService.deleteAll(grant);
				processService.deleteByOrderID(porderid_link);

				List<POrderGrant> list_grant = granttService.getByOrderId(porderid_link);
				if (list_grant.size() == 0) {
					PContract_PO po = poService.findOne(porder.getPcontract_poid_link());
					if (po.getPo_typeid_link() == POType.PO_LINE_PLAN) {
						porder.setStatus(POrderStatus.PORDER_STATUS_FREE);
						porderService.save(porder);
					} else {
						porderService.delete(porder);
					}
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

	@RequestMapping(value = "/create_porder_and_grant", method = RequestMethod.POST)
	public ResponseEntity<CreatePorder_andGrant_response> CreatePorderAndGrant(HttpServletRequest request,
			@RequestBody CreatePorder_andGrant_request entity) {
		CreatePorder_andGrant_response response = new CreatePorder_andGrant_response();

		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			PContract_PO po = poService.findOne(entity.pcontract_poid_link);
			Long productid_link = entity.productid_link;
//			entity.orggrantid_link = entity.orggrantid_link == 0 ? null : entity.orggrantid_link;

			// tao porder
			String po_code = null != po.getPo_vendor() && po.getPo_vendor().length() > 0 ? po.getPo_vendor()
					: po.getPo_buyer();

			POrder porder = new POrder();
			porder.setFinishdate_plan(entity.enddate);
			porder.setGolivedate(po.getShipdate());
			porder.setGranttoorgid_link(entity.orgid_link);
			porder.setId(null);
			porder.setIsMap(true);
			porder.setOrderdate(new Date());
			porder.setOrgrootid_link(orgrootid_link);
			porder.setPcontract_poid_link(entity.pcontract_poid_link);
			porder.setPcontractid_link(po.getPcontractid_link());
			porder.setPlan_duration(entity.duration);
			porder.setPlan_linerequired(po.getPlan_linerequired());
			porder.setPlan_productivity(entity.productivity);
			porder.setProductid_link(productid_link);
			porder.setProductiondate(entity.startdate);
			porder.setStatus(entity.orggrantid_link != null ? POrderStatus.PORDER_STATUS_GRANTED
					: POrderStatus.PORDER_STATUS_FREE);
			porder.setTimecreated(new Date());
			porder.setTotalorder(entity.quantity);
			porder.setUsercreatedid_link(user.getId());

			porder = porderService.savePOrder(porder, po_code);
			Long porderid_link = porder.getId();
			// lay sku sang porder_sku
			Long pcontract_poid_link = po.getId();
			List<PContractProductSKU> list_po_sku = poskuService.getbypo_and_product(pcontract_poid_link,
					productid_link);
			for (PContractProductSKU po_sku : list_po_sku) {
				POrder_Product_SKU porder_sku = new POrder_Product_SKU();
				porder_sku.setId(null);
				porder_sku.setOrgrootid_link(orgrootid_link);
				porder_sku.setPcontract_poid_link(pcontract_poid_link);
				porder_sku.setPorderid_link(porderid_link);
				porder_sku.setPquantity_production(po_sku.getPquantity_production());
				porder_sku.setPquantity_sample(po_sku.getPquantity_sample());
				porder_sku.setPquantity_total(po_sku.getPquantity_total());
				porder_sku.setProductid_link(productid_link);
				porder_sku.setSkuid_link(po_sku.getSkuid_link());

				porderSkuService.save(porder_sku);
			}

			Long pordergrantid_link = null;
			if (entity.orggrantid_link != null) {
				// tao porder_grant
				POrderGrant grant = new POrderGrant();
				grant.setGranttoorgid_link(entity.orggrantid_link);
				grant.setId(null);
				grant.setOrdercode(porder.getOrdercode());
				grant.setOrgrootid_link(orgrootid_link);
				grant.setPorderid_link(porderid_link);
				grant.setTimecreated(new Date());
				grant.setUsercreatedid_link(user.getId());
				grant.setGrantdate(new Date());
				grant.setGrantamount(entity.quantity);
				grant.setStatus(2);
				grant.setOrgrootid_link(orgrootid_link);
				grant.setStart_date_plan(entity.startdate);
				grant.setFinish_date_plan(entity.enddate);
				grant.setProductivity(entity.productivity);
				grant.setDuration(entity.duration);
				grant.setType(0); // 0 la chua qua ngay giao hang
				grant.setTotalamount_tt(entity.quantity);
				grant = granttService.save(grant);
				pordergrantid_link = grant.getId();

				// log lai
				Log4jCommon.move_pordergrant(user.getUsername(), grant.getId(), entity.orggrantid_link,
						entity.orggrantid_link, "create_porder_and_grant");

				// them grant_sku
				for (PContractProductSKU po_sku : list_po_sku) {
					POrderGrant_SKU grant_sku = new POrderGrant_SKU();
					grant_sku.setId(null);
					grant_sku.setOrgrootid_link(orgrootid_link);
					grant_sku.setPcontract_poid_link(pcontract_poid_link);
					grant_sku.setGrantamount(po_sku.getPquantity_total());
					grant_sku.setSkuid_link(po_sku.getSkuid_link());
					grant_sku.setPordergrantid_link(pordergrantid_link);

					grantskuService.save(grant_sku);

					po_sku.setPquantity_granted(po_sku.getPquantity_total());
					po_sku.setPquantity_lenhsx(po_sku.getPquantity_total());
					poskuService.save(po_sku);
				}

				// tao Schedule_porder de tra len giao dien
				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				decimalFormat.setGroupingSize(3);
				Product product = productService.findOne(productid_link);
				String mahang = product.getBuyercode() + "-" + decimalFormat.format(grant.getGrantamount());

				Schedule_porder sch = new Schedule_porder();
				sch.setDuration(entity.duration);
				sch.setProductivity(entity.productivity);
				sch.setBuyername(porder.getBuyername());
				sch.setCls(porder.getCls());
				sch.setEndDate(entity.enddate);
				sch.setId_origin(porder.getId());
				sch.setMahang(mahang);
				sch.setName(mahang);
				sch.setParentid_origin(entity.orgid_link);
				sch.setPordercode(porder.getOrdercode());
				sch.setResourceId(entity.orggrantid_link);
				sch.setStartDate(entity.startdate);
				sch.setStatus(2);
				sch.setTotalpackage(porder.getTotalorder());
				sch.setVendorname(porder.getVendorname());
				sch.setPorder_grantid_link(pordergrantid_link);
				sch.setProductivity_po(porder.getProductivity_po());
				sch.setProductivity_porder(0);
				sch.setPcontract_poid_link(porder.getPcontract_poid_link());
				sch.setPcontractid_link(porder.getPcontractid_link());
				sch.setProductbuyercode(porder.getProductcode());
				sch.setPorderid_link(porder.getId());
				sch.setGrant_type(0);

				response.data = sch;
			}

			// danh dau po da map
			POrder_POLine porder_poline = new POrder_POLine();
			porder_poline.setId(null);
			porder_poline.setPcontract_poid_link(pcontract_poid_link);
			porder_poline.setPorderid_link(porderid_link);
			porder_poline.setPordergrantid_link(pordergrantid_link);
			porderlineService.save(porder_poline);

			// danh dau po da map
			List<PContractProductSKU> list_sku = poskuService.getbypo_and_product(pcontract_poid_link, productid_link);
			for (PContractProductSKU sku : list_sku) {
				sku.setIsmap(true);
				poskuService.save(sku);
			}
			List<PContractProductSKU> list_sku_notmap = poskuService.getsku_notmap(pcontract_poid_link);
			if (list_sku_notmap.size() == 0) {
				po.setIsmap(true);
				poService.save(po);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CreatePorder_andGrant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<CreatePorder_andGrant_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/create_many_porder_and_grant", method = RequestMethod.POST)
	public ResponseEntity<create_many_porder_grant_response> CreateManyPorderAndGrant(HttpServletRequest request,
			@RequestBody create_many_porder_grant_request entity) {
		create_many_porder_grant_response response = new create_many_porder_grant_response();

		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			Long productid_link = entity.list_pcontract_po.get(0).getProductid_link();

			Long sizesetid_link = entity.sizesetid_link == 0 ? null : entity.sizesetid_link;
			Long colorid_link = entity.colorid_link == 0 ? null : entity.colorid_link;
			int productivity = entity.productivity;
			Boolean isMerger = entity.isMerger;

			List<Schedule_porder> list_schedule = new ArrayList<Schedule_porder>();

			if (!isMerger) {
				for (int i = 0; i < entity.list_pcontract_po.size(); i++) {
					Long pcontract_poid_link = entity.list_pcontract_po.get(i).getPcontract_poid_link();
					PContract_PO po = poService.findOne(pcontract_poid_link);
					int total = entity.list_pcontract_po.get(i).getPo_quantity();

					// tao porder
					Date enddate = commonService.getEndOfDate(po.getShipdate());
					int duration = commonService.getDuration_byProductivity(total, productivity);

					Date startdate = commonService.Date_Add_with_holiday(enddate, (0 - duration), orgrootid_link);
					startdate = commonService.getBeginOfDate(startdate);

					String po_code = null != po.getPo_vendor() && po.getPo_vendor().length() > 0 ? po.getPo_vendor()
							: po.getPo_buyer();

					POrder porder = new POrder();
					porder.setFinishdate_plan(enddate);
					porder.setGolivedate(enddate);
					porder.setGranttoorgid_link(entity.orgid_link);
					porder.setId(null);
					porder.setIsMap(true);
					porder.setOrderdate(new Date());
					porder.setOrgrootid_link(orgrootid_link);
					porder.setPcontract_poid_link(pcontract_poid_link);
					porder.setPcontractid_link(po.getPcontractid_link());
					porder.setPlan_duration(duration);
					porder.setPlan_linerequired(po.getPlan_linerequired());
					porder.setPlan_productivity(entity.productivity);
					porder.setProductid_link(productid_link);
					porder.setProductiondate(startdate);
					porder.setStatus(entity.orggrantid_link != null ? POrderStatus.PORDER_STATUS_GRANTED
							: POrderStatus.PORDER_STATUS_FREE);
					porder.setTimecreated(new Date());
					porder.setTotalorder(total);
					porder.setUsercreatedid_link(user.getId());

					porder = porderService.savePOrder(porder, po_code);
					Long porderid_link = porder.getId();

					// lay sku sang porder_sku
					List<PContractProductSKU> list_po_sku = poskuService.getsku_notmap_by_product(pcontract_poid_link,
							productid_link);

					List<SizeSetAttributeValue> list_av = sizesetatt_repo.getall_bySizeSetId(sizesetid_link);
					List<Long> listsku = new ArrayList<Long>();
					if (sizesetid_link != null && colorid_link != null) {
						for (SizeSetAttributeValue sizeset_av : list_av) {
							List<Long> skuid_links = sku_av_repo.getskuid_by_valueMau_and_valueCo(colorid_link,
									sizeset_av.getAttributevalueid_link(), productid_link);
							if (skuid_links.size() > 0)
								listsku.add(skuid_links.get(0));
						}
						if (listsku.size() == 0)
							listsku.add((long) -1);
					} else {
						if (sizesetid_link == null && colorid_link != null) {
							listsku = sku_av_repo.get_bycolorid_link(productid_link, colorid_link);
							if (listsku.size() == 0)
								listsku.add((long) -1);
						} else {
							listsku = null;
						}
					}

					Long pordergrantid_link = null;
					if (entity.orggrantid_link != null) {
						// tao porder_grant
						POrderGrant grant = new POrderGrant();
						grant.setGranttoorgid_link(entity.orggrantid_link);
						grant.setId(null);
						grant.setOrdercode(porder.getOrdercode());
						grant.setOrgrootid_link(orgrootid_link);
						grant.setPorderid_link(porderid_link);
						grant.setTimecreated(new Date());
						grant.setUsercreatedid_link(user.getId());
						grant.setGrantdate(new Date());
						grant.setGrantamount(total);
						grant.setStatus(2);
						grant.setOrgrootid_link(orgrootid_link);
						grant.setStart_date_plan(startdate);
						grant.setFinish_date_plan(enddate);
						grant.setProductivity(productivity);
						grant.setDuration(duration);
						grant.setType(0); // 0 la chua qua ngay giao hang
						grant.setTotalamount_tt(total);
						grant = granttService.save(grant);
						pordergrantid_link = grant.getId();

						// log lai
						Log4jCommon.move_pordergrant(user.getUsername(), grant.getId(), entity.orggrantid_link,
								entity.orggrantid_link, "create_many_porder_and_grant");

						// danh dau po da map
						POrder_POLine porder_poline = new POrder_POLine();
						porder_poline.setId(null);
						porder_poline.setPcontract_poid_link(pcontract_poid_link);
						porder_poline.setPorderid_link(porderid_link);
						porder_poline.setPordergrantid_link(pordergrantid_link);
						porderlineService.save(porder_poline);

						// tao Schedule_porder de tra len giao dien
						DecimalFormat decimalFormat = new DecimalFormat("#,###");
						decimalFormat.setGroupingSize(3);
						Product product = productService.findOne(productid_link);
						String mahang = product.getBuyercode() + "-" + decimalFormat.format(total);
						PContract pcontract = pcontractService.findOne(po.getPcontractid_link());

						String lineinfo = po.getPo_buyer() + "-" + po.getPo_quantity()
								+ commonService.DateToString(po.getShipdate(), "dd/MM/YYYY");

						Schedule_porder sch = new Schedule_porder();
						sch.setDuration(duration);
						sch.setProductivity(entity.productivity);
						sch.setBuyername(porder.getBuyername());
						sch.setCls(pcontract.getcls() + " match");
						sch.setEndDate(enddate);
						sch.setId_origin(porder.getId());
						sch.setMahang(mahang);
						sch.setName(mahang);
						sch.setParentid_origin(entity.orgid_link);
						sch.setPordercode(porder.getOrdercode());
						sch.setResourceId(entity.orggrantid_link);
						sch.setStartDate(startdate);
						sch.setStatus(2);
						sch.setTotalpackage(porder.getTotalorder());
						sch.setVendorname(porder.getVendorname());
						sch.setPorder_grantid_link(pordergrantid_link);
						sch.setProductivity_po(porder.getProductivity_po());
						sch.setProductivity_porder(0);
						sch.setPcontract_poid_link(pcontract_poid_link);
						sch.setPcontractid_link(po.getPcontractid_link());
						sch.setProductbuyercode(product.getBuyercode());
						sch.setPorderid_link(porder.getId());
						sch.setGrant_type(0);
						sch.setProductid_link(productid_link);
						sch.setLineinfo(lineinfo);

						list_schedule.add(sch);
					}

					for (PContractProductSKU po_sku : list_po_sku) {
						if (listsku == null || listsku.contains(po_sku.getSkuid_link())) {
							POrder_Product_SKU porder_sku = new POrder_Product_SKU();
							porder_sku.setId(null);
							porder_sku.setOrgrootid_link(orgrootid_link);
							porder_sku.setPcontract_poid_link(pcontract_poid_link);
							porder_sku.setPorderid_link(porderid_link);
							porder_sku.setPquantity_production(po_sku.getPquantity_production());
							porder_sku.setPquantity_sample(po_sku.getPquantity_sample());
							porder_sku.setPquantity_total(po_sku.getPquantity_total());
							porder_sku.setProductid_link(productid_link);
							porder_sku.setSkuid_link(po_sku.getSkuid_link());

							porderSkuService.save(porder_sku);

							// them grant_sku
							POrderGrant_SKU grant_sku = new POrderGrant_SKU();
							grant_sku.setId(null);
							grant_sku.setOrgrootid_link(orgrootid_link);
							grant_sku.setPcontract_poid_link(pcontract_poid_link);
							grant_sku.setGrantamount(po_sku.getPquantity_total());
							grant_sku.setSkuid_link(po_sku.getSkuid_link());
							grant_sku.setPordergrantid_link(pordergrantid_link);

							grantskuService.save(grant_sku);

							// Cap nhat pcontracct_sku da map

							po_sku.setPquantity_granted(po_sku.getPquantity_total());
							po_sku.setPquantity_lenhsx(po_sku.getPquantity_total());
							po_sku.setIsmap(true);
							poskuService.save(po_sku);
						}

					}

					// danh dau po da map

					List<PContractProductSKU> list_sku_notmap = poskuService.getsku_notmap(pcontract_poid_link);
					if (list_sku_notmap.size() == 0) {
						po.setIsmap(true);
						poService.save(po);
					}
				}
			} else {
				// tao porder
				// lay po co ngay giao gan nhat
				PContract_PO po_min = poService.findOne(entity.list_pcontract_po.get(0).getPcontract_poid_link());
				Date datemin = entity.list_pcontract_po.get(0).getShipdate();
				int total = 0;
				for (PContractPO_Shipping shipping : entity.list_pcontract_po) {
					total += shipping.getPo_quantity();
					if (datemin.after(shipping.getShipdate())) {
						datemin = shipping.getShipdate();
						po_min = poService.findOne(shipping.getPcontract_poid_link());
					}
				}
				int duration_min = commonService.getDuration_byProductivity(po_min.getPo_quantity(), productivity);

				Date startdate = commonService.getBeginOfDate(
						commonService.Date_Add_with_holiday(po_min.getShipdate(), (0 - duration_min), orgrootid_link));
				int duration = commonService.getDuration_byProductivity(total, productivity);
				Date enddate = commonService
						.getEndOfDate(commonService.Date_Add_with_holiday(startdate, duration, orgrootid_link));
				String po_code = null != po_min.getPo_vendor() && po_min.getPo_vendor().length() > 0
						? po_min.getPo_vendor()
						: po_min.getPo_buyer();

				POrder porder = new POrder();
				porder.setFinishdate_plan(enddate);
				porder.setGolivedate(enddate);
				porder.setGranttoorgid_link(entity.orgid_link);
				porder.setId(null);
				porder.setIsMap(true);
				porder.setOrderdate(new Date());
				porder.setOrgrootid_link(orgrootid_link);
				porder.setPcontract_poid_link(null);
				porder.setPcontractid_link(po_min.getPcontractid_link());
				porder.setPlan_duration(duration);
				porder.setPlan_linerequired(po_min.getPlan_linerequired());
				porder.setPlan_productivity(entity.productivity);
				porder.setProductid_link(productid_link);
				porder.setProductiondate(startdate);
				porder.setStatus(entity.orggrantid_link != null ? POrderStatus.PORDER_STATUS_GRANTED
						: POrderStatus.PORDER_STATUS_FREE);
				porder.setTimecreated(new Date());
				porder.setTotalorder(total);
				porder.setUsercreatedid_link(user.getId());

				porder = porderService.savePOrder(porder, po_code);
				Long porderid_link = porder.getId();

				// tao grant
				Long pordergrantid_link = null;
				if (entity.orggrantid_link != null) {
					POrderGrant grant = new POrderGrant();
					grant.setGranttoorgid_link(entity.orggrantid_link);
					grant.setId(null);
					grant.setOrdercode(porder.getOrdercode());
					grant.setOrgrootid_link(orgrootid_link);
					grant.setPorderid_link(porderid_link);
					grant.setTimecreated(new Date());
					grant.setUsercreatedid_link(user.getId());
					grant.setGrantdate(new Date());
					grant.setGrantamount(total);
					grant.setStatus(2);
					grant.setOrgrootid_link(orgrootid_link);
					grant.setStart_date_plan(startdate);
					grant.setFinish_date_plan(enddate);
					grant.setProductivity(productivity);
					grant.setDuration(duration);
					grant.setType(0); // 0 la chua qua ngay giao hang
					grant.setTotalamount_tt(total);
					grant = granttService.save(grant);
					pordergrantid_link = grant.getId();

					// log lai
					Log4jCommon.move_pordergrant(user.getUsername(), grant.getId(), entity.orggrantid_link,
							entity.orggrantid_link, "create_many_porder_and_grant/merger");

				}

				String lineinfo = "";
				DateFormat df = new SimpleDateFormat("dd/MM/YYYY");
				// lay sku sang porder_sku
				for (PContractPO_Shipping shipping : entity.list_pcontract_po) {
					if (lineinfo == "") {
						lineinfo += shipping.getPo_buyer() + "-" + shipping.getPo_quantity() + "-"
								+ df.format(shipping.getShipdate());
					} else {
						lineinfo += "; " + shipping.getPo_buyer() + "-" + shipping.getPo_quantity() + "-"
								+ df.format(shipping.getShipdate());
					}
					Long pcontract_poid_link = shipping.getPcontract_poid_link();
					PContract_PO po = poService.findOne(pcontract_poid_link);
					List<PContractProductSKU> list_po_sku = poskuService.getsku_notmap_by_product(pcontract_poid_link,
							productid_link);

					List<SizeSetAttributeValue> list_av = sizesetatt_repo.getall_bySizeSetId(sizesetid_link);
					List<Long> listsku = new ArrayList<Long>();
					if (sizesetid_link != null && colorid_link != null) {
						for (SizeSetAttributeValue sizeset_av : list_av) {
							List<Long> skuid_links = sku_av_repo.getskuid_by_valueMau_and_valueCo(colorid_link,
									sizeset_av.getAttributevalueid_link(), productid_link);
							if (skuid_links.size() > 0)
								listsku.add(skuid_links.get(0));
						}
						if (listsku.size() == 0)
							listsku.add((long) -1);
					} else {
						if (sizesetid_link == null && colorid_link != null) {
							listsku = sku_av_repo.get_bycolorid_link(productid_link, colorid_link);
							if (listsku.size() == 0)
								listsku.add((long) -1);
						} else {
							listsku = null;
						}
					}

					for (PContractProductSKU po_sku : list_po_sku) {
						if (listsku == null || listsku.contains(po_sku.getSkuid_link())) {
							POrder_Product_SKU porder_sku = new POrder_Product_SKU();
							porder_sku.setId(null);
							porder_sku.setOrgrootid_link(orgrootid_link);
							porder_sku.setPcontract_poid_link(pcontract_poid_link);
							porder_sku.setPorderid_link(porderid_link);
							porder_sku.setPquantity_production(po_sku.getPquantity_production());
							porder_sku.setPquantity_sample(po_sku.getPquantity_sample());
							porder_sku.setPquantity_total(po_sku.getPquantity_total());
							porder_sku.setProductid_link(productid_link);
							porder_sku.setSkuid_link(po_sku.getSkuid_link());

							porderSkuService.save(porder_sku);

							// them grant_sku
							POrderGrant_SKU grant_sku = new POrderGrant_SKU();
							grant_sku.setId(null);
							grant_sku.setOrgrootid_link(orgrootid_link);
							grant_sku.setPcontract_poid_link(pcontract_poid_link);
							grant_sku.setGrantamount(po_sku.getPquantity_total());
							grant_sku.setSkuid_link(po_sku.getSkuid_link());
							grant_sku.setPordergrantid_link(pordergrantid_link);

							grantskuService.save(grant_sku);

							// Cap nhat pcontracct_sku da map

							po_sku.setPquantity_granted(po_sku.getPquantity_total());
							po_sku.setPquantity_lenhsx(po_sku.getPquantity_total());
							po_sku.setIsmap(true);
							poskuService.save(po_sku);
						}
					}
					// danh dau po da map

					List<PContractProductSKU> list_sku_notmap = poskuService.getsku_notmap(pcontract_poid_link);
					if (list_sku_notmap.size() == 0) {
						po.setIsmap(true);
						poService.save(po);
					}
				}

				// Cap nhat lai thong tin grant
				POrderGrant grant = granttService.findOne(pordergrantid_link);
				grant.setLineinfo(lineinfo);
				granttService.save(grant);

				// tao Schedule_porder de tra len giao dien
				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				decimalFormat.setGroupingSize(3);
				Product product = productService.findOne(productid_link);
				String mahang = product.getBuyercode() + "-" + decimalFormat.format(total);
				PContract pcontract = pcontractService.findOne(po_min.getPcontractid_link());

				Schedule_porder sch = new Schedule_porder();
				sch.setDuration(duration);
				sch.setProductivity(entity.productivity);
				sch.setBuyername(porder.getBuyername());
				sch.setCls(pcontract.getcls() + " match");
				sch.setEndDate(enddate);
				sch.setId_origin(porder.getId());
				sch.setMahang(mahang);
				sch.setName(mahang);
				sch.setParentid_origin(entity.orgid_link);
				sch.setPordercode(porder.getOrdercode());
				sch.setResourceId(entity.orggrantid_link);
				sch.setStartDate(startdate);
				sch.setStatus(2);
				sch.setTotalpackage(porder.getTotalorder());
				sch.setVendorname(porder.getVendorname());
				sch.setPorder_grantid_link(pordergrantid_link);
				sch.setProductivity_po(porder.getProductivity_po());
				sch.setProductivity_porder(0);
				sch.setPcontract_poid_link(0);
				sch.setPcontractid_link(po_min.getPcontractid_link());
				sch.setProductbuyercode(product.getBuyercode());
				sch.setPorderid_link(porder.getId());
				sch.setGrant_type(0);
				sch.setProductid_link(productid_link);
				sch.setLineinfo(lineinfo);

				list_schedule.add(sch);
			}

			response.data = list_schedule;

			// lay nhung grant ke hoach de remove tren bieu do
			response.remove = granttService.getGrantIdPlanByProduct(productid_link);
			// cap nhat line ke hoach sang trang thai da map
			for (Long grantid : response.remove) {
				POrderGrant grant = granttService.findOne(grantid);
				grant.setIsmap(true);
				granttService.save(grant);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<create_many_porder_grant_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<create_many_porder_grant_response>(response, HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/break_porder", method = RequestMethod.POST)
	public ResponseEntity<break_porder_response> BreakPorder(HttpServletRequest request,
			@RequestBody break_porder_request entity) {
		break_porder_response response = new break_porder_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		int producttivity = entity.producttivity;
		response.mes = "";
		try {
			// Kiem tra so luong tung sku xem co bi vuot qua khong
			List<POrderGrant_SKU> list_sku = entity.data;
			for (POrderGrant_SKU pOrderGrant_SKU : list_sku) {
				Long skuid_link = pOrderGrant_SKU.getSkuid_link();
				Long pordergrantid_link = pOrderGrant_SKU.getPordergrantid_link();
				Long pcontract_poid_link = pOrderGrant_SKU.getPcontract_poid_link();

				POrderGrant_SKU sku = grantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId_andPO(skuid_link,
						pordergrantid_link, pcontract_poid_link);
				if (sku != null) {
					if (pOrderGrant_SKU.getGrantamount() > sku.getGrantamount()) {
						response.mes = "Bạn không được tách vượt quá số lượng đang được giao cho tổ!";
						response.sku = grantskuService.getPOrderGrant_SKU(pOrderGrant_SKU.getPordergrantid_link());
						break;
					}
				}

			}

			if (response.mes == "") {
				// Cập nhật lại grant cũ sau khi tách
				POrder porder = porderService.findOne(entity.porderid_link);
				POrderGrant grant_old = granttService.findOne(entity.pordergrant_id_link);

				// Lay ngay giao hang
				List<PContract_PO> list_po = porderSkuService.getListPO_ByGrant(entity.pordergrant_id_link);
				Date shipdate = list_po.get(0).getShipdate();
				for (PContract_PO po : list_po) {
					if (po.getShipdate().after(shipdate))
						shipdate = po.getShipdate();
				}

				int total = grant_old.getGrantamount();
				int totalorder_old = grant_old.getGrantamount() - entity.quantity;
				Date start_old = grant_old.getStart_date_plan();
				start_old = commonService.getBeginOfDate(start_old);
				int duration_old = commonService.getDuration_byProductivity(totalorder_old, producttivity);

				Date end_old = commonService.Date_Add_with_holiday(start_old, duration_old - 1, orgrootid_link);
				end_old = commonService.getEndOfDate(end_old);
//				Date end_new = grant_old.getFinish_date_plan();
//				int productivity_old = commonService.getProductivity(totalorder_old, duration_old);

				int type_old = 0, type_new = 0;
				if (end_old.after(shipdate))
					type_old = 1;

				grant_old.setGrantamount(totalorder_old);
				grant_old.setFinish_date_plan(end_old);
				grant_old.setDuration(duration_old);
				grant_old.setTotalamount_tt(grant_old.getTotalamount_tt() - entity.quantity);
				grant_old.setReason_change(null);
				grant_old.setType(type_old);
				grant_old = granttService.save(grant_old);

				// Cap nhat lai Processing cu sau khi tach
				List<POrderProcessing> lsProcessing = processService
						.getByOrderId_and_GrantId(grant_old.getPorderid_link(), grant_old.getId());
				for (POrderProcessing process : lsProcessing) {
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
				old.setTotalpackage(totalorder_old);
				old.setGrant_type(type_old);
				old.setCls(grant_old.getCls());
				response.old_data = old;

				// Sinh grant moi
				Date start_new = commonService.Date_Add_with_holiday(end_old, 1, orgrootid_link);
				start_new = commonService.getBeginOfDate(start_new);

				int total_new = total - totalorder_old;
				int duration_new = commonService.getDuration_byProductivity(total_new, producttivity);
				Date end_new = commonService.Date_Add_with_holiday(start_new, duration_new - 1, orgrootid_link);
				end_new = commonService.getEndOfDate(end_new);

				if (end_new.after(shipdate))
					type_new = 1;

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
				grant.setStatus(grant_old.getStatus());
				grant.setOrgrootid_link(orgrootid_link);
				grant.setStart_date_plan(start_new);
				grant.setFinish_date_plan(end_new);
				grant.setProductivity(producttivity);
				grant.setDuration(duration_new);
				grant.setType(type_new);
				grant.setTotalamount_tt(entity.quantity);
				grant = granttService.save(grant);

				// log lai
				Log4jCommon.move_pordergrant(user.getUsername(), grant.getId(), grant_old.getGranttoorgid_link(),
						grant_old.getGranttoorgid_link(), "break_porder");

				// Sinh 1 dong moi trong Processing
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
				new_data.setStatus(grant_old.getStatus());
				new_data.setPorder_grantid_link(grant.getId());
				new_data.setPorderid_link(grant.getPorderid_link());
				new_data.setPcontract_poid_link(grant_old.getPcontract_poid_link());
				new_data.setProductid_link(grant_old.getProductid_link());
				new_data.setPcontractid_link(grant_old.getPcontractid_link());
				new_data.setProductivity_po(grant_old.getProductivity_po());
				new_data.setProductivity_porder(grant_old.getProductivity_porder());
				new_data.setProductbuyercode(porder.getProductcode());
				new_data.setGrant_type(type_new);
				response.new_data = new_data;

				// gan sku vao grant moi sinh ra va tru sku o grant tach

				for (POrderGrant_SKU pOrderGrant_SKU : list_sku) {
					POrderGrant_SKU sku = new POrderGrant_SKU();
					sku.setGrantamount(pOrderGrant_SKU.getAmount_break());
					sku.setId(null);
					sku.setOrgrootid_link(orgrootid_link);
					sku.setPordergrantid_link(grant.getId());
					sku.setSkuid_link(pOrderGrant_SKU.getSkuid_link());
					sku.setPcontract_poid_link(pOrderGrant_SKU.getPcontract_poid_link());
					grantskuService.save(sku);

					POrderGrant_SKU sku_old = grantskuService.getPOrderGrant_SKUbySKUid_linkAndGrantId_andPO(
							pOrderGrant_SKU.getSkuid_link(), pOrderGrant_SKU.getPordergrantid_link(),
							pOrderGrant_SKU.getPcontract_poid_link());

					sku_old.setGrantamount(sku_old.getGrantamount() - pOrderGrant_SKU.getAmount_break());
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
