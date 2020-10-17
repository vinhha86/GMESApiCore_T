package vn.gpay.gsmart.core.api.porder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.gpay.gsmart.core.actionlog.ActionLogs;
import vn.gpay.gsmart.core.actionlog.IActionLogs_Service;
import vn.gpay.gsmart.core.api.stockout.StockoutDFilterResponse;
import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.category.IUnitService;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontract_po_productivity.IPContract_PO_Productivity_Service;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder.POrderFilter;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_product.IPOrder_Product_Service;
import vn.gpay.gsmart.core.porder_product.POrder_Product;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porder_req.IPOrder_Req_Service;
import vn.gpay.gsmart.core.porder_req.POrder_Req;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.stockin.StockInD;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task.Task;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_checklist.Task_CheckList;
import vn.gpay.gsmart.core.task_flow.ITask_Flow_Service;
import vn.gpay.gsmart.core.task_flow.Task_Flow;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.task_object.Task_Object;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.NetworkUtils;
import vn.gpay.gsmart.core.utils.POStatus;
import vn.gpay.gsmart.core.utils.POrderReqStatus;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.TaskObjectType_Name;

@RestController
@RequestMapping("/api/v1/porder")
public class POrderAPI {
	@Autowired private IPOrder_Service porderService;
	@Autowired private IPContract_PO_Productivity_Service poProductivityService;
	@Autowired private IPContract_POService pcontract_POService;
	@Autowired private IPOrder_Product_SKU_Service porderskuService;
	@Autowired private IPContractProductSKUService pskuservice;
    @Autowired private IActionLogs_Service actionLogsRepository;
    @Autowired private IPOrderProcessing_Service porderprocessingService;
    @Autowired private IOrgService orgService;
    @Autowired private IPOrderGrant_Service pordergrantService;
    @Autowired private IPOrder_Req_Service porder_reqService;
    
	@Autowired private ITask_Object_Service taskobjectService;
	@Autowired private ITask_CheckList_Service checklistService;
	@Autowired private ITask_Service taskService;
	@Autowired private ITask_Flow_Service commentService;
	@Autowired
	IPOrder_Product_Service porderproductService;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	IUnitService unitService;
	@Autowired
	IAttributeValueService attValService;
	@Autowired private Common commonService;
    ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<POrderGetByIDResponse> POrderGetOne(@RequestBody POrder_getbyid_request entity,HttpServletRequest request ) {
		POrderGetByIDResponse response = new POrderGetByIDResponse();
		try {
			
			response.data = porderService.findOne(entity.porderid_link); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderGetByIDResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<POrderGetByIDResponse>(response, HttpStatus.OK);
		}
	}    
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<POrder_Create_response> Create(HttpServletRequest request,
			@RequestBody POrder_Create_request entity) {
		POrder_Create_response response = new POrder_Create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrder porder = entity.data;
			
			//Kiem tra xem POrder_req da duoc tao lenhsx hay chua?
			if (porderService.getByPOrder_Req(porder.getPcontract_poid_link(), porder.getPorderreqid_link()).size() > 0){
				response.setRespcode(ResponseMessage.KEY_RC_PORDER_EXISTED);
				response.setMessage(ResponseMessage.MES_RC_PORDER_EXISTED);
				throw new RuntimeException(ResponseMessage.MES_RC_PORDER_EXISTED);
			} else {
				//Lay thong tin PO
				PContract_PO thePO = pcontract_POService.findOne(porder.getPcontract_poid_link());
				
				if (null !=thePO){
					//Kiem tra da khai bao chi tiet SKU cho san pham trong PO chua
					if (pskuservice.getbypo_and_product(thePO.getId(), porder.getProductid_link()).size() == 0 
							&& entity.isBypassSKUEmpty == false){
						response.setRespcode(ResponseMessage.KEY_RC_PORDER_NOSKU);
						response.setMessage(ResponseMessage.MES_RC_PORDER_NOSKU);
						throw new RuntimeException(ResponseMessage.MES_RC_PORDER_NOSKU);					
					} else {
						String po_code = thePO.getPo_vendor().length() > 0?thePO.getPo_vendor():thePO.getPo_buyer();
						
						if (porder.getId() == null || porder.getId() == 0) {
					
							porder.setGolivedate(thePO.getShipdate());
							porder.setProductiondate(thePO.getProductiondate());
							
							porder.setFinishdate_plan(thePO.getShipdate());
							porder.setProductiondate_plan(thePO.getProductiondate());
							
							porder.setOrgrootid_link(orgrootid_link);
							porder.setOrderdate(new Date());
							porder.setUsercreatedid_link(user.getId());
							porder.setStatus(thePO.getStatus() == POStatus.PO_STATUS_UNCONFIRM?POrderStatus.PORDER_STATUS_UNCONFIRM:POrderStatus.PORDER_STATUS_FREE);
							porder.setTimecreated(new Date());
							
							//Lay thong tin NS target tu chao gia 
							porder.setPlan_productivity(poProductivityService.getProductivityByPOAndProduct(thePO.getParentpoid_link(), porder.getProductid_link()));
						} 
//						Float productiondays = (float)thePO.getProductiondays();
						porder = porderService.savePOrder(calPlan_FinishDate(orgrootid_link, porder), po_code);
						
						//Neu chi co duy nhat 1 Porder_req cho san pham (chi phan lenh cho 1 xuong) --> Tu lay toan bo danh sach SKU
						List<POrder_Req> lsPOrder_Req_product = porder_reqService.getByPOAndProduct(porder.getPcontract_poid_link(), porder.getProductid_link());
						if (lsPOrder_Req_product.size()==1){
							for (PContractProductSKU theProductSKU: pskuservice.getbypo_and_product(porder.getPcontract_poid_link(), porder.getProductid_link())){
								POrder_Product_SKU thePorderSKU = new POrder_Product_SKU();
								thePorderSKU.setOrgrootid_link(orgrootid_link);
								thePorderSKU.setPorderid_link(porder.getId());
								thePorderSKU.setProductid_link(porder.getProductid_link());
								thePorderSKU.setSkuid_link(theProductSKU.getSkuid_link());
								thePorderSKU.setPquantity_porder(theProductSKU.getPquantity_porder());
								thePorderSKU.setPquantity_production(theProductSKU.getPquantity_production());
								thePorderSKU.setPquantity_sample(theProductSKU.getPquantity_sample());
								thePorderSKU.setPquantity_total(theProductSKU.getPquantity_total());
								porderskuService.save(thePorderSKU);
							}
						}
						
						response.id = porder.getId();
//						response.data = porderService.findOne(porder.getId());
						
						//Update lai trng thai cua Porder_req ve da tao lenh
						List<POrder_Req> lsPOrder_Req = porder_reqService.getByOrg_PO_Product(porder.getPcontract_poid_link(), porder.getProductid_link(), porder.getGranttoorgid_link());
						for(POrder_Req thePOrder_Req: lsPOrder_Req){
							thePOrder_Req.setStatus(POrderReqStatus.STATUS_PORDERED);
							porder_reqService.save(thePOrder_Req);
						}
						
						//Update lai trng thai cua Porder_req cua PO cha ve da tao lenh
						List<POrder_Req> lsPOrder_Req_parent = porder_reqService.getByOrg_PO_Product(thePO.getParentpoid_link(), porder.getProductid_link(), porder.getGranttoorgid_link());
						for(POrder_Req thePOrder_Req_parent: lsPOrder_Req_parent){
							thePOrder_Req_parent.setStatus(POrderReqStatus.STATUS_PORDERED);
							porder_reqService.save(thePOrder_Req_parent);
						}
						
						
						//Tao Task
						long userid_link = user.getId();
						long pcontractid_link = porder.getPcontractid_link();
						long pcontract_poid_link = porder.getPcontract_poid_link();
						long porder_req_id_link = porder.getPorderreqid_link();
						long porderid_link = porder.getId();
						long productid_link = porder.getProductid_link();
						long granttoorgid_link = porder.getGranttoorgid_link();
						createTask_AfterPorderCreating(
								orgrootid_link,
								userid_link,
								pcontractid_link,
								pcontract_poid_link,
								porder_req_id_link,
								porderid_link,
								productid_link,
								granttoorgid_link						
							);
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<POrder_Create_response>(response, HttpStatus.OK);					
					}
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_PORDER_NOPO);
					response.setMessage(ResponseMessage.MES_RC_PORDER_NOPO);
					throw new RuntimeException(ResponseMessage.MES_RC_PORDER_NOPO);
				}
			}
		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			if (response.getRespcode() == ResponseBase.RESPCODE_NOERROR){
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Lỗi hệ thống: " + e.getMessage());
			}
			return new ResponseEntity<POrder_Create_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<POrder_Create_response> Update(HttpServletRequest request,
			@RequestBody POrder_Create_request entity) {
		POrder_Create_response response = new POrder_Create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			POrder porder = entity.data;
			//Lay thong tin PO
			PContract_PO thePO = pcontract_POService.findOne(porder.getPcontract_poid_link());
			if (null !=thePO){
//				Float productiondays = (float)thePO.getProductiondays();
				porder = porderService.save(calPlan_FinishDate(orgrootid_link, porder));
				response.data = porder;

				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<POrder_Create_response>(response, HttpStatus.OK);	
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_PORDER_NOPO);
				response.setMessage(ResponseMessage.MES_RC_PORDER_NOPO);
				throw new RuntimeException(ResponseMessage.MES_RC_PORDER_NOPO);
			}

		} catch (RuntimeException e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			if (response.getRespcode() == ResponseBase.RESPCODE_NOERROR){
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Lỗi hệ thống: " + e.getMessage());
			}
			return new ResponseEntity<POrder_Create_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	private POrder calPlan_FinishDate(Long orgrootid_link, POrder porder){
		if (null != porder.getTotalorder() && 0 != porder.getTotalorder() && 
				null != porder.getPlan_productivity() && 0 != porder.getPlan_productivity()){
			//Tinh toan SL chuyen yeu cau
			Float totalorder = (float)porder.getTotalorder();
			Float plan_productivity = (float)porder.getPlan_productivity();		
			
			Integer plan_duration = Math.round(totalorder/plan_productivity);	
			Date finishdate_plan =  commonService.Date_Add_with_holiday(porder.getProductiondate_plan(), plan_duration, orgrootid_link, Calendar.getInstance().get(Calendar.YEAR));
			
			porder.setFinishdate_plan(finishdate_plan);
			porder.setPlan_duration(plan_duration);
//			porder.setPlan_linerequired(Precision.round(iSLNgaySX/productiondays,1));
		} else {
			porder.setPlan_linerequired(null);				
		}		
		return porder;
	}
	
	@RequestMapping(value = "/get_free_bygolivedate", method = RequestMethod.POST)
	public ResponseEntity<POrderResponse> get_free_bygolivedate(HttpServletRequest request,
			@RequestBody POrder_getbygolivedate_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgid_link = user.getOrgid_link();
		POrderResponse response = new POrderResponse();
		try {
			long i = (long)0;
			List<POrder> lsPOrder = porderService.get_free_bygolivedate(entity.golivedate_from, entity.golivedate_to, orgid_link, "", i, i);
			List<String> orgTypes = new ArrayList<String>();
			orgTypes.add("13");
			orgTypes.add("14");
			List<Org> lsOrgChild = orgService.getorgChildrenbyOrg(orgid_link,orgTypes);
			for(Org theOrg:lsOrgChild){
				long orgid = theOrg.getId();
				lsPOrder.addAll(porderService.get_free_bygolivedate(entity.golivedate_from, entity.golivedate_to, orgid , "", i, i));
			}
			response.data = lsPOrder;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrderResponse>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrderResponse>(response, HttpStatus.OK);
		}
	}
		
	@RequestMapping(value = "/get_byproduct", method = RequestMethod.POST)
	public ResponseEntity<POrder_getbyproduct_response> GetByProduct(HttpServletRequest request,
			@RequestBody POrder_getbyproduct_request entity) {
		POrder_getbyproduct_response response = new POrder_getbyproduct_response();
		try {
			Long productid_link = entity.productid_link;
			
			response.data = porderskuService.getby_productid_link(productid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_getbyproduct_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_getbyproduct_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/get_product_sku", method = RequestMethod.POST)
	public ResponseEntity<POrder_getbyproduct_response> get_Product_SKU(HttpServletRequest request,
			@RequestBody POrder_getbyid_request entity) {
		POrder_getbyproduct_response response = new POrder_getbyproduct_response();
		try {
			Long porderid_link = entity.porderid_link;
			
			response.data = porderskuService.getby_porder(porderid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_getbyproduct_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_getbyproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/get_bycontract", method = RequestMethod.POST)
	public ResponseEntity<POrder_getbycontract_response> GetByContract(HttpServletRequest request,
			@RequestBody POrder_getbycontract_request entity) {
		POrder_getbycontract_response response = new POrder_getbycontract_response();
		try {
			if (null != entity.productid_link)
				response.data = porderService.getByContractAndProduct(entity.pcontractid_link, entity.productid_link);
			else
				response.data = porderService.getByContract(entity.pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_getbycontract_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_getbycontract_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/get_bypo", method = RequestMethod.POST)
	public ResponseEntity<POrder_getbycontract_response> GetByPO(HttpServletRequest request,
			@RequestBody POrder_getbypo_request entity) {
		POrder_getbycontract_response response = new POrder_getbycontract_response();
		try {
			if (null != entity.pcontract_poid_link)
				response.data = porderService.getByContractAndPO(entity.pcontractid_link, entity.pcontract_poid_link);
			else
				response.data = porderService.getByContract(entity.pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_getbycontract_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_getbycontract_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/get_byporder_req", method = RequestMethod.POST)
	public ResponseEntity<POrder_getbycontract_response> GetByPOrder_Req(HttpServletRequest request,
			@RequestBody POrder_getbyporder_req_request entity) {
		POrder_getbycontract_response response = new POrder_getbycontract_response();
		try {
			response.data = porderService.getByPOrder_Req(entity.pcontract_poid_link, entity.porderreqid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_getbycontract_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_getbycontract_response>(response, HttpStatus.OK);
		}
	}	
	
	@RequestMapping(value = "/getfilter",method = RequestMethod.POST)
	public ResponseEntity<POrderFilterResponse> getFilter(@RequestBody POrderFilterRequest entity, HttpServletRequest request) {
		POrderFilterResponse response = new POrderFilterResponse();
		try {
			String[] arrOfStatus = entity.getOrderstatus().split(",", 10);
			for (String sStatus : arrOfStatus){
				try {
					int iStatus = Integer.parseInt(sStatus);
					List<POrder> lsOrders = porderService.getFilter(
							entity.getOrdercode(),
							iStatus,
							entity.getGranttoorgid_link(),
							entity.getCollection(),
							entity.getSeason(),
							entity.getSalaryyear(),
							entity.getSalarymonth(),
							entity.getProcessingdate_from(),
							entity.getProcessingdate_to()
							);
					for(POrder theOrder: lsOrders){
						POrderFilter theOrderResult =  new POrderFilter();
						theOrderResult.setId(theOrder.getId());
						theOrderResult.setPorderid_link(theOrder.getId());
						theOrderResult.setPcontractid_link(theOrder.getPcontractid_link());
						theOrderResult.setContractcode(theOrder.getContractcode());
						theOrderResult.setGranttoorgid_link(theOrder.getGranttoorgid_link());
						theOrderResult.setGranttoorgname(theOrder.getGranttoorgname());
						theOrderResult.setOrdercode(theOrder.getOrdercode());
						theOrderResult.setTotalorder(theOrder.getTotalorder());
						theOrderResult.setSalarymonth(theOrder.getSalarymonth());
						theOrderResult.setSalaryyear(theOrder.getSalarymonth());
						theOrderResult.setComment(theOrder.getComment());
						theOrderResult.setContractcode(theOrder.getContractcode());
						theOrderResult.setStatus(theOrder.getStatus());
						
						theOrderResult.setOrderdate(theOrder.getOrderdate());
						theOrderResult.setGolivedate(theOrder.getGolivedate());
						theOrderResult.setGolivedesc(theOrder.getGolivedesc());
						theOrderResult.setProductiondate(theOrder.getProductiondate());
						theOrderResult.setProductionyear(theOrder.getProductionyear());
						
						theOrderResult.setMaterial_date(theOrder.getMaterial_date());
						theOrderResult.setSample_date(theOrder.getSample_date());
						theOrderResult.setCut_date(theOrder.getCut_date());
						theOrderResult.setPacking_date(theOrder.getPacking_date());
						theOrderResult.setQc_date(theOrder.getQc_date());
						theOrderResult.setStockout_date(theOrder.getStockout_date());
						
						//Lay thong tin processing
						List<POrderProcessing> lstProcessing = porderprocessingService.getByBeforeDateAndOrderID(theOrder.getId(), new Date());
						if (lstProcessing.size() > 0){
							POrderProcessing theProcessing = lstProcessing.get(0);
							
							theOrderResult.setGranttolineid_link(theProcessing.getGranttoorgid_link());
							theOrderResult.setGranttolinename(theProcessing.getGranttoorgname());
							theOrderResult.setAmountcutsum(theProcessing.getAmountcutsum());
							theOrderResult.setAmountinputsum(theProcessing.getAmountinputsum());
							theOrderResult.setAmountoutputsum(theProcessing.getAmountoutputsum());
							theOrderResult.setAmountpackedsum(theProcessing.getAmountpackedsum());
							theOrderResult.setAmountpackstockedsum(theProcessing.getAmountpackedsum());
						}
						response.data.add(theOrderResult);
					}
				} catch(NumberFormatException ex){
					
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderFilterResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderFilterResponse>(response, HttpStatus.BAD_REQUEST);
		}    			
	}	
	@RequestMapping(value = "/ungranted",method = RequestMethod.POST)
	public ResponseEntity<POrderResponse> getUnGranted(HttpServletRequest request) {
		POrderResponse response = new POrderResponse();
		try {
			response.data=porderService.getByStatus(POrderStatus.PORDER_STATUS_FREE);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<POrderResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<POrderResponse>(HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> deletePOrder(HttpServletRequest request,
			@RequestBody POrder_delete_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			 POrder thePOrder = porderService.findOne(entity.id);
			 if (null != thePOrder && thePOrder.getStatus() <= POrderStatus.PORDER_STATUS_FREE){
//				GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//				Long productid_link = thePOrder.getProductid_link();
//				Long contractid_link = thePOrder.getPcontractid_link();
//				Long orgrootid_link = user.getRootorgid_link();
				
				//Check if having POrder_Grant. Refuse deleting if have
				if (pordergrantService.getByOrderId(thePOrder.getId()).size() > 0){
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Lệnh sản xuất đã được phân chuyền! Cần hủy phân chuyền trước khi xóa lệnh ");
					return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
				}
				
				porderService.delete(thePOrder);

//				updateContractGranted(orgrootid_link,contractid_link, productid_link);

				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Lệnh sản xuất đã được phân chuyền! Cần hủy phân chuyền trước khi xóa lệnh ");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/update_sku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updatePOrder_SKU(HttpServletRequest request,
			@RequestBody POrderSKU_update_request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();		
		ResponseBase response = new ResponseBase();
		try {
			POrder_Product_SKU thePOrderSKU = porderskuService.findOne(entity.data.getId());
			 if (null != thePOrderSKU){
				 POrder thePorder = porderService.findOne(thePOrderSKU.getPorderid_link());
				//Kiem tra neu so dieu chinh nhieu hon so con lai --> Khong cho sua
				long pcontractid_link = thePorder.getPcontractid_link();
				long pcontract_poid_link = thePorder.getPcontract_poid_link();
				long productid_link = thePOrderSKU.getProductid_link();
				List<PContractProductSKU> data = pskuservice.getPOSKU_Free_ByProduct(productid_link, pcontract_poid_link);
				PContractProductSKU poSKU = data.stream().filter(sku -> sku.getSkuid_link().equals(thePOrderSKU.getSkuid_link())).findAny().orElse(null);
				if (null != poSKU){
					int q_total = null != poSKU.getPquantity_total()?poSKU.getPquantity_total():0;
					int q_granted = null != poSKU.getPquantity_lenhsx()?poSKU.getPquantity_lenhsx():0;
					q_granted = q_granted - (null != thePOrderSKU.getPquantity_total()?thePOrderSKU.getPquantity_total():0);
					if ((q_total - q_granted) >= entity.data.getPquantity_total()){
						thePOrderSKU.setPquantity_total(entity.data.getPquantity_total());
						porderskuService.save(thePOrderSKU);
						
						updateTotalOrder(orgrootid_link, thePOrderSKU.getPorderid_link());
						updatePOStatus(orgrootid_link,pcontract_poid_link,pcontractid_link);
//						updateContractSKU(thePOrderSKU.getPorderid_link(),thePOrderSKU.getSkuid_link());
						
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
						return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);						
					} else {
						response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
						response.setMessage("Số lượng không hợp lệ");
						return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);						
					}
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Không tìm thấy SKU của PO");
					return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);						
				}

			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("Không tìm thấy SKU của Lệnh SX");
				return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/create_sku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> createPOrder_SKU(HttpServletRequest request,
			@RequestBody POrderSKU_update_request entity) {
		ResponseBase response = new ResponseBase();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
		entity.data.setOrgrootid_link(orgrootid_link);
		
		try {
			List<POrder_Product_SKU> lstPOrderSKU = porderskuService.getby_porderandsku(entity.data.getPorderid_link(),entity.data.getSkuid_link());
			 if (lstPOrderSKU.size() == 0){
				porderskuService.save(entity.data);
				
				updateTotalOrder(orgrootid_link, entity.data.getPorderid_link());
				
//				updateContractSKU(entity.data.getPorderid_link(),entity.data.getSkuid_link());

				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
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
	@RequestMapping(value = "/create_skulist", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> createPOrder_SKUList(HttpServletRequest request,
			@RequestBody POrderSKU_delete_request entity) {
		ResponseBase response = new ResponseBase();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
	
		try {
			POrder thePorder = porderService.findOne(entity.porderid_link);
			long pcontractid_link = thePorder.getPcontractid_link();
			long pcontract_poid_link = thePorder.getPcontract_poid_link();
		
			for(POrder_Product_SKU thePOrderSKU: entity.data){
				List<POrder_Product_SKU> lstPOrderSKU = porderskuService.getby_porderandsku(thePOrderSKU.getPorderid_link(),thePOrderSKU.getSkuid_link());
				if (lstPOrderSKU.size() == 0){
					thePOrderSKU.setOrgrootid_link(orgrootid_link);
					porderskuService.save(thePOrderSKU);
				} else {
					//Update SL SKU trong lenh tang theo SL moi
					for(POrder_Product_SKU theSKU:lstPOrderSKU){
						theSKU.setPquantity_total(
								(null != theSKU.getPquantity_total()?theSKU.getPquantity_total():0) + 
								(null !=thePOrderSKU.getPquantity_total()?thePOrderSKU.getPquantity_total():0));
					}
				}
			}
			
			updateTotalOrder(orgrootid_link, entity.porderid_link);
			updatePOStatus(orgrootid_link,pcontract_poid_link,pcontractid_link);
//			updateContractSKU(thePOrderSKU.getPorderid_link(),thePOrderSKU.getSkuid_link());
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
	@RequestMapping(value = "/delete_sku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> deletePOrder_SKU(HttpServletRequest request,
			@RequestBody POrderSKU_delete_request entity) {
		ResponseBase response = new ResponseBase();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long orgrootid_link = user.getRootorgid_link();
		
		try {
			POrder thePorder = porderService.findOne(entity.porderid_link);
			long pcontractid_link = thePorder.getPcontractid_link();
			long pcontract_poid_link = thePorder.getPcontract_poid_link();
			
			for(POrder_Product_SKU thePOrderSKU: entity.data){
				porderskuService.delete(thePOrderSKU);
			} 
			updateTotalOrder(orgrootid_link, entity.porderid_link);
			updatePOStatus(orgrootid_link,pcontract_poid_link,pcontractid_link);
//			updateContractSKU(thePOrderSKU.getPorderid_link(),thePOrderSKU.getSkuid_link());
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}	
	private void updateTotalOrder(Long orgrootid_link, Long porderid_link){
		POrder thePOrder = porderService.findOne(porderid_link);
		int totalorder = 0;
		for(POrder_Product_SKU thePorderSKU:thePOrder.getPorder_product_sku()){
			totalorder += null==thePorderSKU.getPquantity_total()?0:thePorderSKU.getPquantity_total();
		}
		thePOrder.setTotalorder(totalorder);
		calPlan_FinishDate(orgrootid_link,thePOrder);
		porderService.save(thePOrder);
	}
	
	private void updatePOStatus(long orgrootid_link, long pcontract_poid_link, long pcontractid_link){
		PContract_PO thePO = pcontract_POService.findOne(pcontract_poid_link);
		if (null != thePO){
			int totalFree = 0;
			List<PContractProductSKU> data = pskuservice.getlistsku_bypo_and_pcontract_free(orgrootid_link, pcontract_poid_link,pcontractid_link);
			for(PContractProductSKU theSKU:data){
				totalFree += (null != theSKU.getPquantity_total()?theSKU.getPquantity_total():0) - (null !=theSKU.getPquantity_lenhsx()?theSKU.getPquantity_lenhsx():0);
			}
			if (totalFree == 0){
				if (thePO.getStatus() == POStatus.PO_STATUS_CONFIRMED) thePO.setStatus(POStatus.PO_STATUS_PORDER_ALL);
				
			} else {
				if (thePO.getStatus() == POStatus.PO_STATUS_PORDER_ALL) thePO.setStatus(POStatus.PO_STATUS_CONFIRMED);
			}
			pcontract_POService.save(thePO);
		}
	}
	
	//Hien tai ko dung den func nay
//	private void updateContractSKU(Long porderid_link, Long skuid_link){
//		POrder thePOrder = porderService.findOne(porderid_link);
//		List<PContractProductSKU> lstSKU = pskuservice.getlistsku_bysku_and_pcontract(skuid_link, thePOrder.getPcontractid_link());
//		for(PContractProductSKU theSKU:lstSKU){
//			int totalgranted = 0;
//			//Duyet danh sach cac Lenhsx cua san pham trong don hang
//			List<POrder> lstPOrder = porderService.getByContractAndProduct(thePOrder.getPcontractid_link(), thePOrder.getProductid_link());
//			for(POrder order: lstPOrder){
//				List<POrder_Product_SKU> lstPorderSKU = porderskuService.getby_porderandsku(order.getId(), skuid_link);
//				for(POrder_Product_SKU porderSKU:lstPorderSKU){
//					totalgranted += porderSKU.getPquantity_total();
//				}
//			}
//			theSKU.setPquantity_granted(totalgranted);
//			pskuservice.save(theSKU);
//		}
//	}
	
	//Lấy danh sách và tình trạng chuẩn bị NPL của 1 lệnh
	@RequestMapping(value = "/getbalance",method = RequestMethod.POST)
	public ResponseEntity<StockoutDFilterResponse> getBalance(@RequestBody POrderGetBalanceRequest entity, HttpServletRequest request) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StockoutDFilterResponse response = new StockoutDFilterResponse();
		try {
		//Lay danh sach lenh cho vao chuyen de tao Stockout list
//			StockOut theWaitingOrder = new StockOut();
//			theWaitingOrder.setPordercode(entity.ordercode);
//			
//			//Lay danh sach NPL cua lenh tu IVY ERP
//			theWaitingOrder.setStockoutd(erpOrderConnect.lenhsx_get_nguyenlieu(ivy_uri, user.getAppuser().getId(), user.getAppuser().getLoginname(), entity.ordercode));
//			
//			//Tinh toan so check, process, stockout cho moi dong stockoutd
//			for(StockoutD theSku:theWaitingOrder.getStockoutd()){
//				theSku.setTotalydscheck(stockoutpklistRepository.getAvailableYdscheckSumBySku(theSku.getSkucode()));
//				theSku.setTotalydsprocessed(stockoutpklistRepository.getAvailableYdsprocessedSumBySku(theSku.getSkucode()));
//				theSku.setTotalydsstockout(stockoutpklistRepository.getStockoutSumBySkuAndOrdercode(theSku.getSkucode(),theWaitingOrder.getPordercode()));
//				if (entity.isGetMaterialUsedBy != 0) 
//					theSku.setExtrainfo(erpMaterialConnect.npl_get_lenhsx(ivy_uri, user.getAppuser().getLoginname(), theSku.getSkucode(), entity.ordercode));
//			}
//			
////			recalStockout_Total(theWaitingOrder);
//			
//			response.data=theWaitingOrder.getStockoutd();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<StockoutDFilterResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutDFilterResponse>(HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/updatebalance",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updateBalance(@RequestBody PProcessBalanceStatusRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = NetworkUtils.getClientIp(request);
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setPorderid_link(entity.porderid_link);
		actionLogs.setOrdercode(entity.ordercode);
		actionLogs.setAction_task("porder_updatebalance");
		try {
			actionLogs.setAction_content(mapper.writeValueAsString(entity));
		} catch (JsonProcessingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		//String loginname = user.getAppuser().getLoginname();
		
		ResponseBase response = new ResponseBase();
		try {
			POrder pOrder = porderService.findOne(entity.porderid_link);
			if (null != pOrder){
				pOrder.setBalance_status(entity.balance_status);
				pOrder.setBalance_date(entity.balance_date);
				pOrder.setBalance_rate(entity.balance_rate);
				porderService.save(pOrder);
			}
			else {
				POrder pOrder_New = new POrder();
				pOrder_New.setOrdercode(entity.ordercode);
				pOrder_New.setBalance_status(entity.balance_status);
				pOrder_New.setBalance_date(entity.balance_date);
				pOrder_New.setBalance_rate(entity.balance_rate);
				pOrder_New.setTimecreated(new Date());
				porderService.save(pOrder_New);
			}
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}    			
	}	
	
	@RequestMapping(value = "/setsalarymonth",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> setSalaryMonth(@RequestBody POrderSetSalaryMonthRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = NetworkUtils.getClientIp(request);
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("setsalarymonth_erp");
		
		ResponseBase response = new ResponseBase();
		try {
			boolean isAllActionDoneWell = true;
	        //For each selected order --> set Ready and update Local Database for ProductionDate
			for(POrderFilter porder: entity.data){
				//Update to LocalDB
				actionLogs.setOrdercode(porder.getOrdercode());
				POrder theOrder = porderService.findOne(porder.getId());
				if (null != theOrder){
					theOrder.setSalarymonth(porder.getSalarymonth());
					theOrder.setSalaryyear(porder.getSalaryyear());
					porderService.save(theOrder);
				} else
					isAllActionDoneWell = false;
			}
			if (isAllActionDoneWell){
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));				
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));				
				return new ResponseEntity<ResponseBase>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/get_porder_stockin", method = RequestMethod.POST)
	public ResponseEntity<porder_get_stockin_response> Get_Porder_Stockin(HttpServletRequest request,
			@RequestBody porder_get_stockin_request entity) {
		porder_get_stockin_response response = new porder_get_stockin_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			String ordercode = entity.ordercode;
			List<StockInD> list = new ArrayList<StockInD>();

			POrder porder = porderService.get_by_code(ordercode, orgrootid_link).get(0);
			Long porderid_link = porder.getId();

			// get SKU within porder
			List<POrder_Product_SKU> list_sku = porderskuService.getlist_sku_in_porder(orgrootid_link, porderid_link);
			// get product within porder
			List<POrder_Product> list_product = porderproductService.get_product_inporder(orgrootid_link,
					porderid_link);

			for (POrder_Product_SKU porder_sku : list_sku) {
				Float unitprice = (float) 0;
				SKU sku = skuService.findOne(porder_sku.getSkuid_link());
				Unit unit = unitService.findOne(porder_sku.getUnitid_link());
				Attributevalue attMau = attValService.findOne(porder_sku.getColorid_link());
				Attributevalue attCo = attValService.findOne(porder_sku.getSizeid_link());

				for (POrder_Product obj : list_product) {
					if (obj.getProductid_link().equals(porder_sku.getProductid_link())) {
						unitprice = Float.parseFloat("0"+obj.getEmt());
					}
				}

				StockInD stockind = new StockInD();
				stockind.setColorid_link(porder_sku.getColorid_link());
				stockind.setId(null);
				stockind.setOrgrootid_link(orgrootid_link);
				stockind.setSizeid_link(porder_sku.getSizeid_link());
				stockind.setSkucode(porder_sku.getSkucode());
				stockind.setSkuid_link(porder_sku.getSkuid_link());
				stockind.setSkutypeid_link(porder_sku.getSkutypeid_link());
				stockind.setStockinid_link(null);
				stockind.setTimecreate(new Date());
				stockind.setTotalpackage(0);
				stockind.setTotalpackage_order(porder_sku.getPquantity_total());
				stockind.setUnitid_link(porder_sku.getUnitid_link());
				stockind.setUsercreateid_link(user.getId());
				stockind.setUnitprice(unitprice);
				stockind.setSku(sku);
				stockind.setUnit(unit);
				stockind.setPorder_year(porder_sku.getPorder_year());
				stockind.setAttMau(attMau);
				stockind.setAttCo(attCo);

				list.add(stockind);
			}

			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}

		return new ResponseEntity<porder_get_stockin_response>(response, HttpStatus.OK);
	}
	
	//Sau khi tao lenh sx --> yeu cau phan chuyen va lap dinh muc sx
	private void createTask_AfterPorderCreating(
			long orgrootid_link,
			long userid_link,
			long pcontractid_link,
			long pcontract_poid_link,
			long porder_req_id_link,
			long porderid_link,
			long productid_link,
			long granttoorgid_link
			){
		try {
			//Tao viec trong taskboard
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
					flow.setFromuserid_link(userid_link);
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
			Long userinchargeid_link = null;
			List<Task_Object> list_object = new ArrayList<Task_Object>();
			
			Task_Object obj_pcontract = new Task_Object();
			obj_pcontract.setId(null);
			obj_pcontract.setObjectid_link(pcontractid_link);
			obj_pcontract.setOrgrootid_link(orgrootid_link);
			obj_pcontract.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHang);
			list_object.add(obj_pcontract);
			
			Task_Object obj_product = new Task_Object();
			obj_product.setId(null);
			obj_product.setObjectid_link(productid_link);
			obj_product.setOrgrootid_link(orgrootid_link);
			obj_product.setTaskobjecttypeid_link((long)TaskObjectType_Name.SanPham);
			list_object.add(obj_product);
			
			Task_Object obj_req = new Task_Object();
			obj_req.setId(null);
			obj_req.setObjectid_link(porder_req_id_link);
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
			obj_po.setObjectid_link(pcontract_poid_link);
			obj_po.setOrgrootid_link(orgrootid_link);
			obj_po.setTaskobjecttypeid_link((long)TaskObjectType_Name.DonHangPO);
			list_object.add(obj_po);
	
			long tasktypeid_link_phanchuyen = 5; //type phan chuyen
			commonService.CreateTask(orgrootid_link, granttoorgid_link, userid_link, tasktypeid_link_phanchuyen, list_object, userinchargeid_link);
			
			long tasktypeid_link_dmsx = 6; //type phan chuyen
			commonService.CreateTask(orgrootid_link, granttoorgid_link, userid_link, tasktypeid_link_dmsx, list_object, userinchargeid_link);
		} catch (Exception ex){
			ex.printStackTrace();
		}
	}
}
