package vn.gpay.gsmart.core.api.cutplan_processing;

import java.util.ArrayList;
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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.cutplan.ICutPlan_Row_Service;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessing;
import vn.gpay.gsmart.core.cutplan_processing.CutplanProcessingD;
import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingDService;
import vn.gpay.gsmart.core.cutplan_processing.ICutplanProcessingService;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/cutplan_processing")
public class CutplanProcessingAPI {
	@Autowired ICutplanProcessingService cutplanProcessingService;
	@Autowired ICutplanProcessingDService cutplanProcessingDService;
	@Autowired ICutPlan_Row_Service cutplanRowService;
	@Autowired IOrgService orgService;
	
	@RequestMapping(value = "/cutplan_processing_create",method = RequestMethod.POST)
	@Transactional(rollbackFor = RuntimeException.class)
	public ResponseEntity<?> CutplanProcessingCreate(@RequestBody CutplanProcessingCreateRequest entity, HttpServletRequest request ) {
		CutplanProcessingResponse response = new CutplanProcessingResponse();
		
		try {
			
			if(entity.data.size()>0) {
				GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
				if (user != null) {
					//Nếu thêm mới isNew = true
					boolean isNew = false;
					
					CutplanProcessing cutplanProcessing =entity.data.get(0);
				    if(cutplanProcessing.getId()==null || cutplanProcessing.getId()==0) {
				    	isNew = true;
				    	cutplanProcessing.setOrgrootid_link(user.getRootorgid_link());
				    	cutplanProcessing.setUsercreatedid_link(user.getUserId());
				    	cutplanProcessing.setTimecreated(new Date());
				    	cutplanProcessing.setStatus(0);
				    }
				    
				    List<CutplanProcessingD> cutplanProcessingDs = cutplanProcessing.getCutplanProcessingD();
				    for (CutplanProcessingD cutplanProcessingD : cutplanProcessingDs) {
				    	if(cutplanProcessingD.getId()==null || cutplanProcessingD.getId()==0) {
				    		cutplanProcessingD.setOrgrootid_link(user.getRootorgid_link());
				    		cutplanProcessingD.setTimecreated(new Date());
				    	}
			    	};
				    
			    	cutplanProcessing = cutplanProcessingService.save(cutplanProcessing);
					
					response.data = cutplanProcessing;
					response.id = cutplanProcessing.getId();
					
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
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_list",method = RequestMethod.POST)
	public ResponseEntity<?> CutplanProcessingList(@RequestBody CutplanProcessingListRequest entity, HttpServletRequest request ) {
		CutplanProcessingListResponse response = new CutplanProcessingListResponse();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgid_link = user.getOrgid_link();
			Org userOrg = orgService.findOne(orgid_link);
			Long userOrgId = userOrg.getId();
			Integer userOrgType = userOrg.getOrgtypeid_link();
			
			if (entity.page == 0) entity.page = 1;
			if (entity.limit == 0) entity.limit = 100;
			
			// 
			
			response.data = cutplanProcessingService.findAll();
			
			for(CutplanProcessing cutplanProcessing : response.data) {
				System.out.println("masp " + cutplanProcessing.getMaSP());
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingListResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_processing_getbyid",method = RequestMethod.POST)
	public ResponseEntity<?> GetStockinByID(@RequestBody CutplanProcessingByIDRequest entity, HttpServletRequest request ) {
		CutplanProcessingByIDResponse response = new CutplanProcessingByIDResponse();
		try {
			response.data = cutplanProcessingService.findOne(entity.id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutplanProcessingByIDResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@RequestMapping(value = "/cutplan_row_by_porder",method = RequestMethod.POST)
	public ResponseEntity<?> CutplanRowByPorder(@RequestBody CutPlanRow_GetByPorder_Request entity, HttpServletRequest request ) {
		CutPlanRow_List_Response response = new CutPlanRow_List_Response();
		try {
//			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long porderid_link = entity.porderid_link;
			
			// 
			System.out.println("here");
			response.data = cutplanRowService.findByPOrder(porderid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<CutPlanRow_List_Response>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			e.printStackTrace();
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
