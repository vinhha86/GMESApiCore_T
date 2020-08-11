package vn.gpay.gsmart.core.api.stockout;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.actionlog.ActionLogs;
import vn.gpay.gsmart.core.actionlog.IActionLogs_Service;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU_By_Color;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.stockout.IStockOutDService;
import vn.gpay.gsmart.core.stockout.IStockOutPklistService;
import vn.gpay.gsmart.core.stockout.IStockOutService;
import vn.gpay.gsmart.core.stockout.StockOut;
import vn.gpay.gsmart.core.stockout.StockOutD;
import vn.gpay.gsmart.core.stockout.StockOutPklist;
import vn.gpay.gsmart.core.stockout.StockoutReport;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.StockoutStatus;
import vn.gpay.gsmart.core.utils.StockoutTypes;
import vn.gpay.gsmart.core.utils.ProductType;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;

@RestController
@RequestMapping("/api/v1/stockout")
public class StockOutAPI {

	@Autowired private IStockOutService stockOutService;
	@Autowired private IWarehouseService warehouseService;
	@Autowired private IStockOutPklistService stockOutPklistService;
	@Autowired private IStockOutDService stockout_d_Service;
	@Autowired private IActionLogs_Service actionLogsRepository;
	@Autowired private ISKU_Service skuService;
	@Autowired private IPOrderBOMSKU_Service porderBOMSKUService;
	@Autowired private IPOrder_Product_SKU_Service porderProductSKUService;
	
	@RequestMapping(value = "/stockout_create",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutCreate(@RequestBody StockoutCreateRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			UUID uuid = UUID.randomUUID();
		    String stockoutcode = uuid.toString();
			if(entity.data !=  null) {
				StockOut stockout = entity.data;
				if(stockout.getId()==null || stockout.getId()==0) {
					stockout.setUsercreateid_link(user.getUserId());
					stockout.setOrgrootid_link(user.getRootorgid_link());
					stockout.setOrgid_from_link(user.getOrgId());
					stockout.setTimecreate(new Date());
					stockout.setStockoutcode(stockoutcode);
					stockout.setStatus(0);
					
			    }else {
			    	stockout.setOrgrootid_link(user.getRootorgid_link());
			    	stockout.setLastuserupdateid_link(user.getUserId());
			    	stockout.setLasttimeupdate(new Date());
			    }
				stockout.getStockoutd().forEach(stockoutd -> {
					stockoutd.setOrgrootid_link(user.getRootorgid_link());
					stockoutd.getStockoutpklist().forEach(pklist-> pklist.setOrgrootid_link(user.getRootorgid_link()));
		    	});
				
				stockOutService.create(stockout);
				
				List<StockOutD> stockoutd = stockout.getStockoutd();
				if(stockoutd !=null && stockoutd.size() >0) {
					for (StockOutD item : stockoutd) {
						List<StockOutPklist> Pklist = item.getStockoutpklist();
						if(Pklist !=null && Pklist.size() >0) {
							for (StockOutPklist entry : Pklist) {
								warehouseService.deleteByEpc(entry.getEpc(),user.getOrgId());
							}
							
						}
					}
				}
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockout_getone",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutGetone(@RequestBody StockoutGetoneRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = stockOutService.stockout_getone(user.getOrgId(),entity.stockoutcode, entity.stockcode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockout_list",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutList(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long orgid_from_link =null;
			if(!user.isOrgRoot()) {
				orgid_from_link =user.getOrgId();
			}
					
			response.data = stockOutService.stockout_list_test(user.getRootorgid_link(),entity.stockouttypeid_link,entity.stockoutcode, orgid_from_link,entity.orgid_to_link, entity.stockoutdate_from, entity.stockoutdate_to,entity.status);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/stockout_listbyorgto",method = RequestMethod.POST)
	public ResponseEntity<?> Stockout_listbyorgto(@RequestBody StockoutListRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			response.data = stockOutService.stockout_listByOrgTo(entity.stockouttypeid_link, entity.orgid_to_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/getbydate",method = RequestMethod.POST)
	public ResponseEntity<StockoutFilterResponse> Stockout_getbydate(@RequestBody StockoutFilterRequest entity, HttpServletRequest request) {
		StockoutFilterResponse response = new StockoutFilterResponse();
		try {
			response.data = recalStockout_Total(stockOutService.getByDate(entity.stockouttypeid, GPAYDateFormat.atStartOfDay(entity.stockoutdate_from), GPAYDateFormat.atEndOfDay(entity.stockoutdate_to)));
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			System.out.println(response.data.size());
			return new ResponseEntity<StockoutFilterResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutFilterResponse>(HttpStatus.OK);
		}    			
	}	
	@RequestMapping(value = "/stockout_activate",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutActivate(@RequestBody StockOutActivateRequest entity, HttpServletRequest request ) {
		StockoutResponse response = new StockoutResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = stockOutService.stockout_listByOrgTo(entity.stockouttypeid_link, user.getOrgId());
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/stockout_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> StockinGetByID(@RequestBody GetStockoutByIDRequest entity, HttpServletRequest request ) {
		StockoutByIDReponse response = new StockoutByIDReponse();
		try {
			response.data = stockOutService.findOne(entity.id);
			response.epcs = stockOutPklistService.inv_getbyid(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutByIDReponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockout_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutDeleteByID(@RequestBody GetStockoutByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			stockOutService.deleteById(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getstockoutdbydate",method = RequestMethod.POST)
	public ResponseEntity<StockoutDFilterResponse> getStockout_d_ByDate(@RequestBody StockoutDFilterRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockout_getbydate");
		
		StockoutDFilterResponse response = new StockoutDFilterResponse();
		try {
			if (entity.skucode.length() > 0){
				System.out.println("co skucode");
				response.data = stockout_d_Service.getByDateAndSkucode(entity.stockouttypeid, GPAYDateFormat.atStartOfDay(entity.stockoutdate_from), GPAYDateFormat.atEndOfDay(entity.stockoutdate_to), entity.skuid_link);
			}
			else {
				System.out.println(entity.stockouttypeid);
				response.data = stockout_d_Service.getByDateAndType(entity.stockouttypeid, GPAYDateFormat.atStartOfDay(entity.stockoutdate_from), GPAYDateFormat.atEndOfDay(entity.stockoutdate_to));
			}
			//response.data = stockoutRepository.getAll();
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			System.out.println(response.data.size());
			return new ResponseEntity<StockoutDFilterResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutDFilterResponse>(HttpStatus.OK);
		}    			
	}
	
	@RequestMapping(value = "/getstockoutd_bystockoutid",method = RequestMethod.POST)
	public ResponseEntity<StockoutDResponse> getStockoutd_bystockoutid(@RequestBody StockoutDRequest entity, HttpServletRequest request) {
		StockoutDResponse response = new StockoutDResponse();
		try {
			response.data = stockout_d_Service.getByStockoutId(entity.stockoutid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			System.out.println(response.data.size());
			return new ResponseEntity<StockoutDResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutDResponse>(response, HttpStatus.BAD_REQUEST);
		}    			
	}	
	//Xuất vải đi kiểm đo theo mã NPL - SKU
	@RequestMapping(value = "/createstockoutforcheck",method = RequestMethod.POST)
	public ResponseEntity<StockoutCreateResponse> createStockoutForCheck(@RequestBody StockoutCreateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockout_createforcheck");
		
		StockoutCreateResponse response = new StockoutCreateResponse();
		try {
			//1. Check if the skucode exists
			SKU theSku = skuService.getSKU_byCode(entity.data.getP_skucode(), orgrootid_link);
			if (null != theSku){
				//1. Check for ordercode available on Stockout table, if have return error
				//List<Stockout> stockoutChecklist = stockoutRepository.getBySkucode(StockoutTypes.STOCKOUT_TYPE_FORCHECK, entity.data.getP_skucode());
				List<StockOut> stockoutChecklist = stockOutService.getByDateAndSkuID(entity.data.getStockoutdate(), entity.data.getStockouttypeid_link(), theSku.getId());
				if (stockoutChecklist.size() > 0){
					System.out.println("Key duplication");
					response.setRespcode(ResponseMessage.KEY_RC_KEY_DUPLICATION);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_KEY_DUPLICATION));
					return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.BAD_REQUEST);
				} else {
				//2. Get material's list from IvyERP for the order
					StockOut stockoutNew = new StockOut();
					stockoutNew.setOrgrootid_link(user.getRootorgid_link());
					stockoutNew.setStockoutdate(entity.data.getStockoutdate());
					stockoutNew.setStockouttypeid_link(entity.data.getStockouttypeid_link());
					stockoutNew.setOrgid_from_link(user.getOrgid_link());
					stockoutNew.setOrgid_to_link(entity.data.getOrgid_to_link());
					stockoutNew.setPordercode(entity.data.getPordercode());
					stockoutNew.setShipperson(entity.data.getShipperson());
					stockoutNew.setShipperson(entity.data.getShipperson());
					stockoutNew.setTotalpackage(entity.data.getTotalpackage());
					stockoutNew.setTotalyds(entity.data.getTotalyds());
					stockoutNew.setTotalm3(entity.data.getTotalm3());
					stockoutNew.setTotalnetweight(entity.data.getTotalnetweight());
					stockoutNew.setTotalgrossweight(entity.data.getTotalgrossweight());
					stockoutNew.setP_skuid_link(theSku.getId());
					stockoutNew.setP_skucode(theSku.getCode());
					stockoutNew.setExtrainfo(entity.data.getExtrainfo());
					stockoutNew.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
					stockoutNew.setUsercreateid_link(user.getId());
					stockoutNew.setTimecreate(new Date());
					
					List<StockOutD> lsStockoutD = new ArrayList<StockOutD>();
		    	
					StockOutD theStockoutD = new StockOutD();
		    		theStockoutD.setStockoutdate(entity.data.getStockoutdate());
		    		theStockoutD.setSkuid_link(theSku.getId());
		    		theStockoutD.setUsercreateid_link(user.getId());
		    		theStockoutD.setTimecreate(new Date());
		    		theStockoutD.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
		    		
		    		lsStockoutD.add(theStockoutD);
					stockoutNew.setStockoutd(lsStockoutD);
				//3. Create new Stockout and StockoutD
					stockOutService.save(stockoutNew);
				//4. Return Stockout object to client
					response.data = stockoutNew;
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.OK);	
				}
			}
			else {
					response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
				    return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.BAD_REQUEST);
			}
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);

			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));		
		    return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/createstockoutforcheck_withsku",method = RequestMethod.POST)
	public ResponseEntity<StockoutCreateResponse> createStockoutForCheck_WithSku(@RequestBody StockoutCreateWithSkuRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockout_createforcheck_withsku");
		
		StockoutCreateResponse response = new StockoutCreateResponse();
		try {
			SKU theSku = entity.skudata;
			//1. Check for ordercode available on Stockout table, if have return error
			//List<Stockout> stockoutChecklist = stockoutRepository.getBySkucode(StockoutTypes.STOCKOUT_TYPE_FORCHECK, entity.data.getP_skucode());
			List<StockOut> stockoutChecklist = stockOutService.getByDateAndSkuID(entity.data.getStockoutdate(), entity.data.getStockouttypeid_link(), entity.data.getP_skuid_link());
			if (stockoutChecklist.size() > 0){
				response.setRespcode(ResponseMessage.KEY_RC_KEY_DUPLICATION);
				response.setMessage(ResponseMessage.MES_RC_KEY_DUPLICATION);
				return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.BAD_REQUEST);
			} else {
			//2. Get material's list from IvyERP for the order
				StockOut stockoutNew = new StockOut();
				stockoutNew.setOrgrootid_link(user.getRootorgid_link());
				stockoutNew.setStockoutdate(entity.data.getStockoutdate());
				stockoutNew.setStockouttypeid_link(entity.data.getStockouttypeid_link());
				stockoutNew.setOrgid_from_link(user.getOrgid_link());
				stockoutNew.setOrgid_to_link(entity.data.getOrgid_to_link());
				stockoutNew.setPordercode(entity.data.getPordercode());
				stockoutNew.setShipperson(entity.data.getShipperson());
				stockoutNew.setShipperson(entity.data.getShipperson());
				stockoutNew.setTotalpackage(entity.data.getTotalpackage());
				stockoutNew.setTotalyds(entity.data.getTotalyds());
				stockoutNew.setTotalm3(entity.data.getTotalm3());
				stockoutNew.setTotalnetweight(entity.data.getTotalnetweight());
				stockoutNew.setTotalgrossweight(entity.data.getTotalgrossweight());
				stockoutNew.setP_skuid_link(entity.data.getP_skuid_link());
				stockoutNew.setP_skucode(entity.data.getP_skucode());
				stockoutNew.setExtrainfo(entity.data.getExtrainfo());
				stockoutNew.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
				stockoutNew.setUsercreateid_link(user.getId());
				stockoutNew.setTimecreate(new Date());
				
				List<StockOutD> lsStockoutD = new ArrayList<StockOutD>();
	    	
				StockOutD theStockoutD = new StockOutD();
	    		theStockoutD.setStockoutdate(entity.data.getStockoutdate());
	    		theStockoutD.setSkuid_link(theSku.getId());
//	    		theStockoutD.setSkucode(entity.data.getP_skucode());
//	    		theStockoutD.setSkutypeid_link(theSku.getSkutypeid_link());
//	    		theStockoutD.setColor_code(theSku.getColor_code());
//	    		theStockoutD.setColor_name(theSku.getColor_name());
	    		theStockoutD.setUsercreateid_link(user.getId());
	    		theStockoutD.setTimecreate(new Date());
	    		theStockoutD.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
	    		
	    		lsStockoutD.add(theStockoutD);
				stockoutNew.setStockoutd(lsStockoutD);
			//3. Create new Stockout and StockoutD
				stockOutService.save(stockoutNew);
			//4. Return Stockout object to client
				response.data = stockoutNew;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.MES_RC_SUCCESS);
				return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.OK);	
			}
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutCreateResponse>(response, HttpStatus.BAD_REQUEST);
		}    			
	}	
	@RequestMapping(value = "/createstockouttocut",method = RequestMethod.POST)
	public ResponseEntity<StockoutCreateResponse> createStockoutToCut(@RequestBody StockoutCreateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockout_createforcheck");
		
		StockoutCreateResponse response = new StockoutCreateResponse();
		try {
			//1. Check for ordercode available on Stockout table, if have return error
			List<StockOut> stockoutChecklist = stockOutService.getByTypeAndOrderID(StockoutTypes.STOCKOUT_TYPE_FORCUT, entity.data.getPorderid_link());
			if (stockoutChecklist.size() > 0){
				response.setRespcode(ResponseMessage.KEY_RC_KEY_DUPLICATION);
				response.setMessage(ResponseMessage.MES_RC_KEY_DUPLICATION);
				return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.OK);
			} else {
			//2. Get material's list from IvyERP for the order
				StockOut stockoutNew = new StockOut();
				stockoutNew.setOrgrootid_link(user.getRootorgid_link());
				stockoutNew.setStockoutdate(new Date());
				stockoutNew.setStockouttypeid_link(StockoutTypes.STOCKOUT_TYPE_FORCUT);
				stockoutNew.setOrgid_from_link(user.getOrgid_link());
				stockoutNew.setOrgid_to_link(entity.data.getOrgid_to_link());
				stockoutNew.setPorderid_link(entity.data.getPorderid_link());
				stockoutNew.setPordercode(entity.data.getPordercode());
				stockoutNew.setShipperson(entity.data.getShipperson());
				stockoutNew.setShipperson(entity.data.getShipperson());
				stockoutNew.setTotalpackage(entity.data.getTotalpackage());
				stockoutNew.setTotalyds(entity.data.getTotalyds());
				stockoutNew.setTotalm3(entity.data.getTotalm3());
				stockoutNew.setTotalnetweight(entity.data.getTotalnetweight());
				stockoutNew.setTotalgrossweight(entity.data.getTotalgrossweight());
				stockoutNew.setP_skuid_link(entity.data.getP_skuid_link());
				stockoutNew.setExtrainfo(entity.data.getExtrainfo());
				stockoutNew.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
				stockoutNew.setUsercreateid_link(user.getId());
				stockoutNew.setTimecreate(new Date());
				
				List<POrderBOMSKU_By_Color> lsPOrderBOMSKU = porderBOMSKUService.getByPOrderID_GroupByColor(entity.data.getPorderid_link());
				for (POrderBOMSKU_By_Color porder_sku: lsPOrderBOMSKU){
					//Group by màu sản phẩm
					StockOutD theStockoutD = new StockOutD();
					theStockoutD.setPorderid_link(entity.data.getPorderid_link());
					
					List<POrderBOMSKU> lsSKU = porderBOMSKUService.getSKUByMaterial(entity.data.getPorderid_link(), porder_sku.getMaterialid_link());
					int totalSKU = 0;
					for(POrderBOMSKU skuBOM: lsSKU){
						List<POrder_Product_SKU> lsSKUOrder = porderProductSKUService.getby_porderandsku(entity.data.getPorderid_link(), skuBOM.getSkuid_link());
						if (lsSKUOrder.size() > 0){
							totalSKU += lsSKUOrder.get(0).getPquantity_total();
						}
					}
					theStockoutD.setTotalorder_tech(porder_sku.getAmount()*totalSKU);
					
					
		    		theStockoutD.setProductcolor_name(porder_sku.getProductcolor_name());
		    		theStockoutD.setSkuid_link(porder_sku.getMaterialid_link());
		    		theStockoutD.setStockoutdate(new Date());
		    		theStockoutD.setUsercreateid_link(user.getId());
		    		theStockoutD.setTimecreate(new Date());
		    		theStockoutD.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);	
		    		stockoutNew.getStockoutd().add(theStockoutD);
				}
			//3. Create new Stockout and StockoutD
				stockOutService.save(stockoutNew);
			//4. Return Stockout object to client
				response.data = stockoutNew;
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.MES_RC_SUCCESS);
				return new ResponseEntity<StockoutCreateResponse>(response,HttpStatus.OK);				
			}
		}catch (Exception e) {
			e.printStackTrace();
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutCreateResponse>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/getpklist",method = RequestMethod.POST)
	public ResponseEntity<StockoutPklistResponse> getPklist(@RequestBody StockoutPklistRequest entity, HttpServletRequest request) {
		StockoutPklistResponse response = new StockoutPklistResponse();
		try {
			response.data = stockOutPklistService.getByStockoutdId(entity.stockoutdid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			System.out.println(response.data.size());
			return new ResponseEntity<StockoutPklistResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutPklistResponse>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	@RequestMapping(value = "/createpklist",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> createPklist(@RequestBody StockoutPklistCreateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_create");
		
		ResponseBase response = new ResponseBase();
		try {
			//Create new Pklist
			StockOutPklist newPklist = new StockOutPklist();
			newPklist.setOrgrootid_link(user.getRootorgid_link());
			newPklist.setStockoutid_link(entity.data.getStockoutid_link());
			newPklist.setStockoutdid_link(entity.data.getStockoutdid_link());
			newPklist.setSkuid_link(entity.data.getSkuid_link());
			newPklist.setLotnumber(entity.data.getLotnumber());
			newPklist.setPackageid(entity.data.getPackageid());
			newPklist.setYdsorigin(entity.data.getYdsorigin());
			newPklist.setYdscheck(entity.data.getYdscheck());
			newPklist.setYdsprocessed(entity.data.getYdsprocessed());
			newPklist.setWidthorigin(entity.data.getWidthorigin());
			newPklist.setWidthcheck(entity.data.getWidthcheck());
			newPklist.setWidthprocessed(entity.data.getWidthprocessed());
			newPklist.setTotalerror(entity.data.getTotalerror());
			newPklist.setNetweight(entity.data.getNetweight());
			newPklist.setGrossweight(entity.data.getGrossweight());
			
			if (null != entity.data.getEpc() && entity.data.getEpc().length() > 0)
				newPklist.setEpc(entity.data.getEpc());
			else {
				newPklist.setEpc(UUID.randomUUID().toString());
			}
			
			newPklist.setRssi(entity.data.getRssi());
			newPklist.setStatus(0);
			newPklist.setEncryptdatetime(entity.data.getEncryptdatetime());
			newPklist.setUsercheckid_link(entity.data.getUsercheckid_link());
			newPklist.setTimecheck(entity.data.getTimecheck());
			newPklist.setUserprocessedkid_link(entity.data.getUserprocessedkid_link());
			newPklist.setTimeprocessed(entity.data.getTimeprocessed());
			newPklist.setUsercreateid_link(user.getId());
			newPklist.setTimecreate(new Date());
			newPklist.setLastuserupdateid_link(user.getId());
			newPklist.setLasttimeupdate(new Date());
			
			stockOutPklistService.save(newPklist);
			
			//Update Total Value on Stockout_d
			recalStockout_d_Total(entity.data.getStockoutdid_link());
			
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			//System.out.println(response.data.size());
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	@RequestMapping(value = "/updatepklist",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updatePklist(@RequestBody StockoutPklistUpdateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_update");
		
		ResponseBase response = new ResponseBase();
		try {
			//Update Pklist
	    	StockOutPklist updatePklist = stockOutPklistService.findOne(entity.data.getId());
	    	if (null != updatePklist){
		    	if (entity.data.getYdsorigin() != entity.data.getYdsoriginold())
		    		updatePklist.setYdsorigin(entity.data.getYdsorigin());			
		    	if (entity.data.getYdscheck() != entity.data.getYdscheckold())
		    		updatePklist.setYdscheck(entity.data.getYdscheck());
		    	if (entity.data.getYdsprocessed() != entity.data.getYdsprocessedold()) 
		    		updatePklist.setYdsprocessed(entity.data.getYdsprocessed());
		    	if (entity.data.getWidthorigin() != entity.data.getWidthoriginold()) 
		    		updatePklist.setWidthorigin(entity.data.getWidthorigin());
		    	if (entity.data.getWidthcheck() != entity.data.getWidthcheckold()) 
		    		updatePklist.setWidthcheck(entity.data.getWidthcheck());
		    	if (entity.data.getWidthprocessed() != entity.data.getWidthprocessedold())
		    		updatePklist.setWidthprocessed(entity.data.getWidthprocessed());
		    	if (entity.data.getTotalerror() != entity.data.getTotalerrorold())
		    		updatePklist.setTotalerror(entity.data.getTotalerror());
		    	updatePklist.setExtrainfo(entity.data.getExtrainfo());
		    	updatePklist.setEpc(entity.data.getEpc());
		    	updatePklist.setRssi(entity.data.getRssi());
		    	updatePklist.setEncryptdatetime(entity.data.getEncryptdatetime());
		    	updatePklist.setUsercheckid_link(entity.data.getUsercheckid_link());
		    	updatePklist.setTimecheck(entity.data.getTimecheck());
		    	updatePklist.setUserprocessedkid_link(entity.data.getUserprocessedkid_link());
		    	updatePklist.setTimeprocessed(entity.data.getTimeprocessed());
		    	updatePklist.setLastuserupdateid_link(user.getId());
		    	updatePklist.setLasttimeupdate(new Date());
				
		    	stockOutPklistService.save(updatePklist);
				recalStockout_d_Total(entity.data.getStockoutdid_link());
				
				actionLogs.setResponse_time(new Date());
				actionLogs.setResponse_status(0);
				actionLogs.setResponse_msg("OK");
				actionLogsRepository.save(actionLogs);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				//System.out.println(response.data.size());
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
	    	} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));		
			    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);
	    	}
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/deletepklist",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> deletePklist(@RequestBody StockoutPklistDeleteRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_delete");
		
		ResponseBase response = new ResponseBase();
		try {
	    	StockOutPklist stockoutpklist = stockOutPklistService.findOne(entity.stockoutpklistid);
	    	if (null!=stockoutpklist){
		    	stockOutPklistService.delete(stockoutpklist);
				//response.data = stockoutpklistRepository.getAll();
				actionLogs.setResponse_time(new Date());
				actionLogs.setResponse_status(0);
				actionLogs.setResponse_msg("OK");
				actionLogsRepository.save(actionLogs);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
	    	} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));		
			    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);
	    	}
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}	
	@RequestMapping(value = "/deletestockoutforcheck",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> deleteStockoutForCheck(@RequestBody StockoutDeleteRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockout_delete");
		
		ResponseBase response = new ResponseBase();
		try {
	    	StockOut stockout = stockOutService.findOne(entity.stockoutid);

	    	//If Stockout type = STOCKOUT_TYPE_FORCHECK
	    	if (stockout.getStockouttypeid_link() == StockoutTypes.STOCKOUT_TYPE_FORCHECK || stockout.getStockouttypeid_link() == StockoutTypes.STOCKOUT_TYPE_FORPROCESS){
	    		//Check if having Pklist that Stockout? --> return error
	    		if (stockOutPklistService.getByStockoutIdAndStatus(entity.stockoutid, 1).size() > 0){
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(-1);
	    			actionLogs.setResponse_msg("Đã có hàng xuất kho, không thể xóa");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
	    			response.setMessage("Đã có hàng xuất kho, không thể xóa");			
	    		    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);	    			
	    		}
	    		else {
	    			//Delete Pklist
	    			for (StockOutPklist thePklist: stockOutPklistService.getByStockoutId(entity.stockoutid)){
	    				stockOutPklistService.delete(thePklist);
	    			}
	    			//Delete StockoutD
	    			for (StockOutD theStockoutD: stockout_d_Service.getByStockoutId(entity.stockoutid)){
	    				stockout_d_Service.delete(theStockoutD);
	    			}
	    			stockOutService.delete(stockout);
	    			//response.data = stockoutpklistRepository.getAll();
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(0);
	    			actionLogs.setResponse_msg("OK");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
	    			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
	    			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	    			
	    		}
	    	}
	    	else
	    	//If Stockout type = STOCKOUT_TYPE_FORCUT
	    	if (stockout.getStockouttypeid_link() == StockoutTypes.STOCKOUT_TYPE_FORCUT){
	    		if (stockout.getStatus() == StockoutStatus.STOCKOUT_STATUS_APPROVED){
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(-1);
	    			actionLogs.setResponse_msg("Đã đồng bộ Lệnh sx, không thể xóa");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
	    			response.setMessage("Đã đồng bộ Lệnh sx, không thể xóa");			
	    		    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);	 
	    		} else {
	    			//Delete Pklist
	    			for (StockOutPklist thePklist: stockOutPklistService.getByStockoutId(entity.stockoutid)){
	    				//Update Pklist forcut status to 0
	    				List<StockOutPklist> lsPklistForCheck = stockOutPklistService.getStockoutedByEpc(thePklist.getSkuid_link(), thePklist.getEpc());
	    				if (lsPklistForCheck.size() > 0){
	    					StockOutPklist thePklistForCheck = lsPklistForCheck.get(0);
	    					thePklistForCheck.setStatus(0);
	    					stockOutPklistService.save(thePklistForCheck);
	    				}
	    					
	    				stockOutPklistService.delete(thePklist);
	    			}
	    			//Delete StockoutD
	    			for (StockOutD theStockoutD: stockout_d_Service.getByStockoutId(entity.stockoutid)){
	    				stockout_d_Service.delete(theStockoutD);
	    			}
	    			stockOutService.delete(stockout);
	    			//response.data = stockoutpklistRepository.getAll();
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(0);
	    			actionLogs.setResponse_msg("OK");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
	    			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
	    			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);		    			
	    		}
	    	}
	    	else {
    			actionLogs.setResponse_time(new Date());
    			actionLogs.setResponse_status(-1);
    			actionLogs.setResponse_msg("Lệnh không hợp lệ, không thể xóa");
    			actionLogsRepository.save(actionLogs);
    			
    			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
    			response.setMessage("Lệnh không hợp lệ, không thể xóa");			
    		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
	    	}

		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}  	
	
	@RequestMapping(value = "/reportskuprocesed",method = RequestMethod.POST)
	public ResponseEntity<StockoutForCheckReportResponse> reportSkuProcesed(@RequestBody StockoutFilterRequest entity, HttpServletRequest request) {
		
		StockoutForCheckReportResponse response = new StockoutForCheckReportResponse();
		try {
			List<StockoutReport> theReportList = new ArrayList<StockoutReport>();
			List<StockOutPklist> processedList = new ArrayList<StockOutPklist>();
			List<StockOutPklist> checkList = new ArrayList<StockOutPklist>();
			if (entity.stockouttypeid == 1 || entity.stockouttypeid == -1)
				processedList = stockOutPklistService.getDetailStockoutForProcess(GPAYDateFormat.atStartOfDay(entity.stockoutdate_from), GPAYDateFormat.atEndOfDay(entity.stockoutdate_to));
			if (entity.stockouttypeid == 0 || entity.stockouttypeid == -1)
				checkList = stockOutPklistService.getDetailStockoutForCheck(GPAYDateFormat.atStartOfDay(entity.stockoutdate_from), GPAYDateFormat.atEndOfDay(entity.stockoutdate_to));
			
			//Duyet qua cac cay vai da khu co
			for (StockOutPklist row : processedList) {
				StockoutReport r_processed = theReportList.stream().filter(skucode -> skucode.getSkucode().equals(row.getSkucode())).findAny().orElse(null);
				if (null == r_processed){
					StockoutReport r_entity = new StockoutReport();
					r_entity.setSkucode(row.getSkucode());
					r_entity.setSkutypeid_link(row.getProducttypeid_link());
					r_entity.setSkutype(row.getProducttype_name());
					r_entity.setColor_name(row.getMauSanPham());
					r_entity.setYdsorigin(null);
					if (null !=row.getYdscheck())
						r_entity.setYdscheck(Double.valueOf(row.getYdscheck().toString()));
					r_entity.setYdsprocessed(Double.valueOf(row.getYdsprocessed().toString()));
					if (null !=row.getTotalerror())
						r_entity.setTotalerror(Double.valueOf(row.getTotalerror().toString()));
					
					//set Ydsorigin == null de so sanh ben duoi
					row.setYdsorigin(null);
					r_entity.getProcessedpklist().add(row);
					r_entity.setPackageprocessed((null==r_entity.getPackageprocessed()?0:r_entity.getPackageprocessed()) + 1);
					theReportList.add(r_entity);
				} else {
					r_processed.setYdscheck((null==r_processed.getYdscheck()?0:r_processed.getYdscheck()) + (null==row.getYdscheck()?0:row.getYdscheck()));
					r_processed.setYdsprocessed((null==r_processed.getYdsprocessed()?0:r_processed.getYdsprocessed()) + (null==row.getYdsprocessed()?0:row.getYdsprocessed()));
					r_processed.setTotalerror((null==r_processed.getTotalerror()?0:r_processed.getTotalerror()) + (null==row.getTotalerror()?0:row.getTotalerror()));
					
					//set Ydsorigin == null de so sanh ben duoi
					row.setYdsorigin(null);
					r_processed.getProcessedpklist().add(row);
					r_processed.setPackageprocessed((null==r_processed.getPackageprocessed()?0:r_processed.getPackageprocessed()) + 1);
				}
			}
			
			for (StockOutPklist row : checkList) {
				StockoutReport r_processed = theReportList.stream().filter(skucode -> skucode.getSkucode().equals(row.getSkucode())).findAny().orElse(null);
				
				//Neu tim thay thi Update, khong thi bo qua (chi thong ke cac cay vai da khu co)
				if (null != r_processed){
					List<StockOutPklist> lst_pklistprocessed = r_processed.getProcessedpklist().stream().filter(pklist -> pklist.getWidthcheck().equals(row.getWidthcheck()) && pklist.getYdscheck().equals(row.getYdscheck())).collect(Collectors.toList());
					if (lst_pklistprocessed.size() > 0){
						//Neu co cay vai khu co trung Ydscheck --> Update so origin
						for (StockOutPklist r_pklistprocessed:lst_pklistprocessed){
							if (null == r_pklistprocessed.getYdsorigin()){
								r_pklistprocessed.setWidthorigin(row.getWidthorigin());
								r_pklistprocessed.setYdsorigin(row.getYdsorigin());
								r_processed.setYdsorigin((null==r_processed.getYdsorigin()?0:r_processed.getYdsorigin()) + row.getYdsorigin());
								
								row.setStatus(1);
								r_processed.getCheckpklist().add(row);
								r_processed.setPackagecheck((null==r_processed.getPackagecheck()?0:r_processed.getPackagecheck()) + 1);
								break;
							}
						}
					} else {
						//Neu khong co cay vai khu co trung Yds --> Them vao de xem xet sau
						row.setStatus(0);
						r_processed.getCheckpklist().add(row);
						r_processed.setPackagecheck((null==r_processed.getPackagecheck()?0:r_processed.getPackagecheck()) + 1);
						
						//Neu chi chon bao cao kiem vai thi moi thuc hien
						if (entity.stockouttypeid == 0){
							r_processed.setYdsorigin((null==r_processed.getYdsorigin()?0:r_processed.getYdsorigin()) + (null==row.getYdsorigin()?0:row.getYdsorigin()));
							r_processed.setYdscheck((null==r_processed.getYdscheck()?0:r_processed.getYdscheck()) + (null==row.getYdscheck()?0:row.getYdscheck()));
						}
					}
				} else {
					//Neu chi chon bao cao kiem vai thi moi thuc hien
					if (entity.stockouttypeid == 0){
						StockoutReport r_entity = new StockoutReport();
						r_entity.setSkucode(row.getSkucode());
						r_entity.setSkutypeid_link(row.getProducttypeid_link());
						r_entity.setSkutype(row.getProducttype_name());
						r_entity.setColor_name(row.getMauSanPham());
						r_entity.setYdsorigin(Double.valueOf(row.getYdsorigin().toString()));
						r_entity.setYdscheck(Double.valueOf(row.getYdscheck().toString()));
						
						row.setStatus(0);
						r_entity.getCheckpklist().add(row);
						r_entity.setPackagecheck((null==r_entity.getPackagecheck()?0:r_entity.getPackagecheck()) + 1);
						theReportList.add(r_entity);						
					}
				}
			}
			
			response.data = theReportList;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutForCheckReportResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
	
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutForCheckReportResponse>(HttpStatus.BAD_REQUEST);
		}    			
	}	
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> deleteByID(@RequestBody StockoutDeleteRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockout_delete");
		
		ResponseBase response = new ResponseBase();
		try {
	    	StockOut stockout = stockOutService.findOne(entity.stockoutid);

	    	//If Stockout type = STOCKOUT_TYPE_FORCHECK
	    	if (stockout.getStockouttypeid_link() == StockoutTypes.STOCKOUT_TYPE_FORCHECK){
	    		//Check if having Pklist that Stockout? --> return error
	    		if (stockOutPklistService.getByStockoutIdAndStatus(entity.stockoutid, 1).size() > 0){
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(-1);
	    			actionLogs.setResponse_msg("Đã có hàng xuất kho, không thể xóa");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
	    			response.setMessage("Đã có hàng xuất kho, không thể xóa");			
	    		    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);	    			
	    		}
	    		else {
	    			//Delete Pklist
	    			for (StockOutPklist thePklist: stockOutPklistService.getByStockoutId(entity.stockoutid)){
	    				stockOutPklistService.delete(thePklist);
	    			}
	    			//Delete StockoutD
	    			for (StockOutD theStockoutD: stockout_d_Service.getByStockoutId(entity.stockoutid)){
	    				stockout_d_Service.delete(theStockoutD);
	    			}
	    			stockOutService.delete(stockout);
	    			//response.data = stockoutpklistRepository.getAll();
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(0);
	    			actionLogs.setResponse_msg("OK");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
	    			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
	    			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);	    			
	    		}
	    	}
	    	else
	    	//If Stockout type = STOCKOUT_TYPE_FORCUT
	    	if (stockout.getStockouttypeid_link() == StockoutTypes.STOCKOUT_TYPE_FORCUT){
	    		if (stockout.getStatus() == StockoutStatus.STOCKOUT_STATUS_APPROVED){
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(-1);
	    			actionLogs.setResponse_msg("Đã đồng bộ Lệnh sx, không thể xóa");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
	    			response.setMessage("Đã đồng bộ Lệnh sx, không thể xóa");			
	    		    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);	 
	    		} else {
	    			//Delete Pklist
	    			for (StockOutPklist thePklist: stockOutPklistService.getByStockoutId(entity.stockoutid)){
	    				//Update Pklist forcut status to 0
	    				List<StockOutPklist> lsPklistForCheck = stockOutPklistService.getStockoutedByEpc(thePklist.getSkuid_link(), thePklist.getEpc());
	    				if (lsPklistForCheck.size() > 0){
	    					StockOutPklist thePklistForCheck = lsPklistForCheck.get(0);
	    					thePklistForCheck.setStatus(0);
	    					stockOutPklistService.save(thePklistForCheck);
	    				}
	    					
	    				stockOutPklistService.delete(thePklist);
	    			}
	    			//Delete StockoutD
	    			for (StockOutD theStockoutD: stockout_d_Service.getByStockoutId(entity.stockoutid)){
	    				stockout_d_Service.delete(theStockoutD);
	    			}
	    			stockOutService.delete(stockout);
	    			//response.data = stockoutpklistRepository.getAll();
	    			actionLogs.setResponse_time(new Date());
	    			actionLogs.setResponse_status(0);
	    			actionLogs.setResponse_msg("OK");
	    			actionLogsRepository.save(actionLogs);
	    			
	    			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
	    			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
	    			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);		    			
	    		}
	    	}
	    	else {
    			actionLogs.setResponse_time(new Date());
    			actionLogs.setResponse_status(-1);
    			actionLogs.setResponse_msg("Lệnh không hợp lệ, không thể xóa");
    			actionLogsRepository.save(actionLogs);
    			
    			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
    			response.setMessage("Lệnh không hợp lệ, không thể xóa");			
    		    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);
	    	}

		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(HttpStatus.BAD_REQUEST);
		}    			
	} 
	
	@RequestMapping(value = "/pklist_getavailablebysku",method = RequestMethod.POST)
	public ResponseEntity<StockoutPklistResponse> pklist_getAvailableBySku(@RequestBody StockoutPklistAvailableRequest entity, HttpServletRequest request) {

		StockoutPklistResponse response = new StockoutPklistResponse();
		try {
			response.data = stockOutPklistService.getAvailableBySku(entity.sku_id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockoutPklistResponse>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<StockoutPklistResponse>(response,HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/pklist_stockoutbatch",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> pklist_stockoutBatch(@RequestBody StockoutPklistCreateBatchRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_createbatch");
		
		ResponseBase response = new ResponseBase();
		try {
			for(StockOutPklist dataPklist: entity.data){
				//Update StockoutPklist for Check status to 1
//				List<StockoutPklist> pklistForCheck = stockoutpklistRepository.getAvailableByEpc(dataPklist.getSkucode(), dataPklist.getEpc());
				List<StockOutPklist> pklistForCheck = stockOutPklistService.getAvailableByEpc(dataPklist.getEpc());
				if (pklistForCheck.size() > 0){
					//Đánh dấu cuộn vải đã được đưa vào danh sách xuất kho
					StockOutPklist pklistForCheckItem = pklistForCheck.get(0);
					pklistForCheckItem.setStatus(StockoutStatus.STOCKOUT_STATUS_APPROVED);
					stockOutPklistService.save(pklistForCheckItem);
					
					//Update StockoutD Status of Pklist
			    	StockOutD stockoutD = stockout_d_Service.findOne(pklistForCheckItem.getStockoutdid_link());
			    	stockoutD.setStatus(StockoutStatus.STOCKOUT_STATUS_APPROVED);
			    	stockout_d_Service.save(stockoutD);
			    	
					//Create new Pklist
					StockOutPklist newPklist = new StockOutPklist();
					newPklist.setOrgrootid_link(user.getRootorgid_link());
					newPklist.setStockoutid_link(dataPklist.getStockoutid_link());
					newPklist.setStockoutdid_link(dataPklist.getStockoutdid_link());
					newPklist.setSkuid_link(pklistForCheckItem.getSkuid_link());
					newPklist.setLotnumber(pklistForCheckItem.getLotnumber());
					newPklist.setPackageid(pklistForCheckItem.getPackageid());
					newPklist.setYdsorigin(pklistForCheckItem.getYdsorigin());
					newPklist.setYdscheck(pklistForCheckItem.getYdscheck());
					newPklist.setYdsprocessed(pklistForCheckItem.getYdsprocessed());
					newPklist.setWidthorigin(pklistForCheckItem.getWidthorigin());
					newPklist.setWidthcheck(pklistForCheckItem.getWidthcheck());
					newPklist.setWidthprocessed(pklistForCheckItem.getWidthprocessed());
					newPklist.setTotalerror(pklistForCheckItem.getTotalerror());
					newPklist.setNetweight(pklistForCheckItem.getNetweight());
					newPklist.setGrossweight(pklistForCheckItem.getGrossweight());
					
					if (null != pklistForCheckItem.getEpc() && pklistForCheckItem.getEpc().length() > 0)
						newPklist.setEpc(pklistForCheckItem.getEpc());
					else {
						newPklist.setEpc(UUID.randomUUID().toString());
					}
					
					newPklist.setRssi(dataPklist.getRssi());
					newPklist.setStatus(0);
					newPklist.setEncryptdatetime(pklistForCheckItem.getEncryptdatetime());
					newPklist.setUsercheckid_link(pklistForCheckItem.getUsercheckid_link());
					newPklist.setTimecheck(pklistForCheckItem.getTimecheck());
					newPklist.setUserprocessedkid_link(pklistForCheckItem.getUserprocessedkid_link());
					newPklist.setTimeprocessed(pklistForCheckItem.getTimeprocessed());
					newPklist.setUsercreateid_link(user.getId());
					newPklist.setTimecreate(new Date());
					newPklist.setLastuserupdateid_link(user.getId());
					newPklist.setLasttimeupdate(new Date());
					
					stockOutPklistService.save(newPklist);
					
					//Update Total Value on Stockout_d
					recalStockout_d_Total(dataPklist.getStockoutdid_link());
				}				

			}
			
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			//System.out.println(response.data.size());
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/pklist_stockoutreversebatch",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> pklist_stockoutreverseBatch(@RequestBody StockoutPklistCreateBatchRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_reversebatch");
		
		ResponseBase response = new ResponseBase();
		try {
			for(StockOutPklist dataPklist: entity.data){
				//Update StockoutPklist for Check status to 1
				List<StockOutPklist> pklistForCheck = stockOutPklistService.getStockoutedByEpc(dataPklist.getSkuid_link(), dataPklist.getEpc());
				if (pklistForCheck.size() > 0){
					StockOutPklist pklistForCheckItem = pklistForCheck.get(0);
					pklistForCheckItem.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
					stockOutPklistService.save(pklistForCheckItem);
					
					//Update StockoutD Status of Pklist
			    	StockOutD stockoutD = stockout_d_Service.findOne(pklistForCheckItem.getStockoutdid_link());
			    	boolean isAllPklistFree = true;
			    	for (StockOutPklist thePklist:stockOutPklistService.getByStockoutdId(stockoutD.getId())){
			    		if (thePklist.getStatus() == StockoutStatus.STOCKOUT_STATUS_APPROVED) isAllPklistFree = false;
			    		
			    	}
			    	if (isAllPklistFree)
			    		stockoutD.setStatus(StockoutStatus.STOCKOUT_STATUS_NEW);
			    	else
			    		stockoutD.setStatus(StockoutStatus.STOCKOUT_STATUS_APPROVED);
			    	stockout_d_Service.save(stockoutD);
					
					//Delete PKList
					
			    	stockOutPklistService.delete(stockOutPklistService.findOne(dataPklist.getId()));
					
					//Update Total Value on Stockout_d
					recalStockout_d_Total(dataPklist.getStockoutdid_link());
				}				

			}
			
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			//System.out.println(response.data.size());
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	
	@RequestMapping(value = "/pklist_update",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> pklist_Update(@RequestBody StockoutPklistUpdateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_update");
		
		ResponseBase response = new ResponseBase();
		try {
			//Update Pklist
	    	StockOutPklist updatePklist = stockOutPklistService.findOne(entity.data.getId());
	    	if (entity.data.getYdsorigin() != entity.data.getYdsoriginold())
	    		updatePklist.setYdsorigin(entity.data.getYdsorigin());			
	    	if (entity.data.getYdscheck() != entity.data.getYdscheckold())
	    		updatePklist.setYdscheck(entity.data.getYdscheck());
	    	if (entity.data.getYdsprocessed() != entity.data.getYdsprocessedold()) 
	    		updatePklist.setYdsprocessed(entity.data.getYdsprocessed());
	    	if (entity.data.getWidthorigin() != entity.data.getWidthoriginold()) 
	    		updatePklist.setWidthorigin(entity.data.getWidthorigin());
	    	if (entity.data.getWidthcheck() != entity.data.getWidthcheckold()) 
	    		updatePklist.setWidthcheck(entity.data.getWidthcheck());
	    	if (entity.data.getWidthprocessed() != entity.data.getWidthprocessedold())
	    		updatePklist.setWidthprocessed(entity.data.getWidthprocessed());
	    	if (entity.data.getTotalerror() != entity.data.getTotalerrorold())
	    		updatePklist.setTotalerror(entity.data.getTotalerror());
	    	updatePklist.setExtrainfo(entity.data.getExtrainfo());
	    	updatePklist.setEpc(entity.data.getEpc());
	    	updatePklist.setRssi(entity.data.getRssi());
	    	updatePklist.setEncryptdatetime(entity.data.getEncryptdatetime());
	    	updatePklist.setUsercheckid_link(entity.data.getUsercheckid_link());
	    	updatePklist.setTimecheck(entity.data.getTimecheck());
	    	updatePklist.setUserprocessedkid_link(entity.data.getUserprocessedkid_link());
	    	updatePklist.setTimeprocessed(entity.data.getTimeprocessed());
	    	updatePklist.setLastuserupdateid_link(user.getId());
	    	updatePklist.setLasttimeupdate(new Date());
			
	    	stockOutPklistService.save(updatePklist);
			recalStockout_d_Total(entity.data.getStockoutdid_link());
			
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(0);
			actionLogs.setResponse_msg("OK");
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			//System.out.println(response.data.size());
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			actionLogs.setResponse_time(new Date());
			actionLogs.setResponse_status(-1);
			actionLogs.setResponse_msg(e.getMessage());
			actionLogsRepository.save(actionLogs);
			
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());			
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}
	
	//Stockout Mex only, Mex will not be checked and processed, just stockout
	@RequestMapping(value = "/pklist_stockoutmex",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> pklist_StockoutMex(@RequestBody StockoutPklistCreateRequest entity, HttpServletRequest request) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String requestAddr = request.getRemoteAddr();
		ActionLogs actionLogs = new ActionLogs();
		actionLogs.setOrgrootid_link(user.getRootorgid_link());
		actionLogs.setOrgid_link(user.getOrgid_link());
		actionLogs.setUserid_link(user.getId());
		actionLogs.setAction_time(new Date());
		actionLogs.setAction_ip(requestAddr);
		actionLogs.setAction_task("stockoutpklist_mex");
		
		ResponseBase response = new ResponseBase();
		try {
			//Create new Pklist
			StockOutPklist tocutPklist = new StockOutPklist();
			tocutPklist.setOrgrootid_link(user.getRootorgid_link());
			tocutPklist.setStockoutid_link(entity.data.getStockoutid_link());
			tocutPklist.setStockoutdid_link(entity.data.getStockoutdid_link());
			tocutPklist.setSkuid_link(entity.data.getSkuid_link());
			tocutPklist.setLotnumber(entity.data.getLotnumber());
			tocutPklist.setPackageid(entity.data.getPackageid());
			tocutPklist.setYdsorigin(entity.data.getYdsorigin());
			tocutPklist.setYdscheck(entity.data.getYdscheck());
			tocutPklist.setYdsprocessed(entity.data.getYdsprocessed());
			tocutPklist.setWidthorigin(entity.data.getWidthorigin());
			tocutPklist.setWidthcheck(entity.data.getWidthcheck());
			tocutPklist.setWidthprocessed(entity.data.getWidthprocessed());
			tocutPklist.setTotalerror(entity.data.getTotalerror());
			tocutPklist.setNetweight(entity.data.getNetweight());
			tocutPklist.setGrossweight(entity.data.getGrossweight());
			
			if (null != entity.data.getEpc() && entity.data.getEpc().length() > 0)
				tocutPklist.setEpc(entity.data.getEpc());
			else {
				tocutPklist.setEpc(UUID.randomUUID().toString());
			}
			
			tocutPklist.setRssi(entity.data.getRssi());
			tocutPklist.setStatus(0);
			tocutPklist.setEncryptdatetime(entity.data.getEncryptdatetime());
			tocutPklist.setUsercheckid_link(entity.data.getUsercheckid_link());
			tocutPklist.setTimecheck(entity.data.getTimecheck());
			tocutPklist.setUserprocessedkid_link(entity.data.getUserprocessedkid_link());
			tocutPklist.setTimeprocessed(entity.data.getTimeprocessed());
			tocutPklist.setUsercreateid_link(user.getId());
			tocutPklist.setTimecreate(new Date());
			tocutPklist.setLastuserupdateid_link(user.getId());
			tocutPklist.setLasttimeupdate(new Date());

			stockOutPklistService.save(tocutPklist);
			
			//Update Total Value on Stockout_d
			recalStockout_d_Total(tocutPklist.getStockoutdid_link());
			
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
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}    			
	}		
	private void recalStockout_d_Total(Long stockoutdid_link){
		try {
			List<StockOutPklist> lsPklist = stockOutPklistService.getByStockoutdId(stockoutdid_link);
			int totalpackage = 0;
			String listpackage = "";
			float totalyds = 0;
			int totalpackagecheck = 0;
			float totalydscheck = 0;
			int totalpackageprocessed = 0;
			float totalydsprocessed = 0;
			float totalerror = 0;
			for(StockOutPklist pklist: lsPklist){
				totalpackage = totalpackage + 1;
				
				//If processed --> Get Ydsprocess to Listpackage. If not getYdscheck else getYdsorigin
				if ((null != pklist.getYdsprocessed()) && (pklist.getYdsprocessed() > 0)){
					if (listpackage == "") 
						listpackage = pklist.getYdsprocessed().toString();
					else
						listpackage = listpackage + " + " + pklist.getYdsprocessed().toString();
				} 
				else if ((null != pklist.getYdscheck()) && (pklist.getYdscheck() > 0)){
					if (listpackage == "") 
						listpackage = pklist.getYdscheck().toString();
					else
						listpackage = listpackage + " + " + pklist.getYdscheck().toString();
				} else {
					if (listpackage == "") 
						listpackage = pklist.getYdsorigin().toString();
					else
						listpackage = listpackage + " + " + pklist.getYdsorigin().toString();
				}
				
				totalyds = totalyds + (null ==pklist.getYdsorigin()?0:pklist.getYdsorigin());
				if ((null != pklist.getYdscheck()) && (pklist.getYdscheck() > 0)){
					totalpackagecheck = totalpackagecheck + 1;
					totalydscheck = totalydscheck + pklist.getYdscheck();
				}
				if ((null != pklist.getYdsprocessed()) && (pklist.getYdsprocessed() > 0)){
					totalpackageprocessed = totalpackageprocessed + 1;
					totalydsprocessed = totalydsprocessed + pklist.getYdsprocessed();
				}
				if ((null != pklist.getTotalerror()) && (pklist.getTotalerror() > 0)){
					totalerror = totalerror + pklist.getTotalerror();
				}
			}
			
			//Update Stockout_d
	    	StockOutD updateStockoutD = stockout_d_Service.findOne(stockoutdid_link);
	    	if (null != updateStockoutD){
	    		if (listpackage.length() > 0)
	    			listpackage = updateStockoutD.getSkucode() + ": " + listpackage + " = " + Float.toString(totalydsprocessed) + "m" + (totalerror > 0?" (Lỗi: " + Float.toString(totalerror) + "m)":"");
	    		
	    		updateStockoutD.setTotalpackage(totalpackage);
	    		updateStockoutD.setListpackage(listpackage);
	    		updateStockoutD.setTotalyds(totalyds);
	    		updateStockoutD.setTotalpackagecheck(totalpackagecheck);
	    		updateStockoutD.setTotalydscheck(totalydscheck);
	    		updateStockoutD.setTotalpackageprocessed(totalpackageprocessed);
	    		updateStockoutD.setTotalydsprocessed(totalydsprocessed);
	    		updateStockoutD.setTotalerror(totalerror);
	    		stockout_d_Service.save(updateStockoutD);
	    	}
			return;
		} catch(Exception e){
			e.printStackTrace();
			return;
		}
	}
	private List<StockOut> recalStockout_Total(List<StockOut> lstStockout){
		for(StockOut theStockout:lstStockout){
			try {
				Double t_MainMaterial = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_MAINMATERIAL)).mapToDouble(o -> null==o.getTotalorder_tech()?0:o.getTotalorder_tech()).sum();
				Double t_MainMaterial_stockout = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_MAINMATERIAL)).mapToDouble(o -> null==o.getTotalydsprocessed()?0:o.getTotalydsprocessed()).sum();
				theStockout.setTotal_mainmaterial(t_MainMaterial==null?0:t_MainMaterial);
				theStockout.setTotal_mainmaterial_stockout(t_MainMaterial_stockout==null?0:t_MainMaterial_stockout);
				
				Double t_LiningMaterial = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_LININGMATERIAL)).mapToDouble(o -> null==o.getTotalorder_tech()?0:o.getTotalorder_tech()).sum();
				Double t_LiningMaterial_stockout = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_LININGMATERIAL)).mapToDouble(o -> null==o.getTotalydsprocessed()?0:o.getTotalydsprocessed()).sum();
				theStockout.setTotal_liningmaterial(t_LiningMaterial==null?0:t_LiningMaterial);
				theStockout.setTotal_liningmaterial_stockout(t_LiningMaterial_stockout==null?0:t_LiningMaterial_stockout);
				
				Double t_Mex = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_MEX)).mapToDouble(o -> null==o.getTotalorder_tech()?0:o.getTotalorder_tech()).sum();
				Double t_Mex_stockout = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_MEX)).mapToDouble(o -> null==o.getTotalydsprocessed()?0:o.getTotalydsprocessed()).sum();
				theStockout.setTotal_mex(t_Mex==null?0:t_Mex);
				theStockout.setTotal_mex_stockout(t_Mex_stockout==null?0:t_Mex_stockout);
				
				Double t_MixMaterial = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_MIXMATERIAL)).mapToDouble(o -> null==o.getTotalorder_tech()?0:o.getTotalorder_tech()).sum();
				Double t_MixMaterial_stockout = theStockout.getStockoutd().stream().filter(o -> o.getSkutypeid_link().equals(ProductType.SKU_TYPE_MIXMATERIAL)).mapToDouble(o -> null==o.getTotalydsprocessed()?0:o.getTotalydsprocessed()).sum();
				theStockout.setTotal_mixmaterial(t_MixMaterial==null?0:t_MixMaterial);
				theStockout.setTotal_mixmaterial_stockout(t_MixMaterial_stockout==null?0:t_MixMaterial_stockout);
				
			} catch(Exception ex){
				ex.printStackTrace();
			}
		}
		return lstStockout;
	}
}
