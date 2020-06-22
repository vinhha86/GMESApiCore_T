package vn.gpay.gsmart.core.api.markettype;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.markettype.IMarketTypeService;
import vn.gpay.gsmart.core.markettype.MarketType;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/markettype")
public class MarketTypeAPI {
	@Autowired IMarketTypeService marketService;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<Market_getall_response> Product_GetAll(HttpServletRequest request,
			@RequestBody MarketService_getall_request entity) {
		Market_getall_response response = new Market_getall_response();
		try {
			List<MarketType> branch = marketService.getMarket_by_Status(entity.status);
						
			response.data = branch;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Market_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Market_getall_response>(response, HttpStatus.OK);
		}
	}
	
}
