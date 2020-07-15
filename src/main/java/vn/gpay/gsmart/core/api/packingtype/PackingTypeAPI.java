package vn.gpay.gsmart.core.api.packingtype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import vn.gpay.gsmart.core.packingtype.IPackingTypeService;
import vn.gpay.gsmart.core.packingtype.PackingType;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/packingtype")
public class PackingTypeAPI {
	@Autowired 
	IPackingTypeService packingtypeService;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<PackingType_getall_response> Product_GetAll(HttpServletRequest request) {
		PackingType_getall_response response = new PackingType_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<PackingType> packingtype = packingtypeService.getall_byorgrootid(user.getRootorgid_link());
			
			response.data = packingtype;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PackingType_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PackingType_getall_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
