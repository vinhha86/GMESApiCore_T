package vn.gpay.gsmart.core.api.stockin;

import java.util.Date;
import java.util.List;
import java.util.UUID;

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
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.base.Stockintype;
import vn.gpay.gsmart.core.invoice.IInvoiceService;
import vn.gpay.gsmart.core.packinglist.IPackingListService;
import vn.gpay.gsmart.core.packinglist.PackingList;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.stockin.IStockInDService;
import vn.gpay.gsmart.core.stockin.IStockInService;
import vn.gpay.gsmart.core.stockin.StockIn;
import vn.gpay.gsmart.core.stockin.StockInD;
import vn.gpay.gsmart.core.stockout.IStockOutService;
import vn.gpay.gsmart.core.tagencode.ITagEncodeService;
import vn.gpay.gsmart.core.utils.ResponseMessage;
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
	@Autowired ITagEncodeService tagencodeService;
	
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
		ResponseBase response = new ResponseBase();
		try {
			if(entity.data.size()>0) {
				GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
				if (user != null){
					UUID uuid = UUID.randomUUID();
				    String stockincode = uuid.toString();
				    StockIn stockin =entity.data.get(0);
				    if(stockin.getId()==null || stockin.getId()==0) {
				    	stockin.setOrgid_link(user.getRootorgid_link());
				    	stockin.setOrgid_to_link(user.getOrgId());
				    	stockin.setUsercreateid_link(user.getUserId());
				    	stockin.setTimecreate(new Date());
				    	stockin.setStockincode(stockincode);
				    	stockin.setStatus(0);
				    }else {
				    	stockin.setOrgid_link(user.getRootorgid_link());
				    	stockin.setOrgid_to_link(user.getOrgId());
				    	stockin.setLastuserupdateid_link(user.getUserId());
				    	stockin.setLasttimeupdate(new Date());
				    }
				    stockin.getStockind().forEach(stockind -> {
				    	if(stockind.getId()==null || stockind.getId()==0) {
				    		stockind.setOrgid_link(user.getRootorgid_link());
				    		stockind.setUsercreateid_link(user.getUserId());
				    		stockind.setTimecreate(new Date());
				    		stockind.getPackinglist().forEach(pklist-> {
				    			if(pklist.getUsercreateid_link()==null || pklist.getUsercreateid_link()==0) {
					    			pklist.setOrgid_link(user.getRootorgid_link());
					    			pklist.setUsercreateid_link(user.getUserId());
					    			pklist.setTimecreate(new Date());
					    			pklist.setStockid_link(user.getOrgId());
					    			
					    			
					    			//if stockin complete product from encode
									if (stockin.getStockintypeid_link() == 6){
										tagencodeService.deleteByEpc(pklist.getEpc(), user.getRootorgid_link());
									}					    			
				    			}
				    		});
				    	}
			    	});
					sockInService.create(stockin);
					
					//System.out.println("stocking type:" + stockin.getStockintypeid_link().toString());
					//if stockin from invoice
					if (stockin.getStockintypeid_link() == 1){
						if(stockin.getInvoicenumber() != null) {
							invoiceService.updateStatusByInvoicenumber(stockin.getInvoicenumber());
				    	}
					}
					//if stockin from stockout
					if (stockin.getStockintypeid_link() == 4){
						if(stockin.getStockoutid_link() != null) {
							stockoutService.updateStatusById(stockin.getStockoutid_link());
				    	}
					}
					

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
	@RequestMapping(value = "/stockin_list",method = RequestMethod.POST)
	public ResponseEntity<?> StockinList(@RequestBody StockinListRequest entity, HttpServletRequest request ) {
		StockInResponse response = new StockInResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			Long orgid_to_link =null;
			if(!user.isOrgRoot()) {
				orgid_to_link =user.getOrgId();
			}
			response.data = sockInService.stocin_list(user.getRootorgid_link(),entity.stockintypeid_link,entity.stockincode, entity.stockcode, orgid_to_link,entity.stockindate_from, entity.stockindate_to,entity.status);
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
			response.epcs= warehouseService.inv_getbyid(entity.id);
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
			sockInService.deleteById(entity.id);
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
