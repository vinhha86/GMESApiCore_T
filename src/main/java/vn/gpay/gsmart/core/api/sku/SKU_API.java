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

import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom2;
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
	@Autowired IPContractProductBom2Service pcontract_bom2_Service;
	
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
				SKU sku_old =  skuService.findOne(entity.data.getId());
				if (sku_old.getCode() != sku_check.getCode()){
					response.mesErr = "SKU đã bị trùng trong hệ thống! Liên hệ quản trị hệ thống để kiểm tra lại";
				} else {
					sku_old.setBarcode(entity.data.getBarcode());
					sku_old.setPartnercode(entity.data.getPartnercode());
					skuService.save(sku_old);
					response.mesErr = "";
				}
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
	
	@RequestMapping(value = "/getSkuByCode",method = RequestMethod.POST)
	public ResponseEntity<SKU_getbyproduct_response> getSkuByCode(HttpServletRequest request, @RequestBody SKU_getSkuByCode_request entity ) {
		SKU_getbyproduct_response response = new SKU_getbyproduct_response();
		try {
//			response.data = skuService.getSkuByCode(entity.code);
			if(entity.typeFrom == null) entity.typeFrom = 20;
			if(entity.typeTo == null) entity.typeTo = 30;
			response.data = skuService.getSkuByCodeAndType(entity.code, entity.typeFrom, entity.typeTo); //ex: typeFrom: 20, typeTo: 30
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getbyproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getbyproduct_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getSkuForXuatDieuChuyenNguyenLieu",method = RequestMethod.POST)
	public ResponseEntity<SKU_getbyproduct_response> getSkuForXuatDieuChuyenNguyenLieu(HttpServletRequest request, @RequestBody SKU_GetForXuatDieuChuyenNguyenLieu_request entity ) {
		SKU_getbyproduct_response response = new SKU_getbyproduct_response();
		try {
			Long pcontractid_link_current = entity.pcontractid_link_current;
			Long pcontractid_link_loanfrom = entity.pcontractid_link_loanfrom;
			Long productid_link = entity.productid_link;
			
			List<SKU> result = new ArrayList<SKU>();
			List<PContractProductBom2> listbom = pcontract_bom2_Service.get_material_in_pcontract_productBOM(productid_link, pcontractid_link_current, 20);
			List<Long> skuid_list = new ArrayList<Long>();
			// lấy danh sách id sku loại vải của sản phẩm và đơn hàng hiện tại
			for(PContractProductBom2 pcontractProductBom2 : listbom) {
				skuid_list.add(pcontractProductBom2.getMaterialid_link());
			}
			
			if(skuid_list.size() > 0) {
				// tìm danh sách sku của đơn hàng chứa sản phẩm chứa các loại vải của đơn hàng hiện tại
				result = skuService.getSkuForXuatDieuChuyenNguyenLieu(skuid_list, pcontractid_link_loanfrom);
//				System.out.println(result.size());
			}
			
			response.data = result;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<SKU_getbyproduct_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<SKU_getbyproduct_response>(response, HttpStatus.OK);
		}
	}
}
