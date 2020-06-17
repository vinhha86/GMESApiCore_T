package vn.gpay.gsmart.core.api.encode;

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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.base.ResponseError;
import vn.gpay.gsmart.core.security.GpayAuthentication;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.tagencode.ITagEncodeService;
import vn.gpay.gsmart.core.tagencode.TagEncode;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/encode")
public class EncodeApi {

	@Autowired ITagEncodeService tagEncodeService;
	@Autowired ISKU_Service skuService;
	@RequestMapping(value = "/encode_create",method = RequestMethod.POST)
	public ResponseEntity<?> Encode_Create(@RequestBody EncodeRequest entity, HttpServletRequest request ) {//@RequestParam("type") 
		ResponseBase response = new ResponseBase();
		try {
			
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			long orgrootid_link = user.getRootorgid_link();
			if(entity.data.size()>0) {
				SKU sku =new SKU();
				try {
					sku = skuService.getSKU_byCode(entity.data.get(0).getSku(), orgrootid_link);
				}catch(Exception e) {
					sku=null;
				}
				Long skuid_link=null;
				if(sku.getId() != null) {skuid_link = sku.getId();}
				TagEncode encode = new TagEncode();
				encode.setEpc(entity.data.get(0).getEpc());
				encode.setOldepc(entity.data.get(0).getOldepc());
				encode.setTid(entity.data.get(0).getTid());
				encode.setDeviceid_link(entity.data.get(0).getDeviceid_link());
				encode.setSkuid_link(skuid_link);
				encode.setOrgid_link(user.getRootorgid_link());
				encode.setOrgencodeid_link(user.getOrgId());
				encode.setStatus(0);
				encode.setUsercreateid_link(user.getUserId());
				encode.setTimecreate(new Date());
				tagEncodeService.create(encode);
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@RequestMapping(value = "/encode_getbydevice",method = RequestMethod.POST)
	public ResponseEntity<?> EncodeGetByDevice(@RequestBody EncodeGetByDeviceIdRequest entity, HttpServletRequest request ) {//@RequestParam("type") 
		EncodeResponse response = new EncodeResponse();
		try {
			
			GpayAuthentication user = (GpayAuthentication)SecurityContextHolder.getContext().getAuthentication();
			response.data = tagEncodeService.encode_getbydevice(user.getOrgId(),entity.deviceid);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<EncodeResponse>(response,HttpStatus.OK);
		}catch (RuntimeException e) {
			ResponseError errorBase = new ResponseError();
			errorBase.setErrorcode(ResponseError.ERRCODE_RUNTIME_EXCEPTION);
			errorBase.setMessage(e.getMessage());
		    return new ResponseEntity<>(errorBase, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
