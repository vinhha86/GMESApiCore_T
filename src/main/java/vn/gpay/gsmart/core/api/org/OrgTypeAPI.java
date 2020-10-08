package vn.gpay.gsmart.core.api.org;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.org.IOrgTypeService;
import vn.gpay.gsmart.core.org.OrgType;

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
}
