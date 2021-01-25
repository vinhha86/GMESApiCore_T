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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.category.IUnitService;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.handover.Handover;
import vn.gpay.gsmart.core.handover.IHandoverService;
import vn.gpay.gsmart.core.handover_product.HandoverProduct;
import vn.gpay.gsmart.core.handover_product.IHandoverProductService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handoverproduct")
public class HandoverProductAPI {
	@Autowired IHandoverProductService handoverProductService;
	@Autowired IHandoverService handoverService;
	@Autowired IPOrder_Service porderService;
	@Autowired IProductService productService;
	@Autowired IUnitService unitService;
	
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
	
	@RequestMapping(value = "/updateHandoverProduct",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> updateHandoverProduct(@RequestBody HandoverProduct_update_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			
			Date date = new Date();
			HandoverProduct handoverProduct = entity.data;
			handoverProduct.setLasttimeupdate(date);
			handoverProduct.setLastuserupdateid_link(user.getId());
			handoverProductService.save(handoverProduct);
			
			Handover handover = handoverService.findOne(entity.data.getHandoverid_link());
			
			List<HandoverProduct> handoverProducts = handover.getHandoverProducts();
			Integer total = 0;
			Integer totalCheck = 0;
			for(HandoverProduct hp : handoverProducts) {
				if(hp.getTotalpackage() != null)
					total += hp.getTotalpackage();
				if(hp.getTotalpackagecheck() != null)
					totalCheck += hp.getTotalpackagecheck();
			}
			
			handover.setTotalpackage(total);
			handover.setTotalpackagecheck(totalCheck);
			handover.setLasttimeupdate(date);
			handover.setLastuserupdateid_link(user.getId());
			handoverService.save(handover);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByPorderId",method = RequestMethod.POST)
	public ResponseEntity<HandoverProduct_GetByPorderId_response> GetByPorderId(@RequestBody HandoverProduct_GetByPorderId_request entity,HttpServletRequest request ) {
		HandoverProduct_GetByPorderId_response response = new HandoverProduct_GetByPorderId_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			POrder porder = porderService.findOne(entity.porderid_link);
			Product product = productService.findOne(porder.getProductid_link());
			Unit unit = unitService.findOne(2);
//			Date date = new Date();

			HandoverProduct handoverProduct = new HandoverProduct();
			handoverProduct.setId(0L);
			handoverProduct.setOrgrootid_link(user.getRootorgid_link());
//			handoverProduct.setHandoverid_link(handover.getId());
			handoverProduct.setProductid_link(porder.getProductid_link());
			handoverProduct.setUnitid_link(2L);
//			handoverProduct.setUsercreateid_link(user.getId());
//			handoverProduct.setTimecreate(date);
//			handoverProduct.setLasttimeupdate(date);
//			handoverProduct.setLastuserupdateid_link(user.getId());
			handoverProduct.setTotalpackage(0);
			handoverProduct.setTotalpackagecheck(0);

			response.data = handoverProduct;
			response.buyername = product.getBuyername();
			response.buyercode = product.getBuyercode();
			response.unitName = unit.getName();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverProduct_GetByPorderId_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverProduct_GetByPorderId_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getNewHandoverProductByProductId",method = RequestMethod.POST)
	public ResponseEntity<HandoverProduct_GetByPorderId_response> getNewHandoverProductByProductId(@RequestBody HandoverProduct_GetByPorderId_request entity,HttpServletRequest request ) {
		HandoverProduct_GetByPorderId_response response = new HandoverProduct_GetByPorderId_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Product product = productService.findOne(entity.productid_link);
			Unit unit = unitService.findOne(2);
//			Date date = new Date();

			HandoverProduct handoverProduct = new HandoverProduct();
			handoverProduct.setId(0L);
			handoverProduct.setOrgrootid_link(user.getRootorgid_link());
//			handoverProduct.setHandoverid_link(handover.getId());
			handoverProduct.setProductid_link(entity.productid_link);
			handoverProduct.setUnitid_link(2L);
//			handoverProduct.setUsercreateid_link(user.getId());
//			handoverProduct.setTimecreate(date);
//			handoverProduct.setLasttimeupdate(date);
//			handoverProduct.setLastuserupdateid_link(user.getId());
			handoverProduct.setTotalpackage(entity.product_quantity);

			response.data = handoverProduct;
			response.buyername = product.getBuyername();
			response.buyercode = product.getBuyercode();
			response.unitName = unit.getName();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverProduct_GetByPorderId_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverProduct_GetByPorderId_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody HandoverProduct_update_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			HandoverProduct hp = entity.data;
			handoverProductService.save(hp);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
}
