package vn.gpay.gsmart.core.api.handover;

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
import vn.gpay.gsmart.core.handover.IHandover_AutoID_Service;
import vn.gpay.gsmart.core.handover_product.HandoverProduct;
import vn.gpay.gsmart.core.handover_product.IHandoverProductService;
import vn.gpay.gsmart.core.handover_sku.HandoverSKU;
import vn.gpay.gsmart.core.handover_sku.IHandoverSKUService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handover")
public class HandoverAPI {
	@Autowired IHandoverService handoverService;
	@Autowired IHandoverProductService handoverProductService;
	@Autowired IHandoverSKUService handoverSkuService;
	@Autowired IHandover_AutoID_Service handoverAutoIdService;
	@Autowired IPOrder_Service porderService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> GetAll(HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			response.data = handoverService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<Handover_create_response> Create(@RequestBody Handover_create_request entity,HttpServletRequest request ) {
		Handover_create_response response = new Handover_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Handover handover = entity.data;
			HandoverProduct handoverProduct = entity.handoverProduct;
			
			if(handover.getId()==null || handover.getId()==0) {
				// new
				if(handover.getHandover_code() == null || handover.getHandover_code().length() == 0) {
					POrder porder = porderService.findOne(handover.getPorderid_link());
					if(porder != null) {
						// Xuất từ cắt lên chuyền cho lệnh : CL
						handover.setHandover_code(handoverAutoIdService.getLastID("CL_" + porder.getOrdercode()));
					}else {
						handover.setHandover_code(handoverAutoIdService.getLastID("UNKNOWN"));
					}
				}else {
					// check existed
					String handover_code = handover.getHandover_code();
					List<Handover> lstcheck = handoverService.getByHandoverCode(handover_code);
					if(lstcheck.size() > 0) {
						response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
						response.setMessage("Mã đã tồn tại trong hệ thống!");
						return new ResponseEntity<Handover_create_response>(response, HttpStatus.BAD_REQUEST);
					}
				}
				
				Date date = new Date();
				handover.setOrgrootid_link(user.getRootorgid_link());
				handover.setUsercreateid_link(user.getId());
				handover.setTimecreate(date);
				handover.setLastuserupdateid_link(user.getId());
				handover.setLasttimeupdate(date);
				handover.setTotalpackage(handoverProduct.getTotalpackage());
				handover = handoverService.save(handover);
				
				handoverProduct.setHandoverid_link(handover.getId());
				handoverProduct.setUsercreateid_link(user.getId());
				handoverProduct.setLastuserupdateid_link(user.getId());
				handoverProduct.setTimecreate(date);
				handoverProduct.setLasttimeupdate(date);
				handoverProductService.save(handoverProduct);
			}else {
				// update
				Date date = new Date();
				Handover _handover =  handoverService.findOne(handover.getId());
				handover.setOrgrootid_link(_handover.getOrgrootid_link());
				handover.setUsercreateid_link(_handover.getUsercreateid_link());
				handover.setTimecreate(_handover.getTimecreate());
				handover.setLastuserupdateid_link(user.getId());
				handover.setLasttimeupdate(date);
				// nếu porder thay đổi
				if(!handover.getPorderid_link().equals(_handover.getPorderid_link())) {
					// Xoá HandoverProduct
					List<HandoverProduct> handoverProducts = handoverProductService.getByHandoverId(handover.getId());
					for(HandoverProduct product : handoverProducts) {
						handoverProductService.deleteById(product.getId());
					}
					// Xoá HandoverSKU
					List<HandoverSKU> handoverSKUs = handoverSkuService.getByHandoverId(handover.getId());
					for(HandoverSKU handoverSKU : handoverSKUs) {
						handoverSkuService.deleteById(handoverSKU.getId());
					}
				}
				handover = handoverService.save(handover);
			}
			
			response.data = handover;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_create_response>(response,HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<Handover_create_response> delete(@RequestBody Handover_delete_request entity,HttpServletRequest request ) {
		Handover_create_response response = new Handover_create_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long id = entity.id;
			// Xoá sku
			List<HandoverSKU> listSku = handoverSkuService.getByHandoverId(id);
			for(HandoverSKU sku : listSku) {
				handoverSkuService.deleteById(sku.getId());
			}
			// Xoá product
			List<HandoverProduct> listProduct = handoverProductService.getByHandoverId(id);
			for(HandoverProduct product : listProduct) {
				handoverProductService.deleteById(product.getId());
			}
			// Xoá handover
			handoverService.deleteById(id);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<Handover_getone_response> Getone(@RequestBody Handover_getone_request entity,HttpServletRequest request ) {
		Handover_getone_response response = new Handover_getone_response();
		try {
			
			response.data = handoverService.findOne(entity.id);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_getone_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_getone_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getbytype",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> Getbytype(@RequestBody Handover_getbytype_request entity,HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			Integer in_out = entity.in_out;
			if(in_out == 0) { // nhập
				response.data = handoverService.getByType(entity.handovertypeid_link, 1);
			}
			if(in_out == 1) { // xuất
				response.data = handoverService.getByType(entity.handovertypeid_link);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/setstatus",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> setStatus(@RequestBody Handover_setstatus_request entity,HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			Handover handover = handoverService.findOne(entity.handoverid_link);
			if(entity.approver_userid_link != 0) {
				System.out.println("handover != 0");
				handover.setApprover_userid_link(entity.approver_userid_link);
			}
			if(entity.receiver_userid_link != 0) {
				System.out.println("receiver != 0");
				handover.setReceiver_userid_link(entity.receiver_userid_link);
			}
			handover.setStatus(entity.status);
			handoverService.save(handover);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/userconfirm",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> userConfirm(@RequestBody Handover_userconfirm_request entity,HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			
			
//			Handover handover = handoverService.findOne(entity.handoverid_link);
//			handover.setStatus(2);
//			handoverService.save(handover);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}
	}
}