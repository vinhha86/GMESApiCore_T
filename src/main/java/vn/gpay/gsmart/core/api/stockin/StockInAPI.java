package vn.gpay.gsmart.core.api.stockin;

import java.util.ArrayList;
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

import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.invoice.IInvoiceService;
import vn.gpay.gsmart.core.packinglist.IPackingListService;
import vn.gpay.gsmart.core.packinglist.PackingList;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.stockin.IStockInDService;
import vn.gpay.gsmart.core.stockin.IStockInService;
import vn.gpay.gsmart.core.stockin.StockIn;
import vn.gpay.gsmart.core.stockin.StockInD;
import vn.gpay.gsmart.core.stockin.StockInPklist;
import vn.gpay.gsmart.core.stockin_type.IStockinTypeService;
import vn.gpay.gsmart.core.stocking_uniquecode.IStocking_UniqueCode_Service;
import vn.gpay.gsmart.core.stocking_uniquecode.Stocking_UniqueCode;
import vn.gpay.gsmart.core.stockout.IStockOutService;
import vn.gpay.gsmart.core.tagencode.IWareHouse_Encode_EPC_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.utils.StockinStatus;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;
import vn.gpay.gsmart.core.warehouse.Warehouse;

@RestController
@RequestMapping("/api/v1/stockin")
public class StockInAPI {

	@Autowired IStockInService sockInService;
	@Autowired IStockInDService sockInServiced;
	@Autowired IPackingListService packingListService;
	@Autowired IInvoiceService invoiceService;
	@Autowired IStockOutService stockoutService;
	@Autowired IWarehouseService warehouseService;
	@Autowired IWareHouse_Encode_EPC_Service tagencodeService;
	@Autowired ISKU_Service skuService;
	@Autowired IStockinTypeService stockintypeService;
	@Autowired IStocking_UniqueCode_Service stockingService;
	@Autowired Common common;
	
	@RequestMapping(value = "/get",method = RequestMethod.GET)
	public ResponseEntity<?> get() {
		try {
			ResponseBase response = new ResponseBase();
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
	
	@RequestMapping(value = "/stockin_create",method = RequestMethod.POST)
	public ResponseEntity<?> StockinCreate(@RequestBody StockinCreateRequest entity, HttpServletRequest request ) {
		Stockin_create_response response = new Stockin_create_response();
		try {
			if(entity.data.size()>0) {
				GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
				if (user != null) {
					//Nếu thêm mới isNew = true
					boolean isNew = false;
					
				    StockIn stockin =entity.data.get(0);
				    if(stockin.getId()==null || stockin.getId()==0) {
				    	isNew = true;
				    	stockin.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
//				    	if (null == stockin.getOrgid_to_link())
//				    		stockin.setOrgid_to_link(user.getOrgId());
				    	
				    	stockin.setUsercreateid_link(user.getUserId());
				    	stockin.setTimecreate(new Date());
				    	stockin.setStockincode(common.GetStockinCode());
				    	stockin.setStatus(0);
				    }
				    else {
				    	stockin.setOrgrootid_link(user.getRootorgid_link());
				    	
				    	//Nếu không cung cấp kho đích, mặc định kho đích là đơn vị của user đăng nhập
				    	if (null == stockin.getOrgid_to_link())
//				    		stockin.setOrgid_to_link(user.getOrgId());
				    	
				    	stockin.setLastuserupdateid_link(user.getUserId());
				    	stockin.setLasttimeupdate(new Date());
				    }
				    
				    List<StockInD> listd = stockin.getStockin_d();
				    for (StockInD stockind : listd) {
				    	if(stockind.getId()==null || stockind.getId()==0) {
				    		stockind.setOrgrootid_link(user.getRootorgid_link());
				    		stockind.setUsercreateid_link(user.getUserId());
				    		stockind.setTimecreate(new Date());
				    		
				    		List<StockInPklist> epcInCorrect = new ArrayList<StockInPklist>();
				    		
				    		for (StockInPklist pklist : stockind.getStockin_packinglist()) {
				    			if(pklist.getUsercreateid_link()==null || pklist.getUsercreateid_link()==0) {
					    			pklist.setOrgrootid_link(user.getRootorgid_link());
					    			pklist.setId(null);
					    			pklist.setUsercreateid_link(stockind.getUsercreateid_link());
					    			pklist.setTimecreate(new Date());
					    			
				    				//Nếu epc đã có trong bảng Warehouse, bỏ qua ko nhập kho chíp này
				    				if (!warehouseService.epcExistedInStock(pklist.getEpc(), stockin.getOrgid_to_link())) {
						    			if (null == pklist.getSkuid_link()) {
						    				if (null!=pklist.getSkucode()) {
						    					SKU theSKU = skuService.getSKU_byCode(stockind.getSkucode(), user.getRootorgid_link());
						    					if (null != theSKU) {
						    						pklist.setSkuid_link(theSKU.getId());
						    						pklist.setColorid_link(theSKU.getColorid_link());
						    						pklist.setSizeid_link(stockind.getSizeid_link());
						    						pklist.setUnitid_link(theSKU.getUnitid_link().intValue());
						    						pklist.setSkutypeid_link(theSKU.getSkutypeid_link());
						    						pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_OK);
								    				
								    				
								    				Warehouse stockin_ws = new Warehouse();
								    				stockin_ws.setSkucode(stockind.getSkucode());
									    			//Orgid_link trong Warehouse la noi hang dc chuyyen toi
								    				stockin_ws.setStockid_link(stockin.getOrgid_to_link());
								    				stockin_ws.setUnitprice(stockind.getUnitprice());
								    				stockin_ws.setColor(pklist.getColor());
								    				stockin_ws.setColorid_link(pklist.getColorid_link());
								    				stockin_ws.setEncryptdatetime(new Date());
								    				stockin_ws.setEpc(pklist.getEpc());
								    				stockin_ws.setGrossweight(pklist.getGrossweight());
								    				stockin_ws.setLasttimeupdate(pklist.getLasttimeupdate());
								    				stockin_ws.setLastuserupdateid_link(pklist.getLastuserupdateid_link());
								    				stockin_ws.setLotnumber(pklist.getLotnumber());
								    				stockin_ws.setNetweight(pklist.getNetweight());
								    				stockin_ws.setOrgrootid_link(pklist.getOrgrootid_link());
								    				stockin_ws.setPackageid(pklist.getPackageid());
								    				stockin_ws.setSizeid_link(pklist.getSizeid_link());
								    				stockin_ws.setSkuid_link(pklist.getSkuid_link());
								    				stockin_ws.setSkutypeid_link(pklist.getSkutypeid_link());
								    				stockin_ws.setStockindid_link(null);
								    				stockin_ws.setStockinid_link(null);
								    				stockin_ws.setTimecreate(pklist.getTimecreate());
								    				stockin_ws.setUnitid_link(pklist.getUnitid_link());
									    			stockin_ws.setUsercreateid_link(pklist.getUsercreateid_link());
									    			stockin_ws.setWidth(pklist.getWidth());
//									    			stockin_ws.setYdscheck(pklist.getYds());
//									    			stockin_ws.setYdsorigin(pklist.getYds());
									    			
//									    			stockind.getStockin_warehouse().add(stockin_ws);
									    			warehouseService.save(stockin_ws);
						    					}
						    					else {
						    						//Neu hang day len ko co trong ban sku --> loai bo
						    						stockind.setTotalpackage(stockind.getTotalpackage()-1);
						    						stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
						    						
						    						pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR_OUTOFSKULIST);
						    						epcInCorrect.add(pklist);
						    					}
						    				}
						    				else {
					    						//Neu hang day len ko co trong ban sku --> loai bo
						    					stockind.setTotalpackage(stockind.getTotalpackage()-1);
						    					stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
						    					
						    					pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR_OUTOFSKULIST);
						    					epcInCorrect.add(pklist);
						    				}
						    			}
				    				}
				    				else {
				    					//Hang day len da ton tai trong Warehouse
				    					stockind.setTotalpackage(stockind.getTotalpackage()-1);
				    					stockind.setStatus(StockinStatus.STOCKIN_D_STATUS_ERR);
				    					
				    					pklist.setStatus(StockinStatus.STOCKIN_EPC_STATUS_ERR_WAREHOUSEEXISTED);
				    					epcInCorrect.add(pklist);
				    				}
					    			//Xoa trong bang Encode sau khi dong bo vao kho
//									if (stockin.getStockintypeid_link() == 6){
//										tagencodeService.deleteByEpc(pklist.getEpc(), user.getRootorgid_link());
//									}					    			
				    			}
				    		};
				    		
				    		//Xóa các epc không hợp lệ
				    		//stockind.getPackinglist().removeAll(epcInCorrect);
				    		
				    		// cập nhật lại stockin_packinglist trong stockind
				    	}
			    	};
				    
			    	stockin = sockInService.save(stockin);
					
					//Cập nhật lại bảng unique_code khi thêm mới
					if(isNew) {
						Stocking_UniqueCode unique = stockingService.getby_type(1);
						unique.setStocking_max(unique.getStocking_max()+ 1);
						stockingService.save(unique);
					}
					
					//System.out.println("stocking type:" + stockin.getStockintypeid_link().toString());
					//if stockin from invoice
					if (stockin.getStockintypeid_link() == 1) {
						if(stockin.getInvoice_number() != null) {
							invoiceService.updateStatusByInvoicenumber(stockin.getInvoice_number());
				    	}
					}
					//if stockin from stockout
					if (stockin.getStockintypeid_link() == 4) {
						if(stockin.getStockoutid_link() != null) {
							stockoutService.updateStatusById(stockin.getStockoutid_link());
				    	}
					}
					
					response.id = stockin.getId();
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);					
				}
				else {
					response.setRespcode(ResponseMessage.KEY_RC_AUTHEN_ERROR);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_AUTHEN_ERROR));
					return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);						
				}
			}
			else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
			    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}

		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_getone",method = RequestMethod.POST)
	public ResponseEntity<?> StockinGetone(@RequestBody StockinGetoneRequest entity, HttpServletRequest request ) {
		StockInResponse response = new StockInResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = sockInService.stockin_getone(user.getOrgId(),entity.stockincode, entity.stockcode);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockInResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/gettype",method = RequestMethod.POST)
	public ResponseEntity<?> Gettype(HttpServletRequest request ) {
		Stockin_gettype_response response = new Stockin_gettype_response();
		try {
			response.data = stockintypeService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Stockin_gettype_response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_list",method = RequestMethod.POST)
	public ResponseEntity<?> StockinList(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
		StockInResponse response = new StockInResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			
			Page<StockIn> pageStockin = sockInService.stockin_page(
					user.getRootorgid_link(), 
					entity.stockintypeid_link, 
					entity.orgid_from_link, 
					entity.orgid_to_link, 
					entity.stockindate_from, 
					entity.stockindate_to, 
					entity.limit, 
					entity.page);
			
			response.data = pageStockin.getContent();
			response.totalCount = pageStockin.getTotalElements();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<StockInResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		GetStockinByIDResponse response = new GetStockinByIDResponse();
		try {
			response.data = sockInService.findOne(entity.id);
			response.listepc = warehouseService.inv_getbyid(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/stockin_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> StockinDeleteByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			StockIn stockin = sockInService.findOne(entity.id);
			if (null != stockin && stockin.getStatus() == 0){
				//Xóa các dòng chíp trong bảng warehouse
				for (StockInD sku : stockin.getStockin_d()) {
					for (StockInPklist epc: sku.getStockin_packinglist()){
						warehouseService.deleteByEpc(epc.getEpc(), stockin.getOrgid_to_link());
					}
				}
				stockin.setStatus(-1);
				stockin.setLasttimeupdate(new Date());
				stockin.setLastuserupdateid_link(user.getUserId());
				sockInService.save(stockin);
				
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
	@RequestMapping(value = "/stockind_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> StockinDDeleteByID(@RequestBody StockinByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			sockInServiced.deleteById(entity.id);
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
	
	
	
	
	
	//cũ
	@RequestMapping(value = "/getStockin",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockin(HttpServletRequest request ) {
		GetStockinByCodeOutput output = new GetStockinByCodeOutput();
		try {
			output.data = sockInService.findAll();
			
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByCodeOutput>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/getStockinByCode",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByCode( @RequestBody GetStockinByCodeInput entity,HttpServletRequest request ) {
		GetStockinByCodeOutput output = new GetStockinByCodeOutput();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			output.data = sockInService.findByStockinCode(user.getOrgId(),entity.stockincode);
			
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetStockinByCodeOutput>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@RequestMapping(value = "/getPackingListByStockinDID",method = RequestMethod.POST)
	public ResponseEntity<?> GetPackingListByStockinDID( @RequestBody GetPackingListByStockinDID entity,HttpServletRequest request ) {
		GetPackingListByStockinDIDOutput output =new GetPackingListByStockinDIDOutput();
		try {
			output.data = packingListService.findByStockinDID(entity.stockindid);
			output.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			output.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<GetPackingListByStockinDIDOutput>(output,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/StockInInsert",method = RequestMethod.POST)
	public ResponseEntity<?> InvoiceInsert( @RequestBody StockInRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			if(entity.stockin!=null) {
				entity.stockin.setId(null);
				entity.stockin.setStockindate(new Date());
				entity.stockin.setStatus(1);
				StockIn stockin= sockInService.create(entity.stockin);
				if(stockin!=null && !entity.stockind.isEmpty()) {
					for (StockInD entry : entity.stockind) {
						entry.setId(null);
						entry.setStockinid_link(stockin.getId());
						sockInServiced.create(entry);
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
    
	@RequestMapping(value = "/PackinglistInsert",method = RequestMethod.POST)
	public ResponseEntity<?> PackinglistInsert( @RequestBody List<PackingList> entity,HttpServletRequest request ){
		ResponseBase response = new ResponseBase();
		try {
			if(entity!=null && entity.size()>0) {
				for(PackingList entry : entity) {
					//entry.setStatus(1);
					packingListService.create(entry);
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
}
