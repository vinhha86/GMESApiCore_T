package vn.gpay.gsmart.core.api.porder_req;

import java.util.ArrayList;
import java.util.Arrays;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.productpairing.IProductPairingService;
import vn.gpay.gsmart.core.productpairing.ProductPairing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_flow.ITask_Flow_Service;
import vn.gpay.gsmart.core.task_flow.Task_Flow;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.task_object.Task_Object;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.TaskObjectType_Name;

@RestController
@RequestMapping("/api/v1/porder_req")
public class POrder_ReqAPI {
	@Autowired private IPOrder_Req_Service porder_req_Service;
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPOrder_Service porderService;
	@Autowired IOrgService orgService;
	@Autowired IPOrder_Req_Service reqService;
	@Autowired IPContractProductSKUService pcontract_ProductSKUService;
	@Autowired IPOrder_Product_SKU_Service porder_ProductSKUService;
	@Autowired ITask_Object_Service taskobjectService;
	@Autowired ITask_CheckList_Service checklistService;
	@Autowired ITask_Service taskService;
	@Autowired ITask_Flow_Service commentService;
	@Autowired Common commonService;
	@Autowired IProductPairingService pairService;
	
    ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_GetOne_Response> GetOne(@RequestBody POrder_Req_GetOne_Request entity,HttpServletRequest request ) {
		POrder_Req_GetOne_Response response = new POrder_Req_GetOne_Response();
		try {
			
			response.data = porder_req_Service.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_GetOne_Response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrder_Req_GetOne_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}    
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_Create_Response> Create(HttpServletRequest request,
			@RequestBody POrder_Req_Create_Request entity) {
		POrder_Req_Create_Response response = new POrder_Req_Create_Response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrder_Req porder_req = entity.data;
			
			//Lay thong tin PO
//			PContract_PO thePO = pcontract_POService.findOne(porder.getPcontract_poid_link());
//			String po_code = thePO.getPo_vendor().length() > 0?thePO.getPo_vendor():thePO.getPo_buyer();
			
			if (porder_req.getId() == null || porder_req.getId() == 0) {
				porder_req.setOrgrootid_link(orgrootid_link);
				porder_req.setOrderdate(new Date());
				porder_req.setUsercreatedid_link(user.getId());
				porder_req.setStatus(POrderReqStatus.STATUS_FREE);
				porder_req.setTimecreated(new Date());
			} 
			
			response.id = porder_req_Service.savePOrder_Req(porder_req);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_Create_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_Req_Create_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	
	@RequestMapping(value = "/get_bypo", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_GetByPO_Response> GetByPO(HttpServletRequest request,
			@RequestBody POrder_Req_GetByPO_Request entity) {
		POrder_Req_GetByPO_Response response = new POrder_Req_GetByPO_Response();
		try {
			response.data = porder_req_Service.getByPO(entity.pcontract_poid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_GetByPO_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_Req_GetByPO_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getby_org", method = RequestMethod.POST)
	public ResponseEntity<POrder_Req_getbyorg_response> GetByOrg(HttpServletRequest request){
		POrder_Req_getbyorg_response response = new POrder_Req_getbyorg_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgid_link = user.getOrgid_link();
//			long orgid_link = 2;
			List<String> orgTypes = new ArrayList<String>();
			orgTypes.add("13");
			orgTypes.add("14");
			List<Org> lsOrgChild = orgService.getorgChildrenbyOrg(orgid_link,orgTypes);
			
			if(orgid_link == 1) {
				for(Org theOrg:lsOrgChild){
					List<POrder_Req> a = reqService.get_by_org(theOrg.getId());
					
					List<POrder_Req> result = new ArrayList<POrder_Req>();
					for(POrder_Req pr : a) {
						long porderreqid_link = pr.getId();
						long pcontract_poid_link = pr.getPcontract_poid_link();
						List<POrder> p = porderService.getByPOrder_Req(pcontract_poid_link, porderreqid_link);
						
						if(p.size() == 0)
							result.add(pr);
					}
					
	//				if(a.size()>0)
	//					response.data.addAll(a);
					if(result.size()>0)
						response.data.addAll(result);
				}
			}else {
				List<POrder_Req> a = reqService.get_by_org(orgid_link);
				
				List<POrder_Req> result = new ArrayList<POrder_Req>();
				for(POrder_Req pr : a) {
					long porderreqid_link = pr.getId();
					long pcontract_poid_link = pr.getPcontract_poid_link();
					List<POrder> p = porderService.getByPOrder_Req(pcontract_poid_link, porderreqid_link);
					
					if(p.size() == 0)
						result.add(pr);
				}
				
//				if(a.size()>0)
//					response.data.addAll(a);
				if(result.size()>0)
					response.data.addAll(result);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_Req_getbyorg_response>(response, HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_Req_getbyorg_response>(response, HttpStatus.BAD_REQUEST);
		}
		
	}
	
	

	@RequestMapping(value = "/gen_porder", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> GenPOrder(HttpServletRequest request,
			@RequestBody POrder_Req_GenPOrder_Request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrder_Req porder_req = porder_req_Service.findOne(entity.porderreqid_link);
			
			//Lay thong tin PO
			PContract_PO thePO = pcontract_POService.findOne(porder_req.getPcontract_poid_link());
			if (thePO.getStatus() == POStatus.PO_STATUS_CONFIRMED){
				String po_code = thePO.getPo_vendor().length() > 0?thePO.getPo_vendor():thePO.getPo_buyer();
				List<PContractProductSKU> po_SKUList = pcontract_ProductSKUService.getbypo_and_product_free(entity.porderreqid_link, porder_req.getPcontractid_link(), porder_req.getPcontract_poid_link(), entity.productid_link);				
				POrder thePOrder = porderService.get_oneby_po_org_product(orgrootid_link, porder_req.getGranttoorgid_link(), thePO.getId(), entity.productid_link);
				List<POrder_Product_SKU> porder_SKU = new ArrayList<POrder_Product_SKU>();
				Integer totalorder = 0;
				
				List<String> size_list = null;
				List<String> color_list = null;
				if (entity.size_list.length() > 0){
					String[] size_arr = entity.size_list.split(";");
					size_list = Arrays.asList(size_arr);
				}
				if (entity.color_list.length() > 0){
					String[] color_arr = entity.color_list.split(";");
					color_list = Arrays.asList(color_arr);
				}
				//Tao danh sach SKU, loc theo size_list va color_list
				for(PContractProductSKU theSKU:po_SKUList){
					if (null != size_list){
						if (!size_list.contains(theSKU.getSizeid_link().toString())){
							continue;
						}
					}
					if (null != color_list){
						if (!color_list.contains(theSKU.getColor_id().toString())){
							continue;
						}
					}
					POrder_Product_SKU theporder_SKU = new POrder_Product_SKU();
					
					theporder_SKU.setOrgrootid_link(orgrootid_link);
					theporder_SKU.setProductid_link(theSKU.getProductid_link());
					theporder_SKU.setSkuid_link(theSKU.getSkuid_link());
					theporder_SKU.setPquantity_porder(theSKU.getPquantity_porder());
					theporder_SKU.setPquantity_production(theSKU.getPquantity_production());
					theporder_SKU.setPquantity_sample(theSKU.getPquantity_sample());
					theporder_SKU.setPquantity_total(theSKU.getPquantity_total());
					
					totalorder += null==theSKU.getPquantity_total()?0:theSKU.getPquantity_total();
					
					porder_SKU.add(theporder_SKU);
				}
				
				long porderid_link = 0;
				if (porder_SKU.size() > 0){
					if (null != thePOrder){
						porderid_link = thePOrder.getId();
						//Xoa product SKU
						for(POrder_Product_SKU thePOrderSKU: thePOrder.getPorder_product_sku()){
							porder_ProductSKUService.delete(thePOrderSKU);
						}
						if(thePOrder.getStatus() == POrderStatus.PORDER_STATUS_UNCONFIRM) {
							thePOrder.setStatus(POrderStatus.PORDER_STATUS_FREE);
							
							//tao viec trong taskboard
							long pcontractid_link = thePO.getPcontractid_link();
							long productid_link = entity.productid_link;
							long porder_req_id_link = entity.porderreqid_link;
							
							List<Long> list_task = taskobjectService.getby_pcontract_and_product(pcontractid_link, productid_link, porder_req_id_link);
							for(Long taskid_link : list_task) {
								//Lay checklist cua task
								long tasktype_checklits_id_link = 10;
								List<Task_CheckList> list_sub = checklistService.getby_taskid_link_and_typechecklist(taskid_link, tasktype_checklits_id_link);
								
								if(list_sub.size() > 0 ) {
									Task task = taskService.findOne(taskid_link);
									task.setDatefinished(new Date());
									task.setStatusid_link(2);
									taskService.save(task);
									
									Task_Flow flow = new Task_Flow();
									flow.setDatecreated(new Date());
									flow.setDescription("Đã tạo lệnh SX");
									flow.setFlowstatusid_link(3);
									flow.setFromuserid_link(user.getId());
									flow.setId(null);
									flow.setOrgrootid_link(orgrootid_link);
									flow.setTaskid_link(taskid_link);
									flow.setTaskstatusid_link(2);
									flow.setTouserid_link(task.getUsercreatedid_link());
									commentService.save(flow);
								}
								
								for(Task_CheckList checklist : list_sub) {
									checklist.setDone(true);
									checklistService.save(checklist);
								}
							}
							
							//Tao viec moi 
							long orgid_link = porder_req.getGranttoorgid_link();
							long userid_link = user.getId();
							Long userinchargeid_link = null;
							List<Task_Object> list_object = new ArrayList<Task_Object>();
							
							Task_Object obj_pcontract = new Task_Object();
							obj_pcontract.setId(null);
							obj_pcontract.setObjectid_link(thePO.getPcontractid_link());
							obj_pcontract.setOrgrootid_link(orgrootid_link);
							obj_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
							list_object.add(obj_pcontract);
							
							Task_Object obj_product = new Task_Object();
							obj_product.setId(null);
							obj_product.setObjectid_link(entity.productid_link);
							obj_product.setOrgrootid_link(orgrootid_link);
							obj_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
							list_object.add(obj_product);
							
							Task_Object obj_req = new Task_Object();
							obj_req.setId(null);
							obj_req.setObjectid_link(entity.porderreqid_link);
							obj_req.setOrgrootid_link(orgrootid_link);
							obj_req.setTaskobjecttypeid_link((long)TaskObjectType_Name.YeuCauSanXuat);
							list_object.add(obj_req);
							
							Task_Object obj_porder = new Task_Object();
							obj_porder.setId(null);
							obj_porder.setObjectid_link(porderid_link);
							obj_porder.setOrgrootid_link(orgrootid_link);
							obj_porder.setTaskobjecttypeid_link((long)TaskObjectType_Name.LenhSanXuat);
							list_object.add(obj_porder);
							
							Task_Object obj_po = new Task_Object();
							obj_po.setId(null);
							obj_po.setObjectid_link(thePO.getId());
							obj_po.setOrgrootid_link(orgrootid_link);
							obj_po.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
							list_object.add(obj_po);

							long tasktypeid_link_phanchuyen = 5; //type phan chuyen
							commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_phanchuyen, list_object, userinchargeid_link);
							
							long tasktypeid_link_dmsx = 6; //type phan chuyen
							commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_dmsx, list_object, userinchargeid_link);
							
						}
						
						thePOrder.setTotalorder(totalorder);
						thePOrder.getPorder_product_sku().clear();
						for(POrder_Product_SKU theSKU: porder_SKU){
							thePOrder.getPorder_product_sku().add(theSKU);
						}

						porderService.savePOrder(thePOrder, po_code);
						
						porder_req.setTotalorder(totalorder);
						porder_req_Service.save(porder_req);
					} else {
						POrder porder = new POrder();
						
						porder.setPcontractid_link(thePO.getPcontractid_link());
						porder.setPcontract_poid_link(thePO.getId());
						porder.setOrdercode(po_code);
						porder.setPorderreqid_link(porder_req.getId());
						
						porder.setGolivedate(thePO.getShipdate());
						porder.setFinishdate_plan(thePO.getShipdate());
						porder.setProductiondate(thePO.getProductiondate());
						porder.setProductiondate_plan(thePO.getProductiondate());
						
						porder.setGranttoorgid_link(porder_req.getGranttoorgid_link());
						porder.setProductid_link(entity.productid_link);
						
						porder.setTotalorder(totalorder);
						
						porder.setOrgrootid_link(orgrootid_link);
						porder.setOrderdate(new Date());
						porder.setUsercreatedid_link(user.getId());
						porder.setStatus(POrderStatus.PORDER_STATUS_FREE);
						porder.setTimecreated(new Date());	
						
						porder.setPorder_product_sku(porder_SKU);
						
						porderid_link = porderService.savePOrder(porder, po_code);
						
						porder_req.setTotalorder(totalorder);
						porder_req_Service.save(porder_req);
						
						//Tao viec trong taskboard
						long pcontractid_link = thePO.getPcontractid_link();
						long productid_link = entity.productid_link;
						long porder_req_id_link = entity.porderreqid_link;
						
						List<Long> list_task = taskobjectService.getby_pcontract_and_product(pcontractid_link, productid_link, porder_req_id_link);
						for(Long taskid_link : list_task) {
							//Lay checklist cua task
							long tasktype_checklits_id_link = 10;
							List<Task_CheckList> list_sub = checklistService.getby_taskid_link_and_typechecklist(taskid_link, tasktype_checklits_id_link);
							
							if(list_sub.size() > 0 ) {
								Task task = taskService.findOne(taskid_link);
								task.setDatefinished(new Date());
								task.setStatusid_link(2);
								taskService.save(task);
								
								Task_Flow flow = new Task_Flow();
								flow.setDatecreated(new Date());
								flow.setDescription("Đã tạo lệnh SX");
								flow.setFlowstatusid_link(3);
								flow.setFromuserid_link(user.getId());
								flow.setId(null);
								flow.setOrgrootid_link(orgrootid_link);
								flow.setTaskid_link(taskid_link);
								flow.setTaskstatusid_link(2);
								flow.setTouserid_link(task.getUsercreatedid_link());
								commentService.save(flow);
							}
							
							for(Task_CheckList checklist : list_sub) {
								checklist.setDone(true);
								checklistService.save(checklist);
							}
						}
						
						//Tao viec moi 
						long orgid_link = porder_req.getGranttoorgid_link();
						long userid_link = user.getId();
						Long userinchargeid_link = null;
						List<Task_Object> list_object = new ArrayList<Task_Object>();
						
						Task_Object obj_pcontract = new Task_Object();
						obj_pcontract.setId(null);
						obj_pcontract.setObjectid_link(thePO.getPcontractid_link());
						obj_pcontract.setOrgrootid_link(orgrootid_link);
						obj_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
						list_object.add(obj_pcontract);
						
						Task_Object obj_product = new Task_Object();
						obj_product.setId(null);
						obj_product.setObjectid_link(entity.productid_link);
						obj_product.setOrgrootid_link(orgrootid_link);
						obj_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
						list_object.add(obj_product);
						
						Task_Object obj_req = new Task_Object();
						obj_req.setId(null);
						obj_req.setObjectid_link(entity.porderreqid_link);
						obj_req.setOrgrootid_link(orgrootid_link);
						obj_req.setTaskobjecttypeid_link((long)TaskObjectType_Name.YeuCauSanXuat);
						list_object.add(obj_req);
						
						Task_Object obj_porder = new Task_Object();
						obj_porder.setId(null);
						obj_porder.setObjectid_link(porderid_link);
						obj_porder.setOrgrootid_link(orgrootid_link);
						obj_porder.setTaskobjecttypeid_link((long)TaskObjectType_Name.LenhSanXuat);
						list_object.add(obj_porder);
						
						Task_Object obj_po = new Task_Object();
						obj_po.setId(null);
						obj_po.setObjectid_link(thePO.getId());
						obj_po.setOrgrootid_link(orgrootid_link);
						obj_po.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
						list_object.add(obj_po);

						long tasktypeid_link_phanchuyen = 5; //type phan chuyen
						commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_phanchuyen, list_object, userinchargeid_link);
						
						long tasktypeid_link_dmsx = 6; //type phan chuyen
						commonService.CreateTask(orgrootid_link, orgid_link, userid_link, tasktypeid_link_dmsx, list_object, userinchargeid_link);
						
					}
				}
				
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Đơn hàng phải được xác nhận trước khi sinh lệnh");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/get_total_select", method = RequestMethod.POST)
	public ResponseEntity<get_totalselect_response> GetTotalSelect(HttpServletRequest request,
			@RequestBody get_totalselect_request entity) {
		get_totalselect_response response = new get_totalselect_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			POrder_Req porder_req = porder_req_Service.findOne(entity.porderreqid_link);

			//Kiem tra san pham co phai la bo hay khong
			long pcontractid_link = porder_req.getPcontractid_link();
			List<ProductPairing> list_pair = pairService.getproduct_pairing_detail_bycontract(orgrootid_link, pcontractid_link, entity.productid_link);
			
			List<PContractProductSKU> po_SKUList = new ArrayList<PContractProductSKU>();
			Integer totalorder = 0;
			List<String> size_list = null;
			List<String> color_list = null;
			
			if(list_pair.size() == 0) {
				po_SKUList = (pcontract_ProductSKUService.getbypo_and_product_free(
						entity.porderreqid_link, porder_req.getPcontractid_link(), porder_req.getPcontract_poid_link(), entity.productid_link));
				
				if (entity.size_list.length() > 0){
					String[] size_arr = entity.size_list.split(";");
					size_list = Arrays.asList(size_arr);
				}
				if (entity.color_list.length() > 0){
					String[] color_arr = entity.color_list.split(";");
					color_list = Arrays.asList(color_arr);
				}
				//Tao danh sach SKU, loc theo size_list va color_list
				for(PContractProductSKU theSKU:po_SKUList){
					if (null != size_list){
						if (!size_list.contains(theSKU.getSizeid_link().toString())){
							continue;
						}
					}
					if (null != color_list){
						if (!color_list.contains(theSKU.getColor_id().toString())){
							continue;
						}
					}
					
					totalorder += null==theSKU.getPquantity_total()?0:theSKU.getPquantity_total();
				}
			}
			else {
				for(ProductPairing pair : list_pair) {
					po_SKUList = (pcontract_ProductSKUService.getbypo_and_product_free(
							entity.porderreqid_link, porder_req.getPcontractid_link(), porder_req.getPcontract_poid_link(), pair.getProductid_link()));
					
					if (entity.size_list.length() > 0){
						String[] size_arr = entity.size_list.split(";");
						size_list = Arrays.asList(size_arr);
					}
					if (entity.color_list.length() > 0){
						String[] color_arr = entity.color_list.split(";");
						color_list = Arrays.asList(color_arr);
					}
					//Tao danh sach SKU, loc theo size_list va color_list
					for(PContractProductSKU theSKU:po_SKUList){
						if (null != size_list){
							if (!size_list.contains(theSKU.getSizeid_link().toString())){
								continue;
							}
						}
						if (null != color_list){
							if (!color_list.contains(theSKU.getColor_id().toString())){
								continue;
							}
						}
						
						totalorder += null==theSKU.getPquantity_total()?0:theSKU.getPquantity_total();
					}
				}
			}
						
			response.total = commonService.FormatNumber(totalorder)+" / "+commonService.FormatNumber(porder_req.getTotalorder());
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<get_totalselect_response>(response, HttpStatus.OK);
			
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<get_totalselect_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete(HttpServletRequest request,
			@RequestBody POrder_Req_GetOne_Request entity) {
		ResponseBase response = new ResponseBase();
		try {
			 POrder_Req thePOrder_Req = porder_req_Service.findOne(entity.id);
			 if (null != thePOrder_Req){
				//Kiem tra xem da co lenh sx dc tao chua, neu co roi --> Bao loi
				if(thePOrder_Req.getPorderlist().size() > 0){
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_POREQ_DELETE_PORDEREXISTED));
					return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);					
				} else {
					porder_req_Service.delete(thePOrder_Req);
	
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_BAD_REQUEST));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
}
