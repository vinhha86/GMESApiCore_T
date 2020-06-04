package vn.gpay.gsmart.core.api.attributevalue;

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

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/attributevalue")
public class AttributeValueAPI {
	@Autowired IAttributeValueService attValueService;
	
	@RequestMapping(value = "/getbyidattribute",method = RequestMethod.POST)
	public ResponseEntity<AttributeValue_getlist_byId_Response> AttributeValue_Get_ById(@RequestBody AttributeValue_getlist_byId_Request entity,HttpServletRequest request ) {
		AttributeValue_getlist_byId_Response response = new AttributeValue_getlist_byId_Response();
		try {
			//GPayUserDetail user = (GPayUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			response.data = attValueService.getlist_byidAttribute(entity.id);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<AttributeValue_getlist_byId_Response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<AttributeValue_getlist_byId_Response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/notin_pcontract_atrtibute",method = RequestMethod.POST)
	public ResponseEntity<AttributeValue_getlist_byId_Response> AttributeValue_NotIn_Pcontract_attribute
	(@RequestBody AttributeValue_getlist_notin_pcontractatt_request entity,HttpServletRequest request ) {
		AttributeValue_getlist_byId_Response response = new AttributeValue_getlist_byId_Response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
			response.data = attValueService.getlistid_notin_pcontract_attribute(orgrootid_link, pcontractid_link, productid_link, attributeid_link);
			
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<AttributeValue_getlist_byId_Response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<AttributeValue_getlist_byId_Response>(response,HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/attributevalue_create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValueCreate(@RequestBody Attributevalue_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Attributevalue attvalue = entity.data;
			if(attvalue.getId()==null || attvalue.getId()==0) {
				attvalue.setOrgrootid_link(user.getRootorgid_link());
				attvalue.setUsercreateid_link(user.getId());
				attvalue.setTimecreate(new Date());
			}else {
				Attributevalue value_old =  attValueService.findOne(attvalue.getId());
				attvalue.setOrgrootid_link(value_old.getOrgrootid_link());
				attvalue.setUsercreateid_link(value_old.getUsercreateid_link());
				attvalue.setTimecreate(value_old.getTimecreate());
			}
			attValueService.save(attvalue);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
		@RequestMapping(value = "/attributevalue_delete",method = RequestMethod.POST)
		public ResponseEntity<ResponseBase> AttributeValueDelete(@RequestBody Attributevalue_delete_request entity,HttpServletRequest request ) {
			ResponseBase response = new ResponseBase();
			try {
				attValueService.deleteById(entity.id);
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}catch (Exception e) {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
			    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
			}
	}
}
