package vn.gpay.gsmart.core.api.porder;

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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import vn.gpay.gsmart.core.actionlog.ActionLogs;
import vn.gpay.gsmart.core.actionlog.IActionLogs_Service;
import vn.gpay.gsmart.core.api.stockout.StockoutDFilterResponse;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOMSKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOMSKU;
import vn.gpay.gsmart.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontratproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_AutoID_Service;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder.POrderFilter;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.porder_product.IPOrder_Product_Service;
import vn.gpay.gsmart.core.porder_product.POrder_Product;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.stockout.StockOut;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.NetworkUtils;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder")
public class POrderAPI {
	@Autowired private IPOrder_Service porderService;
	@Autowired private IPOrder_AutoID_Service porder_AutoID_Service;
	@Autowired private IPOrder_Product_SKU_Service porderskuService;
	@Autowired private IPOrder_Product_Service porderproductService;
	@Autowired private IPContractProductSKUService pskuservice;
    @Autowired private IActionLogs_Service actionLogsRepository;
    @Autowired private IPContractBOMSKUService pcontractBOMSKUService;
    @Autowired private IPOrderBOMSKU_Service porderBOMSKUService;
    @Autowired private ISKU_Service skuService;
    @Autowired private IPOrderProcessing_Service porderprocessingService;
    @Autowired private Common commonService;
    ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create(HttpServletRequest request,
			@RequestBody POrder_Create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long productid_link = entity.productid_link;
			Long orgrootid_link = user.getRootorgid_link();
			Long pcontractid_link = entity.pcontractid_link;
			Integer pquantitytotal = 0;
			
			POrder porder = new POrder();
			
			if (entity.id == null || entity.id == 0) {
				porder.setId((long)0);
				porder.setProductid_link(productid_link);
				porder.setOrgrootid_link(orgrootid_link);
				porder.setOrderdate(new Date());
				porder.setPcontractid_link(entity.pcontractid_link);
				porder.setOrdercode(porder_AutoID_Service.getLastID());
				porder.setGranttoorgid_link(entity.granttoorg_link);
				porder.setUsercreatedid_link(user.getId());
				porder.setStatus(POrderStatus.PORDER_STATUS_FREE);
				porder.setTimecreated(new Date());
			} 
			else {
				porder = porderService.findOne(entity.id);
			}
			
			porder = porderService.save(porder);
			
			List<PContractProductSKU> listsku = entity.sku;
			
			for (PContractProductSKU sku : listsku) {
				SKU orginSKU =skuService.findOne(sku.getSkuid_link());
				if (null != orginSKU){
					POrder_Product_SKU pordersku = new POrder_Product_SKU();
					pordersku.setId(null);
					pordersku.setOrgrootid_link(orgrootid_link);
					pordersku.setPorderid_link(porder.getId());
					pordersku.setPquantity_porder(sku.getPquantity_porder());
					pordersku.setPquantity_sample(sku.getPquantity_sample());
					pordersku.setPquantity_total(sku.getPquantity_total());
					pordersku.setProductid_link(productid_link);
					pordersku.setSkuid_link(sku.getSkuid_link());
					
					porderskuService.save(pordersku);
					
					pquantitytotal += sku.getPquantity_total();
					
					//Update so da phan lenh cua PContract
					updateContractSKU(porder.getId(),pordersku.getSkuid_link());
					
					//Lấy BOM của sản phẩm (SKU) từ Contract
//					List<PContractBOMSKU> lsSKUBOM = pcontractBOMSKUService.getMaterials_BySKUId(sku.getSkuid_link());
					List<PContractBOMSKU> lsSKUBOM = commonService.getBOMSKU_PContract_Product(pcontractid_link, productid_link, listsku);
					for(PContractBOMSKU contractBOM: lsSKUBOM){
						POrderBOMSKU porderBOM = new POrderBOMSKU();
						porderBOM.setOrgrootid_link(orgrootid_link);
						porderBOM.setPcontractid_link(contractBOM.getPcontractid_link());
						porderBOM.setPorderid_link(porder.getId());
						porderBOM.setProductid_link(contractBOM.getProductid_link());
						porderBOM.setSkuid_link(contractBOM.getSkuid_link());
						porderBOM.setMaterialid_link(contractBOM.getMaterialid_link());
						porderBOM.setAmount(contractBOM.getAmount());
						porderBOM.setLost_ratio(contractBOM.getLost_ratio());
						porderBOM.setCreateduserid_link(user.getId());
						porderBOM.setCreateddate(new Date());
						
						porderBOM.setProductcolor_name(orginSKU.getMauSanPham());
						porderBOMSKUService.save(porderBOM);
					}
					
					//Lay BOM tu pcontract_bom_color
//					List<PCon>
//					List<PContractBOMColor> list_bom_color = bomcolorService.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, 0);
				}
			}
			
			POrder_Product porderproduct = new POrder_Product();
			porderproduct.setId(null);
			porderproduct.setOrgrootid_link(orgrootid_link);
			porderproduct.setPorderid_link(porder.getId());
			porderproduct.setPquantity(pquantitytotal);
			porderproduct.setProductid_link(productid_link);
			
			porderproductService.save(porderproduct);
			
			
			//Lưu tổng số lượng sản phẩm theo lệnh
			porder.setTotalorder(pquantitytotal);
			porderService.save(porder);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
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
			 if (null != thePOrder && thePOrder.getStatus() == POrderStatus.PORDER_STATUS_FREE){
				GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
				Long productid_link = thePOrder.getProductid_link();
				Long contractid_link = thePOrder.getPcontractid_link();
				Long orgrootid_link = user.getRootorgid_link();
				
				porderService.delete(thePOrder);

				updateContractGranted(orgrootid_link,contractid_link, productid_link);

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
	
	@RequestMapping(value = "/update_sku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updatePOrder_SKU(HttpServletRequest request,
			@RequestBody POrderSKU_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			POrder_Product_SKU thePOrderSKU = porderskuService.findOne(entity.data.getId());
			 if (null != thePOrderSKU){
				thePOrderSKU.setPquantity_total(entity.data.getPquantity_total());
				porderskuService.save(thePOrderSKU);
				
				updateTotalOrder(thePOrderSKU.getPorderid_link());
				updateContractSKU(thePOrderSKU.getPorderid_link(),thePOrderSKU.getSkuid_link());

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
	private void updateTotalOrder(Long porderid_link){
		POrder thePOrder = porderService.findOne(porderid_link);
		int totalorder = 0;
		for(POrder_Product_SKU thePorderSKU:thePOrder.getPorder_product_sku()){
			totalorder += thePorderSKU.getPquantity_total();
		}
		thePOrder.setTotalorder(totalorder);
		porderService.save(thePOrder);
	}
	private void updateContractSKU(Long porderid_link, Long skuid_link){
		POrder thePOrder = porderService.findOne(porderid_link);
		List<PContractProductSKU> lstSKU = pskuservice.getlistsku_bysku_and_pcontract(skuid_link, thePOrder.getPcontractid_link());
		for(PContractProductSKU theSKU:lstSKU){
			int totalgranted = 0;
			//Duyet danh sach cac Lenhsx cua san pham trong don hang
			List<POrder> lstPOrder = porderService.getByContractAndProduct(thePOrder.getPcontractid_link(), thePOrder.getProductid_link());
			for(POrder order: lstPOrder){
				List<POrder_Product_SKU> lstPorderSKU = porderskuService.getby_porderandsku(order.getId(), skuid_link);
				for(POrder_Product_SKU porderSKU:lstPorderSKU){
					totalgranted += porderSKU.getPquantity_total();
				}
			}
			theSKU.setPquantity_granted(totalgranted);
			pskuservice.save(theSKU);
		}
	}
	private void updateContractGranted(Long orgrootid_link, Long contractid_link, Long productid_link){
		List<PContractProductSKU> lstSKU = pskuservice.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, contractid_link);
		for(PContractProductSKU theSKU:lstSKU){
			int totalgranted = 0;
			//Duyet danh sach cac Lenhsx cua san pham trong don hang
			List<POrder> lstPOrder = porderService.getByContractAndProduct(contractid_link, productid_link);
			for(POrder order: lstPOrder){
				List<POrder_Product_SKU> lstPorderSKU = porderskuService.getby_porderandsku(order.getId(), theSKU.getSkuid_link());
				for(POrder_Product_SKU porderSKU:lstPorderSKU){
					totalgranted += porderSKU.getPquantity_total();
				}
			}
			theSKU.setPquantity_granted(totalgranted);
			pskuservice.save(theSKU);
		}
	}
	
	//Lấy danh sách và tình trạng chuẩn bị NPL của 1 lệnh
	@RequestMapping(value = "/getbalance",method = RequestMethod.POST)
	public ResponseEntity<StockoutDFilterResponse> getBalance(@RequestBody POrderGetBalanceRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		StockoutDFilterResponse response = new StockoutDFilterResponse();
		try {
		//Lay danh sach lenh cho vao chuyen de tao Stockout list
			StockOut theWaitingOrder = new StockOut();
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
}
