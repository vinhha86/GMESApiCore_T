package vn.gpay.gsmart.core.api.handover_sku;

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

import vn.gpay.gsmart.core.handover.Handover;
import vn.gpay.gsmart.core.handover.IHandoverService;
import vn.gpay.gsmart.core.handover_product.HandoverProduct;
import vn.gpay.gsmart.core.handover_product.IHandoverProductService;
import vn.gpay.gsmart.core.handover_sku.HandoverSKU;
import vn.gpay.gsmart.core.handover_sku.IHandoverSKUService;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.porder_product_sku.POrder_Product_SKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/handoversku")
public class HandoverSKUAPI {
	@Autowired IHandoverService handoverService;
	@Autowired IHandoverSKUService handoverSkuService;
	@Autowired IHandoverProductService handoverProductService;
	@Autowired IPOrder_Product_SKU_Service porderskuService;
	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<HandoverSKU_getall_response> GetAll(HttpServletRequest request ) {
		HandoverSKU_getall_response response = new HandoverSKU_getall_response();
		try {
			response.data = handoverSkuService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
		}
	}
	
//	@RequestMapping(value = "/getByHandoverProduct",method = RequestMethod.POST)
//	public ResponseEntity<HandoverSKU_getall_response> getByHandoverProduct(@RequestBody HandoverSKU_getByHandoverProduct_request entity ,HttpServletRequest request ) {
//		HandoverSKU_getall_response response = new HandoverSKU_getall_response();
//		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			
//			List<HandoverSKU> list = handoverSkuService.getByHandoverId(entity.handoverid_link, entity.productid_link);
//			
////			if(list.size() == 0) {
////				// create
////				Date date = new Date();
////				List<POrder_Product_SKU> porderProductSkus = porderskuService.getby_porder(entity.porderid_link);
////				for(POrder_Product_SKU porderProductSku : porderProductSkus) {
////					HandoverSKU newHandoverSKU = new HandoverSKU();
////					newHandoverSKU.setId(0L);
////					newHandoverSKU.setOrgrootid_link(user.getRootorgid_link());
////					newHandoverSKU.setHandoverid_link(entity.handoverid_link);
////					newHandoverSKU.setHandoverproductid_link(entity.handoverproductid_link);
////					newHandoverSKU.setProductid_link(entity.productid_link);
////					newHandoverSKU.setSkuid_link(porderProductSku.getSkuid_link());
////					newHandoverSKU.setTotalpackage(0);
////					newHandoverSKU.setUsercreateid_link(user.getId());
////					newHandoverSKU.setTimecreate(date);
////					newHandoverSKU.setLastuserupdateid_link(user.getId());
////					newHandoverSKU.setLasttimeupdate(date);
////					handoverSkuService.save(newHandoverSKU);
////				}
////				list = handoverSkuService.getByHandoverId(entity.handoverid_link, entity.productid_link);
////			}
//			
//			if(list.size() == 0) {
//				// create
//				Date date = new Date();
//				Long porderid_link = entity.porderid_link;
//				Long handoverid_link = entity.handoverid_link;
//				Long handoverproductid_link = entity.handoverproductid_link;
//				Long productid_link = entity.productid_link;
//				
//				// tim cac sku theo porder
//				
//				List<SKU> skus = skuService.getSKUforHandOver(porderid_link);
//				for(SKU sku : skus) {
//					HandoverSKU newHandoverSKU = new HandoverSKU();
//					newHandoverSKU.setId(0L);
//					newHandoverSKU.setOrgrootid_link(user.getRootorgid_link());
//					newHandoverSKU.setHandoverid_link(entity.handoverid_link);
//					newHandoverSKU.setHandoverproductid_link(entity.handoverproductid_link);
//					newHandoverSKU.setProductid_link(entity.productid_link);
//					newHandoverSKU.setSkuid_link(sku.getId());
//					newHandoverSKU.setTotalpackage(0);
//					newHandoverSKU.setUsercreateid_link(user.getId());
//					newHandoverSKU.setTimecreate(date);
//					newHandoverSKU.setLastuserupdateid_link(user.getId());
//					newHandoverSKU.setLasttimeupdate(date);
//					handoverSkuService.save(newHandoverSKU);
//				}
//				list = handoverSkuService.getByHandoverId(entity.handoverid_link, entity.productid_link);
//			}
//			
//			response.data = list;
//			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
//			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
//			return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
//		}catch (Exception e) {
//			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
//			response.setMessage(e.getMessage());
//		    return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
//		}
//	}
	
	@RequestMapping(value = "/getByHandoverProduct",method = RequestMethod.POST)
	public ResponseEntity<HandoverSKU_getall_response> getByHandoverProduct(@RequestBody HandoverSKU_getByHandoverProduct_request entity ,HttpServletRequest request ) {
		HandoverSKU_getall_response response = new HandoverSKU_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			if(entity.handoverid_link == null) {
				entity.handoverid_link = 0L;
			}
			
			List<HandoverSKU> list = new ArrayList<HandoverSKU>();
			if(entity.handoverid_link != 0L) {
				list = handoverSkuService.getByHandoverId(entity.handoverid_link, entity.productid_link);
			}
			
			
			if(list.size() == 0) {
				// create
				Date date = new Date();
				Long porderid_link = entity.porderid_link;
//				Long handoverid_link = entity.handoverid_link;
//				Long handoverproductid_link = entity.handoverproductid_link;
//				Long productid_link = entity.productid_link;
				
				// tim cac sku theo porder
				
				List<SKU> skus = skuService.getSKUforHandOver(porderid_link);
				for(SKU sku : skus) {
					HandoverSKU newHandoverSKU = new HandoverSKU();
					newHandoverSKU.setId(0L);
					newHandoverSKU.setOrgrootid_link(user.getRootorgid_link());
//					newHandoverSKU.setHandoverid_link(entity.handoverid_link);
//					newHandoverSKU.setHandoverproductid_link(entity.handoverproductid_link);
					newHandoverSKU.setProductid_link(entity.productid_link);
					newHandoverSKU.setTotalpackage(0);
					newHandoverSKU.setTotalpackagecheck(0);
					newHandoverSKU.setUsercreateid_link(user.getId());
					newHandoverSKU.setTimecreate(date);
					newHandoverSKU.setLastuserupdateid_link(user.getId());
					newHandoverSKU.setLasttimeupdate(date);
					// sku properties
					newHandoverSKU.setSkuid_link(sku.getId());
					newHandoverSKU.setSkuCodeString(sku.getCode());
					newHandoverSKU.setSkuColorString(sku.getColor_name());
					newHandoverSKU.setSkuSizeString(sku.getSize_name());
					newHandoverSKU.setSkuSizeSortValInt(sku.getSort_size());
//					handoverSkuService.save(newHandoverSKU);
					list.add(newHandoverSKU);
				}
//				list = handoverSkuService.getByHandoverId(entity.handoverid_link, entity.productid_link);
			}
			
			response.data = list;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverSKU_getall_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/updateHandoverSku",method = RequestMethod.POST)
	public ResponseEntity<HandoverSKU_update_response> updateHandoverSku(@RequestBody HandoverSKU_update_request entity,HttpServletRequest request ) {
		HandoverSKU_update_response response = new HandoverSKU_update_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Date date = new Date();
			HandoverSKU handoverSku = entity.data;
			handoverSku.setLasttimeupdate(date);
			handoverSku.setLastuserupdateid_link(user.getId());
			handoverSku = handoverSkuService.save(handoverSku);
			
			// update handoverProduct totalpackage
			HandoverProduct handoverProduct = handoverProductService.findOne(handoverSku.getHandoverproductid_link());
			List<HandoverSKU> skus = handoverProduct.getHandoverSKUs();
			Integer total = 0;
			Integer totalCheck = 0;
			
			for(HandoverSKU sku : skus) {
				if(sku.getTotalpackage() != null)
					total += sku.getTotalpackage();
				if(sku.getTotalpackagecheck() != null)
					totalCheck += sku.getTotalpackagecheck();
			}
			
			handoverProduct.setTotalpackage(total);
			handoverProduct.setTotalpackagecheck(totalCheck);
			handoverProduct.setLastuserupdateid_link(user.getId());
			handoverProduct.setLasttimeupdate(date);
			handoverProductService.save(handoverProduct);
			
			Handover handover = handoverService.findOne(handoverSku.getHandoverid_link());
			List<HandoverProduct> products = handover.getHandoverProducts();
			total = 0;
			totalCheck = 0;
			
			for(HandoverProduct product : products) {
				if(product.getTotalpackage() != null)
					total += product.getTotalpackage();
				if(product.getTotalpackagecheck() != null)
					totalCheck += product.getTotalpackagecheck();
			}
			
			handover.setTotalpackage(total);
			handover.setTotalpackagecheck(totalCheck);
			handover.setLastuserupdateid_link(user.getId());
			handover.setLasttimeupdate(date);
			handoverService.save(handover);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<HandoverSKU_update_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<HandoverSKU_update_response>(response,HttpStatus.OK);
		}
	}
}
