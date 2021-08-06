package vn.gpay.gsmart.core.api.personnel_type;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.api.personnel.gettype_response;
import vn.gpay.gsmart.core.personnel_type.IPersonnelType_Service;
import vn.gpay.gsmart.core.personnel_type.PersonnelType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/personnel_type")
public class Personnel_TypeAPI {
	@Autowired IPersonnelType_Service personneltypeService;
	
	//thêm mới loại nhân viên
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<gettype_response> getPersonnel_Type(HttpServletRequest request, @RequestBody Add_Personnel_Type_request entity  ) {
		gettype_response response = new gettype_response();
		try {
			PersonnelType per = entity.data;
			personneltypeService.save(entity.data);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}
	}
	
	//xóa  loại nhân viên
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<gettype_response> deletePersonnel_Type(HttpServletRequest request, @RequestBody Add_Personnel_Type_request entity  ) {
		gettype_response response = new gettype_response();
		try {
			PersonnelType per = entity.data;
			personneltypeService.delete(entity.data);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<gettype_response>(response,HttpStatus.OK);
		}
	}
}
