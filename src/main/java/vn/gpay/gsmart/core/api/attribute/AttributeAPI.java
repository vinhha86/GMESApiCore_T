package vn.gpay.gsmart.core.api.attribute;

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

import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.attribute.IAttributeService;
import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeService;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/attribute")
public class AttributeAPI {
	@Autowired IAttributeService attService;
	@Autowired IAttributeValueService attvalueService;
	@Autowired IProductAttributeService pavService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<Attribute_getall_response> Attribute_GetAll(HttpServletRequest request ) {
		Attribute_getall_response response = new Attribute_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			response.data = attService.getList_byorgid_link(user.getOrgid_link());
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Attribute_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<Attribute_getall_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/notinproduct",method = RequestMethod.POST)
	public ResponseEntity<Attribute_getall_response> Attribute_NotinProduct(HttpServletRequest request, @RequestBody Attribute_Delete_Request entity ) {
		Attribute_getall_response response = new Attribute_getall_response();
		List<ProductAttributeValue> lstpav = pavService.getall_byProductId(entity.id);
		
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			response.data = attService.getList_byorgid_link(user.getOrgid_link());
			for(int i =0; i< response.data.size(); i++) {
				Attribute attribute = response.data.get(i);
				for (ProductAttributeValue productAttributeValue : lstpav) {
					if(productAttributeValue.getAttributeid_link() == attribute.getId()) {
						response.data.remove(attribute);
						i--;
						break;
					}
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Attribute_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<Attribute_getall_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/notin_pcontractproduct",method = RequestMethod.POST)
	public ResponseEntity<Attribute_getall_response> Attribute_NotinPcontractProduct(HttpServletRequest request, @RequestBody Attribute_notin_pcontractproduct_request entity ) {
		Attribute_getall_response response = new Attribute_getall_response();
		
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long orgrootid_link = user.getRootorgid_link();
			
			List<Attribute> lstpav = attService.getlist_notin_pcontractproduct(pcontractid_link, productid_link, orgrootid_link);
			
			response.data = lstpav;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Attribute_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<Attribute_getall_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/attribute_create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeCreate(@RequestBody Attribute_Create_Request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Attribute attribute = entity.data;
			if(attribute.getId()==null || attribute.getId()==0) {
				attribute.setOrgrootid_link(user.getRootorgid_link());
				attribute.setUsercreateid_link(user.getId());
				attribute.setTimecreate(new Date());
			}else {
				Attribute _old =  attService.findOne(attribute.getId());
				attribute.setOrgrootid_link(_old.getOrgrootid_link());
				attribute.setUsercreateid_link(_old.getUsercreateid_link());
				attribute.setTimecreate(_old.getTimecreate());
			}
			attService.save(attribute);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/attribute_delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeDelete(@RequestBody Attribute_Delete_Request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			attService.deleteById(entity.id);
			List<Attributevalue> listvalue = attvalueService.getlist_byidAttribute(entity.id);
			for (Attributevalue attributevalue : listvalue) {
				attvalueService.delete(attributevalue);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}
}
