package vn.gpay.gsmart.core.api.handover;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.org.Org;
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
	@Autowired IOrgService orgService;
	
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
//			HandoverProduct handoverProduct = entity.handoverProduct;
			List<HandoverProduct> handoverProducts = handover.getHandoverProducts(); //
			Long type = handover.getHandovertypeid_link();
			
			if(handover.getId()==null || handover.getId()==0) {
				// new
				if(handover.getHandover_code() == null || handover.getHandover_code().length() == 0) {
//					POrder porder = porderService.findOne(handover.getPorderid_link());
					if(handover.getPorderid_link() != null) {
						POrder porder = porderService.findOne(handover.getPorderid_link());
						// Xuất từ cắt lên chuyền : CL
						if(type.equals(1L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("CL_" + porder.getOrdercode()));
						}
						// Xuất từ cắt lên in thêu : CPR
						if(type.equals(2L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("CPR_" + porder.getOrdercode()));
						}
						// Xuất từ chuyền lên hoàn thiện : LP
						if(type.equals(4L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("LP_" + porder.getOrdercode()));
						}
						// Xuất từ chuyền lên in thêu : LPR
						if(type.equals(5L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("LPR_" + porder.getOrdercode()));
						}
					}else {
						// Xuất từ hoàn thiện lên kho TP : PS
						if(type.equals(9L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("PS"));
						}else {
							handover.setHandover_code(handoverAutoIdService.getLastID("UNKNOWN"));
						}
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
//				handover.setTotalpackage(handoverProduct.getTotalpackage());
				handover = handoverService.save(handover);
				
				// products
				for(HandoverProduct handoverProduct : handoverProducts) {
					List<HandoverSKU> handoverSKUs = handoverProduct.getHandoverSKUs();
					
					handoverProduct.setOrgrootid_link(user.getRootorgid_link());
					handoverProduct.setHandoverid_link(handover.getId());
					handoverProduct.setUsercreateid_link(user.getId());
					handoverProduct.setLastuserupdateid_link(user.getId());
					handoverProduct.setTimecreate(date);
					handoverProduct.setLasttimeupdate(date);
					handoverProduct = handoverProductService.save(handoverProduct);
					
					// skus
					for(HandoverSKU handoverSKU : handoverSKUs) {
						handoverSKU.setOrgrootid_link(user.getRootorgid_link());
						handoverSKU.setHandoverid_link(handover.getId());
						handoverSKU.setHandoverproductid_link(handoverProduct.getId());
						handoverSKU.setUsercreateid_link(user.getId());
						handoverSKU.setLastuserupdateid_link(user.getId());
						handoverSKU.setTimecreate(date);
						handoverSKU.setLasttimeupdate(date);
						handoverSkuService.save(handoverSKU);
					}
				}
			}else {
				// update
				Date date = new Date();
				
				if(handover.getHandovertypeid_link().equals(9L)) { // nếu type là pack to stock
					// chia điều kiện vì pack to stock không có porderid_link
					handover.setOrgrootid_link(user.getRootorgid_link());
					handover.setUsercreateid_link(user.getId());
					handover.setTimecreate(date);
					handover.setLastuserupdateid_link(user.getId());
					handover.setLasttimeupdate(date);
//					handover.setTotalpackage(handoverProduct.getTotalpackage());
					handover = handoverService.save(handover);
					
					for(HandoverProduct handoverProduct : handoverProducts) {
						List<HandoverSKU> handoverSKUs = handoverProduct.getHandoverSKUs();
						if(handoverProduct.getId() == null || handoverProduct.getId() == 0) {
							handoverProduct.setOrgrootid_link(user.getRootorgid_link());
							handoverProduct.setHandoverid_link(handover.getId());
							handoverProduct.setUsercreateid_link(user.getId());
							handoverProduct.setTimecreate(date);
						}else {
							handoverProduct.setLastuserupdateid_link(user.getId());
							handoverProduct.setLasttimeupdate(date);
						}
						handoverProduct = handoverProductService.save(handoverProduct);
						
						// skus
						for(HandoverSKU handoverSKU : handoverSKUs) {
							if(handoverSKU.getId() == null || handoverSKU.getId() == 0) {
								handoverSKU.setOrgrootid_link(user.getRootorgid_link());
								handoverSKU.setHandoverid_link(handover.getId());
								handoverSKU.setHandoverproductid_link(handoverProduct.getId());
								handoverSKU.setUsercreateid_link(user.getId());
								handoverSKU.setTimecreate(date);
							}else {
								handoverSKU.setLastuserupdateid_link(user.getId());
								handoverSKU.setLasttimeupdate(date);
							}
							handoverSkuService.save(handoverSKU);
						}
					}
				}else { // type còn lại
					Handover _handover =  handoverService.findOne(handover.getId());
					handover.setOrgrootid_link(_handover.getOrgrootid_link());
					handover.setUsercreateid_link(_handover.getUsercreateid_link());
					handover.setTimecreate(_handover.getTimecreate());
					handover.setLastuserupdateid_link(user.getId());
					handover.setLasttimeupdate(date);
					// nếu porder thay đổi
					if(!handover.getPorderid_link().equals(_handover.getPorderid_link())) {
						// Xoá HandoverProduct
						List<HandoverProduct> listHandoverProducts = handoverProductService.getByHandoverId(handover.getId());
						for(HandoverProduct product : listHandoverProducts) {
							handoverProductService.deleteById(product.getId());
						}
						// Xoá HandoverSKU
						List<HandoverSKU> handoverSKUs = handoverSkuService.getByHandoverId(handover.getId());
						for(HandoverSKU handoverSKU : handoverSKUs) {
							handoverSkuService.deleteById(handoverSKU.getId());
						}
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
			// check status
			Handover handover = handoverService.findOne(entity.id);
			if(handover.getStatus() == 2) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Phiếu đã được bên nhận xác nhận");
				return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
			}
			
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
	
	@RequestMapping(value = "/getbysearch",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> Getbysearch(@RequestBody Handover_getbysearch_request entity,HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long org_grant_id_link = user.getOrg_grant_id_link();
//			Org org = orgService.findOne(org_grant_id_link);
			//
			Long handovertypeid_link = entity.handovertypeid_link;
			Date handover_datefrom = entity.handover_datefrom;
			Date handover_dateto = entity.handover_dateto;
			Long orgid_from_link = entity.orgid_from_link;
			Long orgid_to_link = entity.orgid_to_link;
			List<Integer> status = entity.status;
			//
			response.data = new ArrayList<>();
			List<Handover> result = new ArrayList<>();
			//
//			if(org.getOrgtypeid_link() == 1) { // trụ sở
//			}
//			if(org.getOrgtypeid_link() == 13) { // xưởng
//			}
			if(status.size() == 0) {
				result = handoverService.getHandOverBySearch(
						handovertypeid_link, handover_datefrom, handover_dateto,
						orgid_from_link, orgid_to_link, null);
			}else {
				for(Integer num : status) {
					List<Handover> temp = handoverService.getHandOverBySearch(
							handovertypeid_link, handover_datefrom, handover_dateto,
							orgid_from_link, orgid_to_link, num);
					result.addAll(temp);
				}
			}
			
			if(entity.ordercode == null) entity.ordercode = "";
			
			for(Handover handover : result) {
				String ordercode = handover.getOrdercode().toLowerCase();
				String ordercode_req = entity.ordercode.toLowerCase();
				if(!ordercode.contains(ordercode_req)) {
					continue;
				}
				response.data.add(handover);
			}
			
			response.totalCount = response.data.size();
			
			PageRequest page = PageRequest.of(entity.page - 1, entity.limit);
			int start = (int) page.getOffset();
			int end = (start + page.getPageSize()) > response.data.size() ? response.data.size() : (start + page.getPageSize());
			Page<Handover> pageToReturn = new PageImpl<Handover>(response.data.subList(start, end), page, response.data.size()); 
			
			response.data = pageToReturn.getContent();
			
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
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Date date = new Date();
		try {
			Handover handover = handoverService.findOne(entity.handoverid_link);
			if(entity.approver_userid_link != 0) {
				handover.setApprover_userid_link(entity.approver_userid_link);
			}
			if(entity.receiver_userid_link != 0) {
				handover.setReceiver_userid_link(entity.receiver_userid_link);
				handover.setReceive_date(date);
			}
			handover.setLasttimeupdate(date);
			handover.setLastuserupdateid_link(user.getId());
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
	
	@RequestMapping(value = "/cancelconfirm",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> cancelConfirm(@RequestBody Handover_getone_request entity,HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Handover handover = handoverService.findOne(entity.id);
			if(handover.getStatus() != 2) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Phiếu chưa được xác nhận");
				return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
			}
			Date date = new Date();
			handover.setStatus(1);
			handover.setReceiver_userid_link(null);
			handover.setReceive_date(null);
			handover.setLasttimeupdate(date);
			handover.setLastuserupdateid_link(user.getId());
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
}
