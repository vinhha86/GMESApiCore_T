package vn.gpay.gsmart.core.api.porder_grant;


import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.POrderStatus;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porder_grant")
public class POrder_GrantAPI {
	@Autowired private IPOrderGrant_Service porderGrantService;
	@Autowired private IPOrder_Service porderService;
    ObjectMapper mapper = new ObjectMapper();
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
	public ResponseEntity<POrder_GrantCreate_response> Create(HttpServletRequest request,
			@RequestBody POrder_GrantCreate_request entity) {
		POrder_GrantCreate_response response = new POrder_GrantCreate_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
		
			POrderGrant porder_grant = entity.data;
			
			//Lay thong tin PO
			POrder thePOrder = porderService.findOne(porder_grant.getPorderid_link());
			
			if (porder_grant.getId() == null || porder_grant.getId() == 0) {
				porder_grant.setOrgrootid_link(orgrootid_link);
				porder_grant.setOrdercode(thePOrder.getOrdercode());
				
				porder_grant.setGrantdate(new Date());
				porder_grant.setUsercreatedid_link(user.getId());
				porder_grant.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
				porder_grant.setTimecreated(new Date());
			} 
			
			porderGrantService.save(porder_grant);
			response.id = porder_grant.getId();
			
			thePOrder.setStatus(POrderStatus.PORDER_STATUS_GRANTED);
			porderService.save(thePOrder);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<POrder_GrantCreate_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<POrder_GrantCreate_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
