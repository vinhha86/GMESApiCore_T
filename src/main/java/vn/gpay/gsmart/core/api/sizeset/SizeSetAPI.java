package vn.gpay.gsmart.core.api.sizeset;

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

import vn.gpay.gsmart.core.attribute.Attribute;
import vn.gpay.gsmart.core.attribute.IAttributeService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sizeset.ISizeSetService;
import vn.gpay.gsmart.core.sizeset.SizeSet;
import vn.gpay.gsmart.core.sizesetattributevalue.ISizeSetAttributeService;
import vn.gpay.gsmart.core.sizesetattributevalue.SizeSetAttributeValue;
import vn.gpay.gsmart.core.sizesetattributevalue.SizeSetAttributeValueBinding;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/sizeset")
public class SizeSetAPI {
	@Autowired 
	ISizeSetService sizesetservice;
	@Autowired
	IAttributeService attrService;
	@Autowired
	ISizeSetAttributeService sizesetAttrService;
	
	@RequestMapping(value = "/getall", method = RequestMethod.POST)
	public ResponseEntity<SizeSet_getall_response> Product_GetAll(HttpServletRequest request) {
		SizeSet_getall_response response = new SizeSet_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<SizeSet> season = sizesetservice.getall_byorgrootid(user.getRootorgid_link());
			
			response.data = season;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SizeSet_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<SizeSet_getall_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/getById", method = RequestMethod.POST)
	public ResponseEntity<SizeSet_getById_response> getById(HttpServletRequest request,
			@RequestBody SizeSet_getById_request entity) {
		SizeSet_getById_response response = new SizeSet_getById_response();
		try {
			SizeSet sizeset = sizesetservice.findOne(entity.id);
			response.data = sizeset;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SizeSet_getById_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<SizeSet_getById_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deletesizeset", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> SizeSet_Delete(@RequestBody SizeSet_delete_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();

		try {
			List<SizeSetAttributeValue> list = sizesetAttrService.getall_bySizeSetId(entity.id);
			for(SizeSetAttributeValue sizesetattrvalue : list) {
				sizesetAttrService.delete(sizesetattrvalue);
			}
			sizesetservice.deleteById(entity.id);

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createsizeset", method = RequestMethod.POST)
	public ResponseEntity<SizeSet_create_response> SizeSet_Create(@RequestBody SizeSet_create_request entity,
			HttpServletRequest request) {
		SizeSet_create_response response = new SizeSet_create_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			SizeSet sizeset = entity.data;
			long orgrootid_link = user.getRootorgid_link();
			boolean isNew = false;
			
			if (sizeset.getId() == null || sizeset.getId() == 0) {
				isNew = true;
				sizeset.setOrgrootid_link(user.getRootorgid_link());
				sizeset.setUsercreatedid_link(user.getId());
				sizeset.setTimecreate(new Date());
				
			} else {
				SizeSet sizeset_old = sizesetservice.findOne(sizeset.getId());
				sizeset.setOrgrootid_link(sizeset_old.getOrgrootid_link());
				sizeset.setUsercreatedid_link(sizeset_old.getUsercreatedid_link());
				sizeset.setTimecreate(sizeset_old.getTimecreate());
			}
			
			sizeset = sizesetservice.save(sizeset);
			
			response.id = sizeset.getId();
			response.sizeset = sizeset;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SizeSet_create_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<SizeSet_create_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getattrvalue", method = RequestMethod.POST)
	public ResponseEntity<SizeSet_getattvalue_response> Sizeset_GetAttributeValue(HttpServletRequest request,
			@RequestBody SizeSet_getattvalue_request entity) {
		SizeSet_getattvalue_response response = new SizeSet_getattvalue_response();
		response.data = new ArrayList<SizeSetAttributeValueBinding>();
		try {
			List<SizeSetAttributeValue> lst = sizesetAttrService.getall_bySizeSetId(entity.id);
			for (SizeSetAttributeValue sizesetAttributeValue : lst) {
				SizeSetAttributeValueBinding obj = new SizeSetAttributeValueBinding();
				obj.setAttributeName(sizesetAttributeValue.getAttributeName());
				obj.setAttributeid_link(sizesetAttributeValue.getAttributeid_link());
				obj.setAttributeValueName("");
				
				boolean isExist = false;
				
				for (SizeSetAttributeValueBinding binding : response.data) {
					if (binding.getAttributeid_link() == obj.getAttributeid_link()){
						isExist = true;
						break;
					}
				}
				
				if(!isExist) {
					response.data.add(obj);
				}
			}

			for (SizeSetAttributeValueBinding binding : response.data) {
				String name = "";
				for (SizeSetAttributeValue sizesetAttributeValue : lst) {
					if (sizesetAttributeValue.getAttributeName() == binding.getAttributeName()) {
						String attName = sizesetAttributeValue.getAttributeValueName();
						
						if (attName != "") {
							if (name == "") {
								name += sizesetAttributeValue.getAttributeValueName();
							} else {
								name += ", " + sizesetAttributeValue.getAttributeValueName();
							}
						}
					}

					binding.setAttributeValueName(name);
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SizeSet_getattvalue_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<SizeSet_getattvalue_response>(response, HttpStatus.OK);
		}
	}
}
