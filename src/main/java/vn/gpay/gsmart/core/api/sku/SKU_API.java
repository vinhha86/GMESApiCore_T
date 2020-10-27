package vn.gpay.gsmart.core.api.sku;


import java.util.ArrayList;
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

import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/sku")
public class SKU_API {
	@Autowired ISKU_AttributeValue_Service savService;
	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/getall_byproduct",method = RequestMethod.POST)
	public ResponseEntity<SKU_getbyproduct_response> Product_GetAll(HttpServletRequest request, @RequestBody SKU_getbyproduct_request entity ) {
		SKU_getbyproduct_response response = new SKU_getbyproduct_response();
		try {
			response.data = skuService.getlist_byProduct(entity.productid_link);
			if(entity.isremove) {
				response.data.removeIf(c->c.getIs_default());
			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getbyproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getbyproduct_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createcode",method = RequestMethod.POST)
	public ResponseEntity<SKU_createcode_response> update_SKU(HttpServletRequest request, @RequestBody SKU_Createcode_Request entity ) {
		SKU_createcode_response response = new SKU_createcode_response();
		
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			
			SKU sku_check = skuService.getSKU_byCode(entity.data.getCode(), orgrootid_link);
			
			if(sku_check != null) {
				response.mesErr = "Sku đã trùng trong hệ thống";
			}
			else {
				SKU sku = entity.data;
				if(sku.getId()==null || sku.getId()==0) {
					sku.setOrgrootid_link(user.getRootorgid_link());
				}else {
					SKU sku_old =  skuService.findOne(sku.getId());
					sku.setOrgrootid_link(sku_old.getOrgrootid_link());
				}
				
				skuService.save(sku);
				response.mesErr = "";
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_createcode_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_createcode_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
    @RequestMapping(value = "/getsku_mainmaterial",method = RequestMethod.POST)
    public List<SKU> getSKU_MainMaterial(@RequestBody SkuByCodeRequest entity, HttpServletRequest request) {
    	return skuService.getSKU_MainMaterial(entity.skucode);
    }
    
    @RequestMapping(value = "/getsku_bytype",method = RequestMethod.POST)
    public List<SKU> getSKU_ByType(@RequestBody SkuByCodeRequest entity, HttpServletRequest request) {
    	return skuService.getSKU_ByType(entity.skucode, entity.skutypeid_link);
    }
    
    @RequestMapping(value = "/getProductSKU_ByCode",method = RequestMethod.POST)
	public ResponseEntity<SKU_getbyproduct_response> getProductSKU_ByCode(HttpServletRequest request, @RequestBody SkuByCodeRequest entity ) {
		SKU_getbyproduct_response response = new SKU_getbyproduct_response();
		
		try {
//			System.out.println(entity.skutypeid_link + " " + entity.skucode);
			List<SKU> skus = skuService.getProductSKU_ByBarCode(entity.skutypeid_link, entity.skucode);
			response.data = new ArrayList<SKU>();
			if(skus.size() > 0) {
				response.data.addAll(skus);
			}else {
				response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
				response.setMessage("Mã vạch không tồn tại");
				return new ResponseEntity<SKU_getbyproduct_response>(response,HttpStatus.OK);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getbyproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getbyproduct_response>(response, HttpStatus.BAD_REQUEST);
		}
	}
}
