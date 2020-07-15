package vn.gpay.gsmart.core.api.pcontract_po_shipping;

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
import vn.gpay.gsmart.core.packingtype.IPackingTypeService;
import vn.gpay.gsmart.core.packingtype.PackingType;
import vn.gpay.gsmart.core.pcontract_po_shipping.IPContract_PO_ShippingService;
import vn.gpay.gsmart.core.pcontract_po_shipping.IPContract_PO_Shipping_DService;
import vn.gpay.gsmart.core.pcontract_po_shipping.PContract_PO_Shipping;
import vn.gpay.gsmart.core.pcontract_po_shipping.PContract_PO_Shipping_D;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/po_shipping")
public class PO_ShippingAPI {
	@Autowired IPContract_PO_ShippingService PO_ShippingService;
	@Autowired IPContract_PO_Shipping_DService PO_Shipping_DService;
	@Autowired IPackingTypeService packingtypeService;
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<PO_Shipping_create_response> Create(@RequestBody PO_Shipping_create_request entity,HttpServletRequest request ) {
		PO_Shipping_create_response response = new PO_Shipping_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long usercreatedid_link = user.getId();
			
			PContract_PO_Shipping po_shipping = entity.data;
			if( po_shipping.getId() == null) {
				po_shipping.setOrgrootid_link(orgrootid_link);
				po_shipping.setUsercreatedid_link(usercreatedid_link);
				po_shipping.setTimecreate(new Date());
			}
//			else {
//				PContract_PO pcontract_po_old = pcontract_POService.findOne(pcontract_po.getId());
//				pcontract_po.setOrgrootid_link(pcontract_po_old.getOrgrootid_link());
//				pcontract_po.setUsercreatedid_link(pcontract_po_old.getUsercreatedid_link());
//				pcontract_po.setDatecreated(pcontract_po_old.getDatecreated());
//			}
			po_shipping = PO_ShippingService.save(po_shipping);
			
			long pcontract_po_shippingid_link = po_shipping.getId();
			//Cập nhật lại giá

			//Xóa list cũ
			List<PContract_PO_Shipping_D> lst_shipping_d_old = PO_Shipping_DService.getByShippingID(pcontract_po_shippingid_link);
			for(PContract_PO_Shipping_D shipping_d : lst_shipping_d_old) {
				PO_Shipping_DService.delete(shipping_d);
			}

			//them list moi
			List<PContract_PO_Shipping_D> list_shipping_d  = entity.data.getShipping_d();
			for(PContract_PO_Shipping_D shipping_d : list_shipping_d) {
				shipping_d.setId(null);
				shipping_d.setPcontract_po_shippingid_link(pcontract_po_shippingid_link);
				shipping_d.setOrgrootid_link(orgrootid_link);
				PO_Shipping_DService.save(shipping_d);
			}
			
			//Response to Client
			response.id = po_shipping.getId();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));	
			
			return new ResponseEntity<PO_Shipping_create_response>(response, HttpStatus.OK);
			
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PO_Shipping_create_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getbypo",method = RequestMethod.POST)
	public ResponseEntity<PO_Shipping_getbypo_response> getByPO(@RequestBody PO_Shipping_getbypo_request entity,HttpServletRequest request ) {
		PO_Shipping_getbypo_response response = new PO_Shipping_getbypo_response();
		try {
			List<PContract_PO_Shipping> po_shipping = PO_ShippingService.getByPOID(entity.pcontract_poid_link);
			for(PContract_PO_Shipping theShipping: po_shipping){
				if (theShipping.getPackingnotice().length() > 0){
					String packingcodelist = "";
					String[] array = theShipping.getPackingnotice().split(";");
					for(int i=0; i<array.length; i++){
						PackingType thepackingtype = packingtypeService.findOne(Long.parseLong(array[i]));
						packingcodelist = packingcodelist + thepackingtype.getCode() + ";";
					}
					theShipping.setPackingnoticecode(packingcodelist);
				}
			}
			response.data = po_shipping;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PO_Shipping_getbypo_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PO_Shipping_getbypo_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	

	@RequestMapping(value = "/getone",method = RequestMethod.POST)
	public ResponseEntity<PO_Shipping_getone_response> getOne(@RequestBody PO_Shipping_getone_request entity,HttpServletRequest request ) {
		PO_Shipping_getone_response response = new PO_Shipping_getone_response();
		try {
			
			response.data = PO_ShippingService.findOne(entity.id); 
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PO_Shipping_getone_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PO_Shipping_getone_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Delete(@RequestBody PO_Shipping_getone_request entity
			,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			
			PContract_PO_Shipping thePO_Shipping = PO_ShippingService.findOne(entity.id);
			if (null != thePO_Shipping){
				for(PContract_PO_Shipping_D the_D: thePO_Shipping.getShipping_d()){
					PO_Shipping_DService.delete(the_D);
				}
				PO_ShippingService.delete(thePO_Shipping);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	    
	}
}
