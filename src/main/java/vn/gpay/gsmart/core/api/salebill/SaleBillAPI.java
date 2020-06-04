package vn.gpay.gsmart.core.api.salebill;

import java.util.Date;
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
import vn.gpay.gsmart.core.salebill.ISaleBillService;
import vn.gpay.gsmart.core.salebill.SaleBill;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.utils.ResponseMessage;
import vn.gpay.gsmart.core.warehouse.IWarehouseService;

@RestController
@RequestMapping("/api/v1/salebill")
public class SaleBillAPI {

	@Autowired ISaleBillService saleBillService;
	@Autowired IWarehouseService warehouseService;
	@RequestMapping(value = "/salebill_create",method = RequestMethod.POST)
	public ResponseEntity<?> SaleBillCreate(@RequestBody SaleBillCreateRequest entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			if(entity.data.size()>0) {
				UUID uuid = UUID.randomUUID();
			    String billcode = uuid.toString();
				SaleBill saleBill = entity.data.get(0);
				if(saleBill.getId()==null || saleBill.getId()==0) {
					saleBill.setBillcode(billcode);
					saleBill.setUsercreateid_link(user.getUserId());
					saleBill.setOrgid_link(user.getRootorgid_link());
					saleBill.setOrgbillid_link(user.getOrgId());
					saleBill.setTimecreate(new Date());
					saleBill.setStatus(0);
			    }else {
			    	saleBill.setOrgid_link(user.getRootorgid_link());
					saleBill.setOrgbillid_link(user.getOrgId());
			    	saleBill.setLastuserupdateid_link(user.getUserId());
			    	saleBill.setLasttimeupdate(new Date());
			    }
				saleBill.getSkus().forEach(sku -> {
					sku.setOrgid_link(user.getRootorgid_link());
					if(sku.getId()==null || sku.getId()==0) {
						sku.setUsercreateid_link(user.getUserId());
						sku.setOrgid_link(user.getRootorgid_link());
						sku.setTimecreate(new Date());					
					}else {
						sku.setLastuserupdateid_link(user.getUserId());
						sku.setOrgid_link(user.getRootorgid_link());
						sku.setLasttimeupdate(new Date());
					}
					
		    	});
				
				saleBill.getEpcs().forEach(epc -> {
					epc.setOrgid_link(user.getRootorgid_link());
					if(epc.getId()==null || epc.getId()==0) {
						epc.setUsercreateid_link(user.getUserId());
						epc.setOrgid_link(user.getRootorgid_link());
						epc.setTimecreate(new Date());
						
			    		//delete epc from warehouse of the shop
			    		warehouseService.deleteByEpc(epc.getOldepc(), user.getOrgId());						
					}else {
						epc.setLastuserupdateid_link(user.getUserId());
						epc.setOrgid_link(user.getRootorgid_link());
						epc.setLasttimeupdate(new Date());
					}
					
		    	});
				saleBillService.save(saleBill);
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
	@RequestMapping(value = "/salebill_list",method = RequestMethod.POST)
	public ResponseEntity<?> SalebillList(@RequestBody SaleBillListRequest entity, HttpServletRequest request ) {
		SaleBillResponse response = new SaleBillResponse();
		try {
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			if(entity.billcode==null) entity.billcode="";
			Long orgbillid_link = entity.orgbillid_link;
			if(!user.isOrgRoot()) {
				orgbillid_link=user.getOrgId();
			}		
			response.data = saleBillService.salebill_list(orgbillid_link,entity.billcode, entity.salebilldate_from, entity.salebilldate_to);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SaleBillResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/salebill_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> SalebillGetByID(@RequestBody GetSaleBillByIDRequest entity, HttpServletRequest request ) {
		SaleBillByIDResponse response = new SaleBillByIDResponse();
		try {
			response.data = saleBillService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SaleBillByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/salebill_deleteid",method = RequestMethod.POST)
	public ResponseEntity<?> SalebillDeleteByID(@RequestBody GetSaleBillByIDRequest entity, HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			saleBillService.deleteById(entity.id);
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
