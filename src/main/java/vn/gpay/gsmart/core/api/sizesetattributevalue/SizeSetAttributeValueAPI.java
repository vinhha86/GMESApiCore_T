package vn.gpay.gsmart.core.api.sizesetattributevalue;

import java.util.ArrayList;
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

import vn.gpay.gsmart.core.api.attributevalue.AttributeValue_getlist_byId_Request;
import vn.gpay.gsmart.core.api.attributevalue.AttributeValue_getlist_byId_Response;
import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sizeset.ISizeSetService;
import vn.gpay.gsmart.core.sizeset.SizeSet;
import vn.gpay.gsmart.core.sizesetattributevalue.ISizeSetAttributeService;
import vn.gpay.gsmart.core.sizesetattributevalue.SizeSetAttributeValue;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/sizesetattribute")
public class SizeSetAttributeValueAPI {
	
	@Autowired
	IAttributeValueService avService;
	@Autowired
	ISizeSetService sizesetService;
	@Autowired
	ISizeSetAttributeService sizesetAttrService;
	
	@RequestMapping(value = "/deleteatt", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeDelete(@RequestBody SizeSetAttribute_delete_byAtt_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {

			List<SizeSetAttributeValue> lst = sizesetAttrService.getList_byAttId(entity.attributeid_link,
					entity.sizesetid_link);
			SizeSet sizeset = sizesetService.findOne(entity.sizesetid_link);

			for (SizeSetAttributeValue sizesetAttributeValue : lst) {
				sizesetAttrService.delete(sizesetAttributeValue);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getvalue", method = RequestMethod.POST)
	public ResponseEntity<SizeSetAttributeValue_getbyid_Response> AttributeValueget(
			@RequestBody SizeSetAttribute_createvalue_request entity, HttpServletRequest request) {
		SizeSetAttributeValue_getbyid_Response response = new SizeSetAttributeValue_getbyid_Response();
		List<SizeSetAttributeValue> list_old = sizesetAttrService.getList_byAttId(entity.attributeid_link,
				entity.sizesetid_link);
		response.data = new ArrayList<Attributevalue>();
		try {
			for (SizeSetAttributeValue sizesetAttributeValue : list_old) {
				Attributevalue av = new Attributevalue();
				av.setAttributeid_link(entity.attributeid_link);
				av.setId(sizesetAttributeValue.getAttributevalueid_link());
				av.setDatatype(0);
				av.setValue(sizesetAttributeValue.getAttributeValueName());
				response.data.add(av);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SizeSetAttributeValue_getbyid_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<SizeSetAttributeValue_getbyid_Response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createvalue", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValueCreate(@RequestBody SizeSetAttribute_createvalue_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		List<SizeSetAttributeValue> list_old = sizesetAttrService.getList_byAttId(entity.attributeid_link,
				entity.sizesetid_link);
		try {
			for (SizeSetAttributeValue sizesetAttributeValue : list_old) {
				sizesetAttrService.delete(sizesetAttributeValue);
			}
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			for (Long id : entity.listvalue) {
				SizeSetAttributeValue data = new SizeSetAttributeValue();
				data.setId(null);
				data.setAttributeid_link(entity.attributeid_link);
				data.setAttributevalueid_link(id);
				data.setSizesetid_link(entity.sizesetid_link);
				data.setOrgrootid_link(user.getRootorgid_link());

				sizesetAttrService.save(data);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createatt", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeCreate(@RequestBody SizeSetAttribute_createatt_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {

			for (Long id : entity.listId) {
				SizeSetAttributeValue data = new SizeSetAttributeValue();
				data.setId((long) 0);
				data.setAttributeid_link(id);
				data.setAttributevalueid_link((long) 0);
				data.setSizesetid_link(entity.sizesetid_link);

				sizesetAttrService.save(data);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getbyidattributeforsizeset",method = RequestMethod.POST)
	public ResponseEntity<AttributeValue_getlist_byId_Response> AttributeValue_Get_ById_ForSizeset(@RequestBody AttributeValue_getlist_byId_Request entity,HttpServletRequest request ) {
		AttributeValue_getlist_byId_Response response = new AttributeValue_getlist_byId_Response();
		try {
			//GPayUserDetail user = (GPayUserDetail) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			List<Attributevalue> listAttr = avService.getlist_byidAttribute(entity.id);
			List<SizeSetAttributeValue> listOther = sizesetAttrService.getallother_bySizeSetId(entity.sizesetid_link);
			List<Attributevalue> listToRmove = new ArrayList<Attributevalue>();
			Attributevalue valAll = new Attributevalue();
			
			
			for(Attributevalue attrValue : listAttr) {
				if(attrValue.getValue().equals("ALL")) {
					valAll = attrValue;
				}
			}
			listAttr.remove(valAll);
			
			for(Attributevalue attrValue : listAttr) {
				for(SizeSetAttributeValue sizesetAttrValue : listOther) {
					if(attrValue.getId() == sizesetAttrValue.getAttributevalueid_link()) {
						listToRmove.add(attrValue);
						break;
					}
				}
			}
			for(Attributevalue attrValue : listToRmove) {
				listAttr.remove(attrValue);
			}

			response.data = listAttr;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
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
