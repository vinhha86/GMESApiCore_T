package vn.gpay.gsmart.core.api.handover_product;

import java.util.Date;
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

import vn.gpay.gsmart.core.handover.Handover;
import vn.gpay.gsmart.core.handover.IHandoverService;
import vn.gpay.gsmart.core.handover_product.HandoverProduct;
import vn.gpay.gsmart.core.handover_product.IHandoverProductService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handoverproduct")
public class HandoverProductAPI {
	@Autowired IHandoverProductService handoverProductService;
	@Autowired IHandoverService handoverService;
	@Autowired IPOrder_Service porderService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<HandoverProduct_getall_response> GetAll(HttpServletRequest request ) {
		HandoverProduct_getall_response response = new HandoverProduct_getall_response();
		try {
			response.data = handoverProductService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverProduct_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverProduct_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByHandoverId",method = RequestMethod.POST)
	public ResponseEntity<HandoverProduct_getall_response> GetByHandoverId(@RequestBody HandoverProduct_GetByHandoverId_request entity,HttpServletRequest request ) {
		HandoverProduct_getall_response response = new HandoverProduct_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<HandoverProduct> list = handoverProductService.getByHandoverId(entity.handoverid_link);
			if(list.size() > 0) {
				response.data = list;
			}else {
				Handover handover = handoverService.findOne(entity.handoverid_link);
				POrder porder = porderService.findOne(handover.getPorderid_link());
				Date date = new Date();
				
				HandoverProduct handoverProduct = new HandoverProduct();
				handoverProduct.setId(0L);
				handoverProduct.setOrgrootid_link(user.getRootorgid_link());
				handoverProduct.setHandoverid_link(handover.getId());
				handoverProduct.setProductid_link(porder.getProductid_link());
				handoverProduct.setUnitid_link(2L);
				handoverProduct.setUsercreateid_link(user.getId());
				handoverProduct.setTimecreate(date);
				handoverProduct.setLasttimeupdate(date);
				handoverProduct.setLastuserupdateid_link(user.getId());
				handoverProduct.setTotalpackage(0);
				
				handoverProductService.save(handoverProduct);
				list = handoverProductService.getByHandoverId(entity.handoverid_link);
				response.data = list;
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverProduct_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverProduct_getall_response>(response,HttpStatus.OK);
		}
	}
}
