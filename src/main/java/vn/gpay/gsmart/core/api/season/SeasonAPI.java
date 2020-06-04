package vn.gpay.gsmart.core.api.season;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.season.ISeasonService;
import vn.gpay.gsmart.core.season.Season;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/season")
public class SeasonAPI {
	@Autowired ISeasonService seasonservice;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<Season_getall_response> Product_GetAll(HttpServletRequest request,
			@RequestBody Season_getall_request entity) {
		Season_getall_response response = new Season_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<Season> season = seasonservice.getall_byorgrootid(user.getRootorgid_link());
			
			if(entity.isAll) {
				Season b = new Season();
				b.setId((long)0);
				b.setName("Tất cả");
				season.add(0, b);
			}
			
			response.data = season;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Season_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Season_getall_response>(response, HttpStatus.OK);
		}
	}
}
