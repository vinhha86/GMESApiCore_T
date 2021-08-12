package vn.gpay.gsmart.core.api.org;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;

import vn.gpay.gsmart.core.org.IOrgTypeService;
import vn.gpay.gsmart.core.org.OrgType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/orgtype")
public class OrgTypeAPI {
	@Autowired IOrgTypeService orgTypeService;
	
	
	@RequestMapping("/getAllOrgType")
	public ResponseEntity<?> getAllOrgType(HttpServletRequest request ) {
		try {
			OrgTypeResponse response = new OrgTypeResponse();
//			List<OrgType> all = orgTypeService.findOrgTypeForMenuOrg();
			List<OrgType> all = orgTypeService.findAll();
			response.data=all;
			return new ResponseEntity<OrgTypeResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	/**
	 * theem moi orgtype
	 * @param request
	 * @return
	 */
		@RequestMapping(value = "/create", method = RequestMethod.POST)
		public ResponseEntity<OrgType_create_response> OrgType_Add(
				@RequestBody OrgType_create_request entity, HttpServletRequest request) {
			OrgType_create_response response = new OrgType_create_response();
			try {
				
				OrgType orgtype = entity.data;
				orgTypeService.save(orgtype);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			} catch (RuntimeException e) {
				response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				response.setMessage(e.getMessage());
			}
			return new ResponseEntity<OrgType_create_response>(response, HttpStatus.OK);
		}
		/**
		 * Xoa
		 * @param entity
		 * @param request
		 * @return
		 */
		@RequestMapping(value = "/delete", method = RequestMethod.POST)
		public ResponseEntity<OrgType_delete_response> OrgType_delete(
				@RequestBody OrgType_delete_request entity, HttpServletRequest request) {
			OrgType_delete_response response = new OrgType_delete_response();
			try {
				
				OrgType orgtype = entity.data;
				orgTypeService.delete(orgtype);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			} catch (RuntimeException e) {
				response.setRespcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
				response.setMessage(e.getMessage());
			}
			return new ResponseEntity<OrgType_delete_response>(response, HttpStatus.OK);
		}
}
