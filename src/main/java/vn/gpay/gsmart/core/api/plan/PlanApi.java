package vn.gpay.gsmart.core.api.plan;

import java.util.ArrayList;
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
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_plan.IPorderPlanService;
import vn.gpay.gsmart.core.porder_plan.PorderPlanBinding;
import vn.gpay.gsmart.core.porder_plan.Porder_Plan;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/plan")
public class PlanApi {
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Service porderService;
	@Autowired IPorderPlanService planService;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<Plan_getall_response> GetByProduct(HttpServletRequest request,
			@RequestBody Plan_getall_request entity) {
		Plan_getall_response response = new Plan_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();			

			List<PorderPlanBinding> list = new ArrayList<PorderPlanBinding>();
			long id =1; //id tự sinh để build tree
			//get nha may 
			List<Org> list_org = orgService.findAllorgByTypeId(AtributeFixValues.orgtype_factory, orgrootid_link);
			
			
			
			for(Org org : list_org) {
				Date startdate_org = null, enddate_org = null;
				//sinh nha may va cac lenh cua nha may
				PorderPlanBinding ppo = new PorderPlanBinding();
				ppo.setExpanded(false);
				ppo.setId(id);
				ppo.setId_origin(org.getId());
				ppo.setLeaf(false);
				ppo.Name = (org.getName());
				ppo.setParentId(null);
				ppo.Rollup = true;
				ppo.setIconCls("x-fa fa-home");
								
				id++; //sau khi sinh nha may thi tang id len
				
				//Lay cac lenh cua nha may 
				long orgid_link = org.getId();
				List<POrder> list_porder = porderService.get_by_org(orgid_link);
				
				
				
				for(POrder porder : list_porder) {
					Date startdate_porder = null, enddate_porder = null;
					
					PorderPlanBinding pp_porder = new PorderPlanBinding();
					
					pp_porder.setExpanded(false);
					pp_porder.setId(id);
					pp_porder.setId_origin(porder.getId());
					pp_porder.setLeaf(false);
					pp_porder.Name = (porder.getOrdercode());
					pp_porder.setOrderdate(porder.getOrderdate());
					pp_porder.setParentId(ppo.getId());
					pp_porder.setTotalorder(porder.getTotalorder());
					pp_porder.setStatus(porder.getStatus());
					pp_porder.Rollup = true;
					pp_porder.setPcontractid_link(porder.getPcontractid_link());
					pp_porder.setProductid_link(porder.getProductid_link());
					pp_porder.setPorderid_link(porder.getId());
					pp_porder.setIconCls("x-fa fa-industry");
					pp_porder.setPcontract_number(porder.getContractcode());
					pp_porder.setTotalpackage((float)porder.getTotalorder());
					
					id++; //sau khi sinh nha may thi tang id len
					
					//Them 4 plan co ban: NPL ve, May mau, Nhap kho, Giao hang
					PorderPlanBinding pp_material = new PorderPlanBinding();
					pp_material.setId(id);
					pp_material.Name = ("Nguyên phụ liệu về");
					pp_material.setPorderid_link(porder.getId());
					pp_material.setPlan_type(1);
					pp_material.Rollup = true;
					pp_material.setIconCls("x-fa fa-ambulance");
					
					id++; //sau khi sinh nha may thi tang id len
					
					//Lay danh sach plan tu bang porder_plan voi plan_type=1
					Date startdate_npl = null, enddate_npl = null;
					
					List<Porder_Plan> list_plan = planService.get_by_plantype(porder.getId(), 1);
					for(int i =0; i<list_plan.size(); i++) {

						Porder_Plan plan = list_plan.get(i);
						//kiem tra de lay ngay nho nhat va lon nhat trong cac ke hoach
						
						if(startdate_npl == null) {
							startdate_npl = plan.getPlan_date_start();
						}
						else {
							startdate_npl = startdate_npl.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_npl;
						}
						
						if(enddate_npl == null) {
							enddate_npl = plan.getPlan_date_end();
						}
						else {
							enddate_npl = enddate_npl.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_npl;
						}
						
						//Lay ngay bat dau va ket thuc cho lenh
						
						if(startdate_porder == null) {
							startdate_porder = plan.getPlan_date_start();
						}
						else {
							startdate_porder = startdate_porder.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_porder;
						}
						
						if(enddate_porder == null) {
							enddate_porder = plan.getPlan_date_start();
						}
						else {
							enddate_porder = enddate_porder.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_porder;
						}
						
						//Lay ngay bat dau va ket thuc cho nha may
						
						if(startdate_org == null) {
							startdate_org = plan.getPlan_date_start();
						}
						else {
							startdate_org = startdate_org.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_org;
						}
						
						if(enddate_org == null) {
							enddate_org = plan.getPlan_date_start();
						}
						else {
							enddate_org = enddate_org.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_org;
						}
						
						PorderPlanBinding pp_plan = new PorderPlanBinding();
						pp_plan.setId(id);
						pp_plan.EndDate = (plan.getPlan_date_end());
						pp_plan.StartDate = (plan.getPlan_date_start());
						pp_plan.setExpanded(false);
						pp_plan.setId_origin(plan.getId());
						pp_plan.setLeaf(true);
						pp_plan.Name = plan.getComment();
						pp_plan.setId_origin(plan.getId());
						pp_plan.setParentId(pp_material.getId());
						pp_plan.setPlan_type(1);
						pp_plan.setPorderid_link(porder.getId());
						pp_plan.setTotalpackage(plan.getPlan_amount());
						pp_plan.Rollup = true;
						
						id++;
						
						pp_material.getChildren().add(pp_plan);
					}
					pp_material.StartDate = startdate_npl;
					pp_material.EndDate = enddate_npl;
					pp_porder.getChildren().add(pp_material);
					
					
					PorderPlanBinding pp_sample = new PorderPlanBinding();
					pp_sample.setId(id);
					pp_sample.Name = ("May mẫu");
					pp_sample.setPorderid_link(porder.getId());
					pp_sample.setPlan_type(2);
					pp_sample.Rollup = true;
					pp_sample.setIconCls("x-fa fa-hand-point-right");
					
					id++;
					Date startdate_maymau = null, enddate_maymau = null;
					//Lay danh sach plan tu bang porder_plan voi plan_type=2
					List<Porder_Plan> list_plan_2 = planService.get_by_plantype(porder.getId(), 2);
					for(int i =0; i<list_plan_2.size(); i++) {
						Porder_Plan plan = list_plan_2.get(i);
						
						if(startdate_maymau == null) {
							startdate_maymau = plan.getPlan_date_start();
						}
						else {
							startdate_maymau = startdate_maymau.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_maymau;
						}
						
						if(enddate_maymau == null) {
							enddate_maymau = plan.getPlan_date_end();
						}
						else {
							enddate_maymau = enddate_maymau.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_maymau;
						}
						
						//Lay ngay bat dau va ket thuc cho lenh
						
						if(startdate_porder == null) {
							startdate_porder = plan.getPlan_date_start();
						}
						else {
							startdate_porder = startdate_porder.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_porder;
						}
						
						if(enddate_porder == null) {
							enddate_porder = plan.getPlan_date_start();
						}
						else {
							enddate_porder = enddate_porder.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_porder;
						}
						
						//Lay ngay bat dau va ket thuc cho nha may
						
						if(startdate_org == null) {
							startdate_org = plan.getPlan_date_start();
						}
						else {
							startdate_org = startdate_org.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_org;
						}
						
						if(enddate_org == null) {
							enddate_org = plan.getPlan_date_start();
						}
						else {
							enddate_org = enddate_org.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_org;
						}
						
						PorderPlanBinding pp_plan = new PorderPlanBinding();
						pp_plan.setId(id);
						pp_plan.EndDate = (plan.getPlan_date_end());
						pp_plan.StartDate = (plan.getPlan_date_start());
						pp_plan.setExpanded(false);
						pp_plan.setId_origin(plan.getId());
						pp_plan.setLeaf(true);
						pp_plan.Name = plan.getComment();
						pp_plan.setId_origin(plan.getId());
						pp_plan.setParentId(pp_sample.getId());
						pp_plan.setPlan_type(1);
						pp_plan.setPorderid_link(porder.getId());
						pp_plan.setTotalpackage(plan.getPlan_amount());
						pp_plan.Rollup = true;
						
						id++;
						
						pp_sample.getChildren().add(pp_plan);
					}
					pp_sample.StartDate = startdate_maymau;
					pp_sample.EndDate = enddate_maymau;
					pp_porder.getChildren().add(pp_sample);
					
					PorderPlanBinding pp_vaochuyen = new PorderPlanBinding();
					pp_vaochuyen.setId(id);
					pp_vaochuyen.Name = ("Vào chuyền");
					pp_vaochuyen.setPorderid_link(porder.getId());
					pp_vaochuyen.setPlan_type(5);
					pp_vaochuyen.Rollup = true;
					pp_vaochuyen.setIconCls("x-fa fa-bullhorn");
					
					id++;
					
					Date startdate_vc = null, enddate_vc = null;
					//Lay danh sach plan tu bang porder_plan voi plan_type=2
					List<Porder_Plan> list_plan_5 = planService.get_by_plantype(porder.getId(), 5);
					for(int i =0; i<list_plan_5.size(); i++) {
						Porder_Plan plan = list_plan_5.get(i);
						
						if(startdate_vc == null) {
							startdate_vc = plan.getPlan_date_start();
						}
						else {
							startdate_vc = startdate_vc.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_vc;
						}
						
						if(enddate_vc == null) {
							enddate_vc = plan.getPlan_date_end();
						}
						else {
							enddate_vc = enddate_vc.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_vc;
						}
						
						//Lay ngay bat dau va ket thuc cho lenh
						
						if(startdate_porder == null) {
							startdate_porder = plan.getPlan_date_start();
						}
						else {
							startdate_porder = startdate_porder.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_porder;
						}
						
						if(enddate_porder == null) {
							enddate_porder = plan.getPlan_date_start();
						}
						else {
							enddate_porder = enddate_porder.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_porder;
						}
						
						//Lay ngay bat dau va ket thuc cho nha may
						
						if(startdate_org == null) {
							startdate_org = plan.getPlan_date_start();
						}
						else {
							startdate_org = startdate_org.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_org;
						}
						
						if(enddate_org == null) {
							enddate_org = plan.getPlan_date_start();
						}
						else {
							enddate_org = enddate_org.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_org;
						}
						
						PorderPlanBinding pp_plan = new PorderPlanBinding();
						pp_plan.setId(id);
						pp_plan.EndDate = (plan.getPlan_date_end());
						pp_plan.StartDate = (plan.getPlan_date_start());
						pp_plan.setExpanded(false);
						pp_plan.setId_origin(plan.getId());
						pp_plan.setLeaf(true);
						pp_plan.Name = plan.getComment();
						pp_plan.setId_origin(plan.getId());
						pp_plan.setParentId(pp_sample.getId());
						pp_plan.setPlan_type(1);
						pp_plan.setPorderid_link(porder.getId());
						pp_plan.setTotalpackage(plan.getPlan_amount());
						pp_plan.Rollup = true;
						
						id++;
						
						pp_vaochuyen.getChildren().add(pp_plan);
					}
					pp_vaochuyen.StartDate = startdate_vc;
					pp_vaochuyen.EndDate = enddate_vc;
					pp_porder.getChildren().add(pp_vaochuyen);
					
					
					
					PorderPlanBinding pp_stockin = new PorderPlanBinding();
					pp_stockin.setId(id);
					pp_stockin.Name = ("Nhập kho");
					pp_stockin.setPorderid_link(porder.getId());
					pp_stockin.setPlan_type(3);
					pp_stockin.Rollup = true;
					pp_stockin.setIconCls("x-fa fa-home");
					
					id++;
					//Lay danh sach plan tu bang porder_plan voi plan_type=3
					Date startdate_nk = null, enddate_nk = null;
					
					List<Porder_Plan> list_plan_3 = planService.get_by_plantype(porder.getId(), 3);
					for(int i =0; i<list_plan_3.size(); i++) {
						Porder_Plan plan = list_plan_3.get(i);
						
						if(startdate_nk == null) {
							startdate_nk = plan.getPlan_date_start();
						}
						else {
							startdate_nk = startdate_nk.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_nk;
						}
						
						if(enddate_nk == null) {
							enddate_nk = plan.getPlan_date_end();
						}
						else {
							enddate_nk = enddate_vc.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_nk;
						}
						
						//Lay ngay bat dau va ket thuc cho lenh
						
						if(startdate_porder == null) {
							startdate_porder = plan.getPlan_date_start();
						}
						else {
							startdate_porder = startdate_porder.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_porder;
						}
						
						if(enddate_porder == null) {
							enddate_porder = plan.getPlan_date_start();
						}
						else {
							enddate_porder = enddate_porder.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_porder;
						}
						
						//Lay ngay bat dau va ket thuc cho nha may
						
						if(startdate_org == null) {
							startdate_org = plan.getPlan_date_start();
						}
						else {
							startdate_org = startdate_org.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_org;
						}
						
						if(enddate_org == null) {
							enddate_org = plan.getPlan_date_start();
						}
						else {
							enddate_org = enddate_org.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_org;
						}
						
						PorderPlanBinding pp_plan = new PorderPlanBinding();
						pp_plan.setId(id);
						pp_plan.EndDate = (plan.getPlan_date_end());
						pp_plan.StartDate = (plan.getPlan_date_start());
						pp_plan.setExpanded(false);
						pp_plan.setId_origin(plan.getId());
						pp_plan.setLeaf(true);
						pp_plan.Name = plan.getComment();
						pp_plan.setId_origin(plan.getId());
						pp_plan.setParentId(pp_stockin.getId());
						pp_plan.setPlan_type(1);
						pp_plan.setPorderid_link(porder.getId());
						pp_plan.setTotalpackage(plan.getPlan_amount());
						pp_plan.Rollup = true;
						
						id++;
						
						pp_stockin.getChildren().add(pp_plan);
					}
					pp_stockin.StartDate = startdate_nk;
					pp_stockin.EndDate = enddate_nk;
					pp_porder.getChildren().add(pp_stockin);
					
					PorderPlanBinding pp_delivery = new PorderPlanBinding();
					pp_delivery.setId(id);
					pp_delivery.Name = ("Giao hàng");
					pp_delivery.setPorderid_link(porder.getId());
					pp_delivery.setPlan_type(4);
					pp_delivery.Rollup = true;
					pp_delivery.setIconCls("x-fa fa-location-arrow");
					
					id++;
					
					Date startdate_gh = null, enddate_gh = null;
					
					//Lay danh sach plan tu bang porder_plan voi plan_type=4
					List<Porder_Plan> list_plan_4 = planService.get_by_plantype(porder.getId(), 4);
					for(int i =0; i<list_plan_4.size(); i++) {
						Porder_Plan plan = list_plan_4.get(i);
						
						if(startdate_gh == null) {
							startdate_gh = plan.getPlan_date_start();
						}
						else {
							startdate_gh = startdate_nk.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_gh;
						}
						
						if(enddate_gh == null) {
							enddate_gh = plan.getPlan_date_end();
						}
						else {
							enddate_gh = enddate_vc.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_gh;
						}
						
						//Lay ngay bat dau va ket thuc cho lenh
						
						if(startdate_porder == null) {
							startdate_porder = plan.getPlan_date_start();
						}
						else {
							startdate_porder = startdate_porder.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_porder;
						}
						
						if(enddate_porder == null) {
							enddate_porder = plan.getPlan_date_start();
						}
						else {
							enddate_porder = enddate_porder.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_porder;
						}
						
						//Lay ngay bat dau va ket thuc cho nha may
						
						if(startdate_org == null) {
							startdate_org = plan.getPlan_date_start();
						}
						else {
							startdate_org = startdate_org.compareTo(plan.getPlan_date_start()) > 0 ? plan.getPlan_date_start() : startdate_org;
						}
						
						if(enddate_org == null) {
							enddate_org = plan.getPlan_date_start();
						}
						else {
							enddate_org = enddate_org.compareTo(plan.getPlan_date_end()) < 0 ? plan.getPlan_date_end() : enddate_org;
						}
						
						PorderPlanBinding pp_plan = new PorderPlanBinding();
						pp_plan.setId(id);
						pp_plan.EndDate = (plan.getPlan_date_end());
						pp_plan.StartDate = (plan.getPlan_date_start());
						pp_plan.setExpanded(false);
						pp_plan.setId_origin(plan.getId());
						pp_plan.setLeaf(true);
						pp_plan.Name = plan.getComment();
						pp_plan.setId_origin(plan.getId());
						pp_plan.setParentId(pp_delivery.getId());
						pp_plan.setPlan_type(1);
						pp_plan.setPorderid_link(porder.getId());
						pp_plan.setTotalpackage(plan.getPlan_amount());
						pp_plan.Rollup = true;
						
						id++;
						
						pp_delivery.getChildren().add(pp_plan);
					}
					pp_delivery.StartDate = startdate_gh;
					pp_delivery.EndDate = enddate_gh;
					pp_porder.getChildren().add(pp_delivery);
					
					pp_porder.StartDate = startdate_porder;
					pp_porder.EndDate = enddate_porder;
					ppo.getChildren().add(pp_porder);
				}
				
				ppo.StartDate = startdate_org;
				ppo.EndDate = enddate_org;
				list.add(ppo);
			}
			
			
			
			response.children = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Plan_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Plan_getall_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<plan_create_response> Create(HttpServletRequest request,
			@RequestBody plan_create_request entity) {
		plan_create_response response = new plan_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Porder_Plan data = entity.data;
			if(data.getPlan_date_start() == null) {
				data.setPlan_date_start(data.getPlan_date_end());
			}
			
			if(data.getPlan_date_end() == null) {
				data.setPlan_date_end(data.getPlan_date_start());
			}
			
			
			if(data.getId() == 0 || data.getId() == null) {
				data.setUsercreatedid_link(user.getId());
				data.setTimecreate(new Date());
				
				data = planService.save(data);
			}
			
			
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<plan_create_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<plan_create_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<plan_create_response> Update(HttpServletRequest request,
			@RequestBody plan_create_request entity) {
		plan_create_response response = new plan_create_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Porder_Plan data = entity.data;
			
			Porder_Plan edit = planService.findOne(data.getId());
			edit.setPlan_date_start(data.getPlan_date_start());
			edit.setPlan_date_end(data.getPlan_date_end());
			data = planService.save(edit);
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<plan_create_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<plan_create_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getinfo", method = RequestMethod.POST)
	public ResponseEntity<plan_getinfo_response> GetInfo(HttpServletRequest request,
			@RequestBody plan_getinfo_request entity) {
		plan_getinfo_response response = new plan_getinfo_response();
		try {
			response.data = planService.findOne(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<plan_getinfo_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<plan_getinfo_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(HttpServletRequest request,
			@RequestBody plan_getinfo_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			planService.deleteById(entity.id);
			
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
