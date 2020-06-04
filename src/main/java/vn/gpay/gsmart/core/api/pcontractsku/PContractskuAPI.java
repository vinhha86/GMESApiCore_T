package vn.gpay.gsmart.core.api.pcontractsku;

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
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractattributevalue.PContractAttributeValue;
import vn.gpay.gsmart.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontratproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractsku")
public class PContractskuAPI {
	@Autowired IPContractProductSKUService pskuservice;
	@Autowired IPContractProductAtrributeValueService pcpavservice;
	@Autowired ISKU_AttributeValue_Service skuavService;
	
	@RequestMapping(value = "/getbypcontract_product",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> SKU_GetbyProduct
	(HttpServletRequest request, @RequestBody PContractSKU_getbyproduct_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			response.data = pskuservice.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> SKU_Update
	(HttpServletRequest request, @RequestBody PContractSKYU_update_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			pskuservice.save(entity.data);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> SKU_Delete
	(HttpServletRequest request, @RequestBody PContractSKU_delete_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long skuid_link = entity.skuid_link;
			
			List<PContractProductSKU> psku = pskuservice.getlistsku_bysku_and_pcontract(skuid_link, pcontractid_link);
			if(psku.size() > 0) {
				pskuservice.delete(psku.get(0));
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Create
	(HttpServletRequest request, @RequestBody PContractSKU_create_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long orgrootid_link = user.getRootorgid_link();
			
			 for(Long skuid_link : entity.listskuid_link) {
				 //Kiem tra sku da co trong don hang chua? chua co thi moi insert vao
				 List<PContractProductSKU> list = pskuservice.getlistsku_bysku_and_pcontract(skuid_link, pcontractid_link);
				 if(list.size() == 0) {
					 PContractProductSKU psku = new PContractProductSKU();
					 psku.setId(null);
					 psku.setOrgrootid_link(orgrootid_link);
					 psku.setPcontractid_link(pcontractid_link);
					 psku.setPquantity_granted(0);
					 psku.setPquantity_porder(0);
					 psku.setPquantity_sample(0);
					 psku.setPquantity_total(0);
					 psku.setProductid_link(productid_link);
					 psku.setSkuid_link(skuid_link);
					 
					 pskuservice.save(psku);
				 }
				 
				 
				//Them cac gia tri thuoc tinh vao san pham cua don hang neu chua co
				 List<SKU_Attribute_Value> list_skuav = skuavService.getlist_bysku(skuid_link);
				 
				 long value_mau = 0;
				 long value_co =0;
				 
				 for (SKU_Attribute_Value skuav : list_skuav) {
					if(skuav.getAttributeid_link() == AtributeFixValues.ATTR_COLOR) {
						value_mau = skuav.getAttributevalueid_link();
					}
					else if (skuav.getAttributeid_link() == AtributeFixValues.ATTR_SIZE) {
						value_co = skuav.getAttributevalueid_link();
					}
				}

				 //Kiem tra mau
				List<PContractAttributeValue> pav_mau = pcpavservice.getbyvalue(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR, value_mau);
				if(pav_mau.size() == 0) {
					PContractAttributeValue pav = new PContractAttributeValue();
					pav.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
					pav.setAttributevalueid_link(value_mau);
					pav.setId(null);
					pav.setOrgrootid_link(orgrootid_link);
					pav.setPcontractid_link(pcontractid_link);
					pav.setProductid_link(productid_link);
					
					pcpavservice.save(pav);
				}
				
				 //Kiem tra co
				List<PContractAttributeValue> pav_co = pcpavservice.getbyvalue(pcontractid_link, productid_link, AtributeFixValues.ATTR_SIZE, value_co);
				if(pav_co.size() == 0) {
					PContractAttributeValue pav = new PContractAttributeValue();
					pav.setAttributeid_link(AtributeFixValues.ATTR_SIZE);
					pav.setAttributevalueid_link(value_co);
					pav.setId(null);
					pav.setOrgrootid_link(orgrootid_link);
					pav.setPcontractid_link(pcontractid_link);
					pav.setProductid_link(productid_link);
					
					pcpavservice.save(pav);
				}
			 }
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
}
