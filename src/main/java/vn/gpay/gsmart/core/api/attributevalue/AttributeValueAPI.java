package vn.gpay.gsmart.core.api.attributevalue;

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
				attvalue.setIsdefault(false);
				attvalue.setTimecreate(new Date());
				attvalue.setSortvalue(attValueService.getMaxSortValue(attvalue.getAttributeid_link()));
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
	
	@RequestMapping(value = "/attributevalue_reorder",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValueReorder(@RequestBody Attributevalue_reorder_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
		
			for (Attributevalue attvalue:entity.data){
				Attributevalue Attr = attValueService.findOne(attvalue.getId());
				if (null != Attr){
					Attr.setSortvalue(attvalue.getSortvalue());
					attValueService.save(Attr);
				}
				
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/attributevalue_create_quick",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValueCreateQuick(@RequestBody Attributevalue_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Attributevalue attvalue = entity.data;
			
			String value = attvalue.getValue().trim();
			Long attributeid_link = attvalue.getAttributeid_link();
			List<Attributevalue> listAttributevalue = attValueService.getByValue(value, attributeid_link);
			if(listAttributevalue.size() > 0) {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Giá trị thuộc tính đã tồn tại");
				return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
			}
			
			attvalue.setValue(value);
			attvalue.setOrgrootid_link(user.getRootorgid_link());
			attvalue.setUsercreateid_link(user.getId());
			attvalue.setIsdefault(false);
			attvalue.setTimecreate(new Date());
			attvalue.setSortvalue(attValueService.getMaxSortValue(attvalue.getAttributeid_link()));
			attvalue.setDatatype(0);
			
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
	
	@RequestMapping(value = "/getColorForStockin",method = RequestMethod.POST)
	public ResponseEntity<AttributeValue_getlist_byId_Response> getColorForStockin
	(@RequestBody AttributeValue_getlist_notin_pcontractatt_request entity,HttpServletRequest request ) {
		AttributeValue_getlist_byId_Response response = new AttributeValue_getlist_byId_Response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long stockinid_link = entity.stockinid_link;
			
			response.data = attValueService.getColorForStockin(stockinid_link);
			
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<AttributeValue_getlist_byId_Response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<AttributeValue_getlist_byId_Response>(response,HttpStatus.OK);
		}
	}
}
