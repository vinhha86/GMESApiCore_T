package vn.gpay.gsmart.core.api.salary;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.salary.IOrgSal_TypeService;
import vn.gpay.gsmart.core.salary.IOrgSal_Type_LevelService;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/salary")
public class SalaryAPI {
	@Autowired IOrgSal_TypeService saltypeService;
	@Autowired IOrgSal_Type_LevelService saltype_levelService;
	
	@RequestMapping(value = "/saltype_level_byorg", method = RequestMethod.POST)
	public ResponseEntity<saltype_level_response> saltype_byorg(HttpServletRequest request,
			@RequestBody saltype_level_request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		saltype_level_response response = new saltype_level_response();
		try {
			response.data = saltype_levelService.getall_byorg_and_type(entity.orgid_link,entity.typeid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<saltype_level_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<saltype_level_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
