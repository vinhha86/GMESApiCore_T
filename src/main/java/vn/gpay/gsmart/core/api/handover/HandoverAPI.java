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
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_Service;
import vn.gpay.gsmart.core.porder_grant.POrderGrant;
import vn.gpay.gsmart.core.porderprocessing.IPOrderProcessing_Service;
import vn.gpay.gsmart.core.porderprocessing.POrderProcessing;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.GPAYDateFormat;
import vn.gpay.gsmart.core.utils.HandOverType;
import vn.gpay.gsmart.core.utils.POrderStatus;
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
	@Autowired IPOrderProcessing_Service porderProcessingService;
	@Autowired IPOrderGrant_Service porderGrantService;
	
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
						// Xu???t t??? c???t l??n chuy???n : CL
						if(type.equals(1L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("CL_" + porder.getOrdercode()));
						}
						// Xu???t t??? c???t l??n in th??u : CPR
						if(type.equals(2L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("CPR_" + porder.getOrdercode()));
						}
						// Xu???t t??? chuy???n l??n ho??n thi???n : LP
						if(type.equals(4L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("LP_" + porder.getOrdercode()));
						}
						// Xu???t t??? chuy???n l??n in th??u : LPR
						if(type.equals(5L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("LPR_" + porder.getOrdercode()));
						}
						// Xu???t t??? ho??n thi???n l??n kho TP : PS
						if(type.equals(9L)) {
							handover.setHandover_code(handoverAutoIdService.getLastID("PS_" + porder.getOrdercode()));
						}
					}else {
						handover.setHandover_code(handoverAutoIdService.getLastID("UNKNOWN"));
					}
				}else {
					// check existed
					String handover_code = handover.getHandover_code();
					List<Handover> lstcheck = handoverService.getByHandoverCode(handover_code);
					if(lstcheck.size() > 0) {
						response.setRespcode(ResponseMessage.KEY_RC_BAD_REQUEST);
						response.setMessage("M?? ???? t???n t???i trong h??? th???ng!");
						return new ResponseEntity<Handover_create_response>(response, HttpStatus.BAD_REQUEST);
					}
				}
				
				Date date = new Date();
				Integer total = 0;
				Integer totalCheck = 0;
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
					
					if(handoverProduct.getTotalpackage() != null)
						total+=handoverProduct.getTotalpackage();
					if(handoverProduct.getTotalpackagecheck() != null)
						totalCheck+=handoverProduct.getTotalpackagecheck();
					
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
				handover.setTotalpackage(total);
				handover.setTotalpackagecheck(totalCheck);
				handover = handoverService.save(handover);
			}else {
				// update
				Date date = new Date();
				Integer total = 0;
				Integer totalCheck = 0;
				
				if(handover.getHandovertypeid_link().equals(9L)) { // n???u type l?? pack to stock
					// chia ??i???u ki???n v?? pack to stock kh??ng c?? porderid_link
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
						
						if(handoverProduct.getTotalpackage() != null)
							total+=handoverProduct.getTotalpackage();
						if(handoverProduct.getTotalpackagecheck() != null)
							totalCheck+=handoverProduct.getTotalpackagecheck();
						
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
					handover.setTotalpackage(total);
					handover.setTotalpackagecheck(totalCheck);
					handover = handoverService.save(handover);
				}else { // type c??n l???i
					Handover _handover =  handoverService.findOne(handover.getId());
					handover.setOrgrootid_link(_handover.getOrgrootid_link());
					handover.setUsercreateid_link(_handover.getUsercreateid_link());
					handover.setTimecreate(_handover.getTimecreate());
					handover.setLastuserupdateid_link(user.getId());
					handover.setLasttimeupdate(date);
					// n???u porder thay ?????i
					if(!handover.getPorderid_link().equals(_handover.getPorderid_link())) {
						// Xo?? HandoverProduct
						List<HandoverProduct> listHandoverProducts = handoverProductService.getByHandoverId(handover.getId());
						for(HandoverProduct product : listHandoverProducts) {
							handoverProductService.deleteById(product.getId());
						}
						// Xo?? HandoverSKU
						List<HandoverSKU> handoverSKUs = handoverSkuService.getByHandoverId(handover.getId());
						for(HandoverSKU handoverSKU : handoverSKUs) {
							handoverSkuService.deleteById(handoverSKU.getId());
						}
					}
					
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
						
						if(handoverProduct.getTotalpackage() != null)
							total+=handoverProduct.getTotalpackage();
						if(handoverProduct.getTotalpackagecheck() != null)
							totalCheck+=handoverProduct.getTotalpackagecheck();
						
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
				}
				handover.setTotalpackage(total);
				handover.setTotalpackagecheck(totalCheck);
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
				response.setMessage("Phi???u ???? ???????c b??n nh???n x??c nh???n");
				return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
			}
			
			Long id = entity.id;
			// Xo?? sku
			List<HandoverSKU> listSku = handoverSkuService.getByHandoverId(id);
			for(HandoverSKU sku : listSku) {
				handoverSkuService.deleteById(sku.getId());
			}
			// Xo?? product
			List<HandoverProduct> listProduct = handoverProductService.getByHandoverId(id);
			for(HandoverProduct product : listProduct) {
				handoverProductService.deleteById(product.getId());
			}
			// Xo?? handover
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
			if(in_out == 0) { // nh???p
				response.data = handoverService.getByType(entity.handovertypeid_link, 1);
			}
			if(in_out == 1) { // xu???t
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
			String viewId = entity.viewId;
			
//			System.out.println("viewId: " + viewId);
			//
			response.data = new ArrayList<>();
			List<Handover> result = new ArrayList<>();
			//
//			if(org.getOrgtypeid_link() == 1) { // tr??? s???
//			}
//			if(org.getOrgtypeid_link() == 13) { // x?????ng
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
				// check user org_grant_id_link
				// handover_cut_toline, handover_line_topack, handover_line_toprint, handover_cut_toprint, handover_pack_tostock
				// handover_line_fromcut, handover_pack_fromline
				if(org_grant_id_link != null) {
					switch(viewId) {
						// orgid_from_link
						case "handover_cut_toline":
						case "handover_line_topack":
						case "handover_line_toprint":
						case "handover_cut_toprint":
						case "handover_pack_tostock":
							if(handover.getOrgid_from_link() != null) {
								if(!org_grant_id_link.equals(handover.getOrgid_from_link())) {
									continue;
								}
							}
							break;
						// orgid_to_link
						case "handover_line_fromcut":
						case "handover_pack_fromline":
							if(handover.getOrgid_to_link() != null) {
								if(!org_grant_id_link.equals(handover.getOrgid_to_link())) {
									continue;
								}
							}
							break;
						default:
							break;
					}
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
	public ResponseEntity<Handover_create_response> setStatus(@RequestBody Handover_setstatus_request entity,HttpServletRequest request ) {
		Handover_create_response response = new Handover_create_response();
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			Date date = new Date();
//			Handover handover = handoverService.findOne(entity.handoverid_link);
			Handover handover = entity.data;
			if(handover != null) {
				List<HandoverProduct> handoverProducts = handover.getHandoverProducts();
				
				Integer total = 0;
				Integer totalCheck = 0;
				
				if(handover.getHandovertypeid_link().equals(9L)) { // n???u type l?? pack to stock
					// chia ??i???u ki???n v?? pack to stock kh??ng c?? porderid_link
					handover.setOrgrootid_link(user.getRootorgid_link());
					handover.setUsercreateid_link(user.getId());
					handover.setTimecreate(date);
					handover.setLastuserupdateid_link(user.getId());
					handover.setLasttimeupdate(date);
	//				handover.setTotalpackage(handoverProduct.getTotalpackage());
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
						
						if(handoverProduct.getTotalpackage() != null)
							total+=handoverProduct.getTotalpackage();
						if(handoverProduct.getTotalpackagecheck() != null)
							totalCheck+=handoverProduct.getTotalpackagecheck();
						
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
					handover.setTotalpackage(total);
					handover.setTotalpackagecheck(totalCheck);
					handover = handoverService.save(handover);
				}else { // type c??n l???i
					Handover _handover =  handoverService.findOne(handover.getId());
					handover.setOrgrootid_link(_handover.getOrgrootid_link());
					handover.setUsercreateid_link(_handover.getUsercreateid_link());
					handover.setTimecreate(_handover.getTimecreate());
					handover.setLastuserupdateid_link(user.getId());
					handover.setLasttimeupdate(date);
					// n???u porder thay ?????i
					if(!handover.getPorderid_link().equals(_handover.getPorderid_link())) {
						// Xo?? HandoverProduct trong db
						List<HandoverProduct> listHandoverProducts = handoverProductService.getByHandoverId(handover.getId());
						for(HandoverProduct product : listHandoverProducts) {
							handoverProductService.deleteById(product.getId());
						}
						// Xo?? HandoverSKU trong db
						List<HandoverSKU> handoverSKUs = handoverSkuService.getByHandoverId(handover.getId());
						for(HandoverSKU handoverSKU : handoverSKUs) {
							handoverSkuService.deleteById(handoverSKU.getId());
						}
					}
					
					// luu handoverProduct, sku tu obj request
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
						
						if(handoverProduct.getTotalpackage() != null)
							total+=handoverProduct.getTotalpackage();
						if(handoverProduct.getTotalpackagecheck() != null)
							totalCheck+=handoverProduct.getTotalpackagecheck();
						
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
				}
				handover.setTotalpackage(total);
				handover.setTotalpackagecheck(totalCheck);
				handover = handoverService.save(handover);
			}else {
				// ????? test, sau khi chuy???n t???t c??? c??c view handover th??nh view ri??ng th?? b??? ph???n n??y ??i
				// v?? view c?? ch??? g???i id, view m???i g???i obj
				handover = handoverService.findOne(entity.handoverid_link);
			}
			
			// set new status
			// old info
			Integer oldStatus = handover.getStatus();
			Long old_approver_userid_link = handover.getApprover_userid_link();
			Long old_receiver_userid_link = handover.getReceiver_userid_link();
			Date old_receive_date = handover.getReceive_date();
			Date old_lasttimeupdate = handover.getLasttimeupdate();
			Long old_lastuserupdateid_link = handover.getLastuserupdateid_link();
			Long old_amount_time_to_receive = handover.getAmount_time_to_receive();
			
			if(entity.approver_userid_link != 0) { 
				handover.setApprover_userid_link(entity.approver_userid_link);
			}
			if(entity.receiver_userid_link != 0) { 
				handover.setReceiver_userid_link(entity.receiver_userid_link);
				handover.setReceive_date(date);
				
				Long amount_time_to_receive = date.getTime() - handover.getHandover_date().getTime();
				handover.setAmount_time_to_receive(amount_time_to_receive);
			}
			handover.setLasttimeupdate(date);
			handover.setLastuserupdateid_link(user.getId());
			handover.setStatus(entity.status);
			
			
			// status = 0 // ch??a duy???t
			// status = 1 // ???? duy???t
			// status = 2 // ???? nh???n
			
			// n???u x??c nh???n nh???n t???i n??i nh???n, thay ?????i trong porder processing
			if(handover.getStatus() == 2 && oldStatus == 1) {
				// x??c nh???n nh???n, update porder processing
				Long handovertypeid_link = handover.getHandovertypeid_link();
				Long granttoorgid_link = 0L;
				Long porderid_link = handover.getPorderid_link();
				Date receive_date = handover.getReceive_date(); // ng??y v??o chuy???n
				Integer sumProduct = 0; // sl th??m v??o chuy???n
				String action = "X??c nh???n";
				if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE) || handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) {
					// 1: cut to line
					// 4: line to packstocked
					if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE)) {
						granttoorgid_link = handover.getOrgid_to_link();
					}else if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) {
						granttoorgid_link = handover.getOrgid_from_link();
					}
					List<HandoverProduct> listHandoverProduct = handover.getHandoverProducts();
					for(HandoverProduct handoverProduct : listHandoverProduct) {
						sumProduct += handoverProduct.getTotalpackagecheck();
					}
					if(sumProduct > 0) {
						String result = updatePOrderProcessing(
								handovertypeid_link, 
								granttoorgid_link, porderid_link, 
								receive_date, sumProduct, action);
						if(
								result.equals("Kh??ng t???n t???i POrderProcessing") ||
								result.equals("T???ng SL v??o chuy???n kh??ng ???????c v?????t qu?? SL ????n") ||
								result.equals("T???ng SL nh???p ho??n thi???n kh??ng ???????c v?????t qu?? t???ng SL v??o chuy???n")
						) {
							handover.setStatus(oldStatus);
							handover.setApprover_userid_link(old_approver_userid_link);
							handover.setReceiver_userid_link(old_receiver_userid_link);
							handover.setReceive_date(old_receive_date);
							handover.setLasttimeupdate(old_lasttimeupdate);
							handover.setLastuserupdateid_link(old_lastuserupdateid_link);
							handover.setAmount_time_to_receive(old_amount_time_to_receive);
							handover.setTotalpackagecheck(0);
							handover = handoverService.save(handover);
							
							response.data = handover;
							response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
							response.setMessage(result);
							return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
						}
					}
				}
			}

			handover = handoverService.save(handover);
			response.data = handover;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_create_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/cancelconfirm",method = RequestMethod.POST)
	public ResponseEntity<Handover_getall_response> cancelConfirm(@RequestBody Handover_getone_request entity,HttpServletRequest request ) {
		Handover_getall_response response = new Handover_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Handover handover = handoverService.findOne(entity.id);
			Date date = new Date();
			
			if(handover.getStatus() != 2) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Phi???u ch??a ???????c x??c nh???n");
				return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
			}
			
			// update POrderProcessing

			Long handovertypeid_link = handover.getHandovertypeid_link();
			Long granttoorgid_link = 0L;
			Long porderid_link = handover.getPorderid_link();
			Date receive_date = handover.getReceive_date(); // ng??y v??o
			Integer sumProduct = 0; // sl th??m v??o
			String action = "Hu???";
			
			if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE) || handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) {
				// 1: cut to line
				// 4: line to packstocked
				if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE)) {
					granttoorgid_link = handover.getOrgid_to_link();
				}else if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) {
					granttoorgid_link = handover.getOrgid_from_link();
				}
				List<HandoverProduct> listHandoverProduct = handover.getHandoverProducts();
				for(HandoverProduct handoverProduct : listHandoverProduct) {
					sumProduct += handoverProduct.getTotalpackagecheck();
				}
				if(sumProduct > 0) {
					String result = updatePOrderProcessing(
							handovertypeid_link, 
							granttoorgid_link, porderid_link, 
							receive_date, sumProduct, action
							);
					if(result.equals("Kh??ng t???n t???i POrderProcessing")) {
						response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
						response.setMessage(result);
						return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
					}
				}
			}
			
			// luu handover status
			handover.setStatus(1);
			handover.setReceiver_userid_link(null);
			handover.setReceive_date(null);
			handover.setLasttimeupdate(date);
			handover.setLastuserupdateid_link(user.getId());
			handover.setAmount_time_to_receive(null);
			handover = handoverService.save(handover);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<Handover_getall_response>(response,HttpStatus.OK);
		}
	}
	
	public String updatePOrderProcessing(
			Long handovertypeid_link,
			Long granttoorgid_link, Long porderid_link, 
			Date receive_date, Integer sumProduct,
			String action
			) {
		
		System.out.println("updatePOrderProcessing " + sumProduct);
		System.out.println("handovertypeid_link " + handovertypeid_link);
		
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Long rootorgid_link = user.getRootorgid_link();
		// t??m xem l???nh n??y, t??? chuy???n n??y, ng??y n??y c?? POrderProcessinng kh??ng
		receive_date = GPAYDateFormat.atStartOfDay(receive_date);
		List<POrderProcessing> listPorderProcessing = porderProcessingService.getByPOrderAndLineAndDate(
				porderid_link, granttoorgid_link, receive_date
				);
		
		POrderProcessing pprocess;
		if(listPorderProcessing.size() > 0) {
			// C?? th?? check ng??y, n???u tr??ng th?? s???a, ko tr??ng th?? t???o m???i v?? set th??ng tin d???a v??o ng??y tr?????c
			pprocess = listPorderProcessing.get(0);
			if(receive_date.equals(pprocess.getProcessingdate())) {
				// tr??ng ng??y, th??m v?? t??nh to??n amountinput
				if(action.equals("X??c nh???n")) {
			        // X??c nh???n l??c n??o c??ng l?? ng??y hi???n t???i n??n ko c???n t??nh l???i c??c ng??y sau
					if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE)) { // cut to line 
						// ki???m tra amountinputsum c?? > grantamount c???a pordergrant kh??ng
						Integer amountinput = null==pprocess.getAmountinput()?0:pprocess.getAmountinput() + sumProduct;
						Integer amountinputsum = (null==pprocess.getAmountinputsumprev()?0:pprocess.getAmountinputsumprev())
								+ amountinput;
						Integer grantamount = null==pprocess.getGrantamount()?0:pprocess.getGrantamount();
						
						if(amountinputsum <= grantamount) {
							pprocess.setAmountinput(amountinput);
			    			pprocess.setAmountinputsum(amountinputsum);
						}else {
							return "T???ng SL v??o chuy???n kh??ng ???????c v?????t qu?? SL ????n";
						}
						
					}else if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) { // line to packstocked
						// ki???m tra amountpackstockedsum c?? > amountinputsum kh??ng
						Integer amountpackstocked = null==pprocess.getAmountpackstocked()?0:pprocess.getAmountpackstocked() + sumProduct;
						Integer amountpackstockedsum = (null==pprocess.getAmountpackstockedsumprev()?0:pprocess.getAmountpackstockedsumprev())
								+ amountpackstocked;
						Integer amountinputsum = null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum();
						
						if(amountpackstockedsum <= amountinputsum) {
							pprocess.setAmountpackstocked(amountpackstocked);
			    			pprocess.setAmountpackstockedsum(amountpackstockedsum);
						}else {
							return "T???ng SL nh???p ho??n thi???n kh??ng ???????c v?????t qu?? t???ng SL v??o chuy???n";
						}
					}
				}else if(action.equals("Hu???")) {
					// Hu??? l?? sau x??c nh???n n??n l??c n??o c??ng t???n t???i POrderProcessing
					if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE)) { // cut to line
						pprocess.setAmountinput(pprocess.getAmountinput() - sumProduct);
		    			pprocess.setAmountinputsum((null==pprocess.getAmountinputsumprev()?0:pprocess.getAmountinputsumprev()) 
		    					+ (null==pprocess.getAmountinput()?0:pprocess.getAmountinput()));
					}else if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) { // line to packstocked
						pprocess.setAmountpackstocked(pprocess.getAmountpackstocked() - sumProduct);
		    			pprocess.setAmountpackstockedsum((null==pprocess.getAmountpackstockedsumprev()?0:pprocess.getAmountpackstockedsumprev()) 
		    					+ (null==pprocess.getAmountpackstocked()?0:pprocess.getAmountpackstocked()));
					}
	    			
	    			// T??nh l???i sl c??c ng??y sau
	    			// C???ng d???n trong tr?????ng h???p s???a s??? c???a ng??y tr?????c ng??y hi???n t???i
			        // Update Amount SUM of following days. In case update amount of prev day

			        POrderGrant porder_grant = porderGrantService.findOne(pprocess.getPordergrantid_link());
	    			
			        if (GPAYDateFormat.atStartOfDay(receive_date).before(GPAYDateFormat.atStartOfDay(new Date()))){
				        List<POrderProcessing> pprocessListAfter = porderProcessingService.getAfterDate(porderid_link, pprocess.getPordergrantid_link(), receive_date);
				        
				        int iAmountCutSum = null==pprocess.getAmountcutsum()?0:pprocess.getAmountcutsum();
				        int iAmountInputSum = null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum();
				        int iAmountOuputSum = null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum();
				        int iAmountErrorSum = null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum();
				        int iAmountKcsSum = null==pprocess.getAmountkcssum()?0:pprocess.getAmountkcssum();
				        int iAmountPackedSum = null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum();
				        int iAmountPackStockedSum = null==pprocess.getAmountpackstockedsum()?0:pprocess.getAmountpackstockedsum();
				        int iAmountStockedSum = null==pprocess.getAmountstockedsum()?0:pprocess.getAmountstockedsum();
				        int iLastStatus = pprocess.getStatus();
				        
				        for(POrderProcessing pprocessAfter: pprocessListAfter){
				        	pprocessAfter.setAmountcutsumprev(iAmountCutSum);
				        	pprocessAfter.setAmountcutsum(iAmountCutSum + (null==pprocessAfter.getAmountcut()?0:pprocessAfter.getAmountcut()));
				        	
				        	pprocessAfter.setAmountinputsumprev(iAmountInputSum);
				        	pprocessAfter.setAmountinputsum(iAmountInputSum + (null==pprocessAfter.getAmountinput()?0:pprocessAfter.getAmountinput()));
				        	
				        	pprocessAfter.setAmountoutputsumprev(iAmountOuputSum);
				        	pprocessAfter.setAmountoutputsum(iAmountOuputSum + (null==pprocessAfter.getAmountoutput()?0:pprocessAfter.getAmountoutput()));
				        	
				        	pprocessAfter.setAmounterrorsumprev(iAmountErrorSum);
				        	pprocessAfter.setAmounterrorsum(iAmountErrorSum + (null==pprocessAfter.getAmounterror()?0:pprocessAfter.getAmounterror()));
				        	
				        	pprocessAfter.setAmountkcssumprev(iAmountKcsSum);
				        	pprocessAfter.setAmountkcssum(iAmountKcsSum + (null==pprocessAfter.getAmountkcssum()?0:pprocessAfter.getAmountkcssum()));
				        	
				        	pprocessAfter.setAmountpackedsumprev(iAmountPackedSum);
				        	pprocessAfter.setAmountpackedsum(iAmountPackedSum + (null==pprocessAfter.getAmountpacked()?0:pprocessAfter.getAmountpacked()));
				        	
				        	pprocessAfter.setAmountpackstockedsumprev(iAmountPackStockedSum);
				        	pprocessAfter.setAmountpackstockedsum(iAmountPackStockedSum + (null==pprocessAfter.getAmountpackstocked()?0:pprocessAfter.getAmountpackstocked()));
				        	
				        	pprocessAfter.setAmountstockedsumprev(iAmountStockedSum);
				        	pprocessAfter.setAmountstockedsum(iAmountStockedSum + (null==pprocessAfter.getAmountstocked()?0:pprocessAfter.getAmountstocked()));
				        	
					        if ((null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum()) > 0){
					        	if (((null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum()) 
					        			+ (null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum()))  
					        			< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
					        		pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
					        	}
					        	else {
					        		if ((null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum()) 
					        				< (null==pprocessAfter.getAmountcutsum()||0==pprocessAfter.getAmountcutsum()?pprocessAfter.getTotalorder():pprocessAfter.getAmountcutsum())){
					        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_DONE);
					        		}
					        		else {
					        			pprocessAfter.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
					        		}
					        	}
					        }
					        
					        porderProcessingService.save(pprocessAfter);
				        	
					        iAmountCutSum = null==pprocessAfter.getAmountcutsum()?0:pprocessAfter.getAmountcutsum();
					        iAmountInputSum = null==pprocessAfter.getAmountinputsum()?0:pprocessAfter.getAmountinputsum();
					        iAmountOuputSum = null==pprocessAfter.getAmountoutputsum()?0:pprocessAfter.getAmountoutputsum();
					        iAmountErrorSum = null==pprocessAfter.getAmounterrorsum()?0:pprocessAfter.getAmounterrorsum();
					        iAmountKcsSum = null==pprocessAfter.getAmountkcssum()?0:pprocessAfter.getAmountkcssum();
					        iAmountPackedSum = null==pprocessAfter.getAmountpackedsum()?0:pprocessAfter.getAmountpackedsum();
					        iAmountPackStockedSum = null==pprocessAfter.getAmountpackstockedsum()?0:pprocessAfter.getAmountpackstockedsum();
					        iAmountStockedSum = null==pprocessAfter.getAmountstockedsum()?0:pprocessAfter.getAmountstockedsum();
				        	
					        iLastStatus = pprocessAfter.getStatus();
				        }
				        
				        //Update status of Porder_Grant to last status of Processing
			        	porder_grant.setStatus(iLastStatus);
			        	porderGrantService.save(porder_grant);		        	
			        } else {
			        	porder_grant.setStatus(pprocess.getStatus());
			        	porderGrantService.save(porder_grant);	
			        }
	    			
				}
			}else {
				// ko tr??ng ng??y, t???o v?? t??nh to??n
				POrderProcessing temp = pprocess;
				pprocess = new POrderProcessing();
				
				pprocess.setId(null);
				pprocess.setOrgrootid_link(rootorgid_link); 
				pprocess.setProcessingdate(receive_date);
				pprocess.setPorderid_link(temp.getPorderid_link());
				pprocess.setPordergrantid_link(temp.getPordergrantid_link());
				pprocess.setGranttoorgid_link(temp.getGranttoorgid_link());
	        	
				pprocess.setAmountcut(temp.getAmountcut());
				pprocess.setAmountcutsumprev(temp.getAmountcutsum());
				pprocess.setAmountcutsum(temp.getAmountcutsum());
	
				if(temp.getAmountinputsum() == null) temp.setAmountinputsum(0);
				pprocess.setAmountinput(0);
				pprocess.setAmountinputsumprev(temp.getAmountinputsum());
				pprocess.setAmountinputsum(temp.getAmountinputsum());
		        
				pprocess.setAmountoutput(0);
				pprocess.setAmountoutputsumprev(temp.getAmountoutputsum());
				pprocess.setAmountoutputsum(temp.getAmountoutputsum());
		        
				pprocess.setAmounterror(0);
				pprocess.setAmounterrorsumprev(temp.getAmounterrorsum());
				pprocess.setAmounterrorsum(temp.getAmounterrorsum());
		        
				pprocess.setAmounttargetprev(temp.getAmounttarget());
				pprocess.setAmountkcsregprev(temp.getAmountkcsreg());
	        	
				pprocess.setAmountkcs(0);
				pprocess.setAmountkcssumprev(temp.getAmountkcssum());
				pprocess.setAmountkcssum(temp.getAmountkcssum());
		        
				pprocess.setAmountpacked(0);
				pprocess.setAmountpackedsumprev(temp.getAmountpackedsum());
				pprocess.setAmountpackedsum(temp.getAmountpackedsum());
		        
				if(temp.getAmountpackstockedsum() == null) temp.setAmountpackstockedsum(0);
				pprocess.setAmountpackstocked(0);
				pprocess.setAmountpackstockedsumprev(temp.getAmountpackstockedsum());
				pprocess.setAmountpackstockedsum(temp.getAmountpackstockedsum());
		        
				pprocess.setAmountstocked(0);
				pprocess.setAmountstockedsumprev(temp.getAmountstockedsum());
				pprocess.setAmountstockedsum(temp.getAmountstockedsum());
		        
//	        	pprocess.setOrdercode(porder_grant.getOrdercode());
				pprocess.setTotalorder(temp.getGrantamount());	  
	        	
				pprocess.setUsercreatedid_link(user.getId());
				pprocess.setTimecreated(new Date());
				
				if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_CUT_LINE)) {
					// ki???m tra amountinputsum c?? > grantamount c???a porder grant kh??ng
					Integer amountinput = sumProduct;
					Integer amountinputsum = temp.getAmountinputsum() + amountinput;
					Integer grantamount = null==temp.getGrantamount()?0:temp.getGrantamount();
					
					if(amountinputsum <= grantamount) {
		    			pprocess.setAmountinput(amountinput);
						pprocess.setAmountinputsumprev(temp.getAmountinputsum());
						pprocess.setAmountinputsum(amountinputsum);
					}else {
						return "T???ng SL v??o chuy???n kh??ng ???????c v?????t qu?? SL ????n";
					}
					
					
				}else if(handovertypeid_link.equals(HandOverType.HANDOVER_TYPE_LINE_PACK)) {
					// ki???m tra amountpackstockedsum c?? > amountinputsum kh??ng
					Integer amountpackstocked = sumProduct;
					Integer amountpackstockedsum = temp.getAmountpackstockedsum() + amountpackstocked;
					Integer amountinputsum = null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum();
					
					if(amountpackstockedsum <= amountinputsum) {
						pprocess.setAmountpackstocked(amountpackstocked);
						pprocess.setAmountpackstockedsumprev(temp.getAmountpackstockedsum());
						pprocess.setAmountpackstockedsum(amountpackstockedsum);
					}else {
						return "T???ng SL nh???p ho??n thi???n kh??ng ???????c v?????t qu?? t???ng SL v??o chuy???n";
					}
				}
			}
			//Update trang thai lenh tuong ung
	        if ((null==pprocess.getAmountinputsum()?0:pprocess.getAmountinputsum()) > 0){
	        	if (((null==pprocess.getAmountoutputsum()?0:pprocess.getAmountoutputsum()) 
	        			+ (null==pprocess.getAmounterrorsum()?0:pprocess.getAmounterrorsum()))  
	        			< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
	        		pprocess.setStatus(POrderStatus.PORDER_STATUS_RUNNING);
	        	}
	        	else {
	        		if ((null==pprocess.getAmountpackedsum()?0:pprocess.getAmountpackedsum()) 
	        				< (null==pprocess.getAmountcutsum()||0==pprocess.getAmountcutsum()?pprocess.getTotalorder():pprocess.getAmountcutsum())){
	        			pprocess.setStatus(POrderStatus.PORDER_STATUS_DONE);
	        		}
	        		else {
	        			pprocess.setStatus(POrderStatus.PORDER_STATUS_FINISHED);
	        		}
	        	}
	        }
	        porderProcessingService.save(pprocess);
		}else {
			return "Kh??ng t???n t???i POrderProcessing";
			// ko c??n t???n t???i porder processing g???c ???????c sinh ra khi k??o l???nh v??o chuy???n
		}
		return "OK";
		
	}
}
