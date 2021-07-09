package vn.gpay.gsmart.core.api.stockout;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.gsmart.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.gsmart.core.stockout.IStockOutPklistService;
import vn.gpay.gsmart.core.stockout.IStockOutService;
import vn.gpay.gsmart.core.stockout.StockOut;
import vn.gpay.gsmart.core.stockout.StockOutD;
import vn.gpay.gsmart.core.stockout.StockOutPklist;
import vn.gpay.gsmart.core.stockout_type.IStockoutTypeService;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.StockoutStatus;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;
import vn.gpay.gsmart.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/stockout")
public class StockOutAPI {

	@Autowired IStockOutService stockOutService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IStockOutPklistService stockOutPklistService;
	@Autowired IStockoutTypeService stockouttypeService;
	@Autowired Common commonService;
	@Autowired IStocking_UniqueCode_Service stockingService;
	
	@RequestMapping(value = "/stockout_create",method = RequestMethod.POST)
	public ResponseEntity<?> StockoutCreate(@RequestBody StockoutCreateRequest entity, HttpServletRequest request ) {
		StockOut_Create_response response = new StockOut_Create_response();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
		    String stockoutcode = commonService.GetStockoutCode();
		  //NÃªÌ�u thÃªm mÆ¡Ì�i isNew = true
			boolean isNew = false;
			
			if(entity.data.size()>0) {
				StockOut stockout = entity.data.get(0);
				if(stockout.getId()==null || stockout.getId()==0) {
					stockout.setUsercreateid_link(user.getUserId());
					stockout.setOrgrootid_link(user.getRootorgid_link());
					stockout.setOrgid_from_link(user.getOrgId());
					stockout.setTimecreate(new Date());
					stockout.setStockoutcode(stockoutcode);
					stockout.setStatus(0);
					
					isNew = true;
					
			    }else {
			    	stockout.setOrgrootid_link(user.getRootorgid_link());
			    	stockout.setLastuserupdateid_link(user.getUserId());
			    	stockout.setLasttimeupdate(new Date());
			    }
				for (StockOutD stockoutd : stockout.getStockoutd())
				{
					stockoutd.setStatus(StockoutStatus.STOCKOUT_D_STATUS_OK);
					stockoutd.setOrgrootid_link(user.getRootorgid_link());
					
					for (StockOutPklist item_epc : stockoutd.getStockout_packinglist()) {
						item_epc.setOrgrootid_link(user.getRootorgid_link());
						
						if (null == item_epc.getId()){
							if (warehouseService.epcExistedInStock(item_epc.getEpc(), stockout.getOrgid_from_link())){
								
								item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_OK);
							} else {
								//Thêm thông tin epc lỗi vào response để trả về frontend thông báo cho User
								item_epc.setStatus(StockoutStatus.STOCKOUT_EPC_STATUS_ERR_WAREHOUSENOTEXIST);
								response.epc_err.add(item_epc);
							}
						}
					}
		    	}
				if (response.epc_err.size() > 0){
					//Nếu có EPC lỗi --> Trả về lỗi cho Front end
					response.setRespcode(ResponseMessage.KEY_RC_EPC_INVALID);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EPC_INVALID));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.INTERNAL_SERVER_ERROR);						
				} else {
					//Nếu các EPC đều hợp lệ, Xóa epc trong bảng Warehouse
					List<StockOutD> stockoutd = stockout.getStockoutd();
					if(stockoutd !=null && stockoutd.size() >0) {
						for (StockOutD item_sku : stockoutd) {
							List<StockOutPklist> Pklist = item_sku.getStockout_packinglist();
							if(Pklist !=null && Pklist.size() >0) {
								for (StockOutPklist item_epc : Pklist) {
									warehouseService.deleteByEpc(item_epc.getEpc(),user.getOrgId());
								}
							}
						}
					}
					
					//Ghi Phiếu xuất kho
					stockout = stockOutService.save(stockout);
					
					//CÃ¢Ì£p nhÃ¢Ì£t laÌ£i baÌ‰ng unique_code khi thÃªm mÆ¡Ì�i
					if(isNew) {
						Stocking_UniqueCode unique = stockingService.getby_type(2);
						unique.setStocking_max(unique.getStocking_max()+ 1);
						stockingService.save(unique);
					}
					
					response.id = stockout.getId();
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);					
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_BAD_REQUEST));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.INTERNAL_SERVER_ERROR);			
			}
			
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
	
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<?> GetType( HttpServletRequest request ) {
		Stockout_gettype_response response = new Stockout_gettype_response();
		try {
			response.data = stockouttypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockout_gettype_response>(response,HttpStatus.OK);
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
			Long orgid_from_link = (long)0;
			long orgrootid_link = user.getRootorgid_link();
			if(!user.isOrgRoot()) {
				orgid_from_link =entity.orgid_from_link;
			}
			
			Page<StockOut> pageStockout = stockOutService.stockout_list_page(
					orgrootid_link,
					entity.stockouttypeid_link,
					entity.stockoutcode,
					orgid_from_link,
					entity.orgid_to_link,
					entity.stockoutdate_from, 
					entity.stockoutdate_to ,
					entity.page, 
					entity.limit);
			response.data = pageStockout.getContent();
			response.totalCount = pageStockout.getTotalElements();
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
	public ResponseEntity<?> StockoutGetByID(@RequestBody GetStockoutByIDRequest entity, HttpServletRequest request ) {
		StockoutByIDReponse response = new StockoutByIDReponse();
		try {
			response.data = stockOutService.findOne(entity.id);
			response.listepc = stockOutPklistService.inv_getbyid(entity.id);
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
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			StockOut stockout = stockOutService.findOne(entity.id);
			if (null != stockout && stockout.getStatus() == 0){
				//Hoàn trả epc đã xuất về warehouse
				for (StockOutD sku: stockout.getStockoutd()){
					for(StockOutPklist epc: sku.getStockout_packinglist()){
	    				Warehouse stockin_ws = new Warehouse();
	
		    			//Orgid_link trong Warehouse la noi hang dc chuyyen toi
	    				stockin_ws.setStockid_link(stockout.getOrgid_from_link());
	    				
	    				stockin_ws.setSkuid_link(sku.getSkuid_link());
	    				stockin_ws.setSkucode(sku.getSkucode());
	    				stockin_ws.setUnitprice(sku.getUnitprice());
	    				stockin_ws.setColor(sku.getColor());
	    				stockin_ws.setColorid_link(sku.getColorid_link());
	    				stockin_ws.setSizeid_link(sku.getSizeid_link());
	    				stockin_ws.setUnitid_link(sku.getUnitid_link());
	
	    				stockin_ws.setEncryptdatetime(new Date());
	    				stockin_ws.setEpc(epc.getEpc());
	    				stockin_ws.setGrossweight(epc.getGrossweight());
	    				stockin_ws.setLasttimeupdate(epc.getLasttimeupdate());
	    				stockin_ws.setLastuserupdateid_link(epc.getLastuserupdateid_link());
	    				stockin_ws.setLotnumber(epc.getLotnumber());
	    				stockin_ws.setNetweight(epc.getNetweight());
	    				stockin_ws.setOrgrootid_link(epc.getOrgrootid_link());
	    				stockin_ws.setPackageid(epc.getPackageid());
	    				stockin_ws.setSkutypeid_link(epc.getSkutypeid_link());
	    				stockin_ws.setStockindid_link(null);
	    				stockin_ws.setStockinid_link(null);
	    				stockin_ws.setTimecreate(epc.getTimecreate());
		    			stockin_ws.setUsercreateid_link(epc.getUsercreateid_link());
		    			stockin_ws.setWidth(epc.getWidthorigin());
		    			
		    			warehouseService.save(stockin_ws);
					}
				}
				
				stockout.setStatus(-1);
				stockout.setLasttimeupdate(new Date());
				stockout.setLastuserupdateid_link(user.getUserId());
				stockOutService.save(stockout);
				
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_RS_NOT_FOUND);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_RS_NOT_FOUND));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
