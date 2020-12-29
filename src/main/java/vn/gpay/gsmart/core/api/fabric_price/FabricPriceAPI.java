package vn.gpay.gsmart.core.api.fabric_price;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.currency.Currency;
import vn.gpay.gsmart.core.currency.ICurrencyService;
import vn.gpay.gsmart.core.fabric_price.FabricPrice;
import vn.gpay.gsmart.core.fabric_price.IFabricPriceService;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/fabricprice")
public class FabricPriceAPI {
	@Autowired IFabricPriceService fabricPriceService;
	@Autowired ICurrencyService currencyService;
	@Autowired ISKU_Service skuService;
	
	@RequestMapping(value = "/getByMaterial",method = RequestMethod.POST)
	public ResponseEntity<FabricPrice_getByMaterial_response> getByMaterial(@RequestBody FabricPrice_getByMaterial_request entity,HttpServletRequest request ) {
		FabricPrice_getByMaterial_response response = new FabricPrice_getByMaterial_response();
		try {
			Long unitid_link = entity.unitid_link;
			Long pcontractpoCurrencyid_link = entity.currencyid_link;
			List<Long> materialid_link_list = entity.materialid_link_list;
			
			List<SKU> listSku = new ArrayList<SKU>();
			
			for(Long materialid_link : materialid_link_list) {
				SKU sku = skuService.findOne(materialid_link);
				List<FabricPrice> listFabricPrice = fabricPriceService.getByMaterial(materialid_link);
				if(listFabricPrice.size() > 0 && (unitid_link == 1 || unitid_link == 4)) {
					// đã có giá
					Currency pcontractpoCurrency = currencyService.findOne(pcontractpoCurrencyid_link);
					FabricPrice fabricPrice = listFabricPrice.get(0);
					if(fabricPrice.getCurrencyid_link() == null) {
						// vnd
						Float unitprice = 0F;
						if(unitid_link == 1) {
							// met
							Float price = fabricPrice.getPrice_per_m();
							unitprice = price / pcontractpoCurrency.getExchangerate().floatValue();
							unitprice = ((float) Math.round(unitprice * 1000)) / 1000;
						}
						if(unitid_link == 4) {
							// kg
							Float price = fabricPrice.getPrice_per_kg();
							unitprice = price / pcontractpoCurrency.getExchangerate().floatValue();
							unitprice = ((float) Math.round(unitprice * 1000)) / 1000;
						}
						sku.setUnitPrice(unitprice);
						listSku.add(sku);
					}else {
						// ngoại tệ
						Float unitprice = 0F;
						Long fabricPriceCurrencyId_link = fabricPrice.getCurrencyid_link();
						Currency fabricPriceCurrency = currencyService.findOne(fabricPriceCurrencyId_link);
						Float rate = fabricPriceCurrency.getExchangerate().floatValue() / pcontractpoCurrency.getExchangerate().floatValue();
						if(unitid_link == 1) {
							// met
							Float price = fabricPrice.getPrice_per_m();
							unitprice = price * rate;
							unitprice = ((float) Math.round(unitprice * 1000)) / 1000;
						}
						if(unitid_link == 4) {
							// kg
							Float price = fabricPrice.getPrice_per_kg();
							unitprice = price * rate;
							unitprice = ((float) Math.round(unitprice * 1000)) / 1000;
						}
						sku.setUnitPrice(unitprice);
						listSku.add(sku);
					}
					
				}else {
					// chưa có giá
					sku.setUnitPrice(0F);
					listSku.add(sku);
				}
			}
			
			response.data = listSku;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<FabricPrice_getByMaterial_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<FabricPrice_getByMaterial_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getByMaterialIdLink",method = RequestMethod.POST)
	public ResponseEntity<FabricPrice_getByMaterialIdLink_response> getByMaterialIdLink(@RequestBody FabricPrice_getByMaterial_request entity,HttpServletRequest request ) {
		FabricPrice_getByMaterialIdLink_response response = new FabricPrice_getByMaterialIdLink_response();
		try {
			List<Long> materialid_link_list = entity.materialid_link_list;
			Long materialid_link = materialid_link_list.get(0);
			
			List<FabricPrice> listFabricPrice = fabricPriceService.getByMaterial(materialid_link);
			if(listFabricPrice.size() > 0) {
				FabricPrice fabricPrice = listFabricPrice.get(0);
				response.data = fabricPrice;
			}else {
				FabricPrice fabricPrice = new FabricPrice();
				fabricPrice.setMaterialid_link(materialid_link);
				fabricPrice.setM_per_kg(0F);
				fabricPrice.setPrice_per_kg(0F);
				fabricPrice.setPrice_per_m(0F);
				
				fabricPrice = fabricPriceService.save(fabricPrice);
				
				response.data = fabricPrice;
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<FabricPrice_getByMaterialIdLink_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<FabricPrice_getByMaterialIdLink_response>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> update(@RequestBody FabricPrice_update_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			FabricPrice fabricPrice = entity.data;
			fabricPriceService.save(fabricPrice);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/create",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> create(@RequestBody FabricPrice_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			List<Long> materialid_link_list = entity.data;
			for(Long materialid_link : materialid_link_list) {
				// check tồn tại trong bảng fabric_price
				List<FabricPrice> listFabricPrice = fabricPriceService.getByMaterial(materialid_link);
				if(listFabricPrice.size() == 0) {
					FabricPrice fabricPrice = new FabricPrice();
					fabricPrice.setMaterialid_link(materialid_link);
					fabricPrice.setM_per_kg(0F);
					fabricPrice.setPrice_per_kg(0F);
					fabricPrice.setPrice_per_m(0F);
					fabricPriceService.save(fabricPrice);
				}
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> delete(@RequestBody FabricPrice_create_request entity,HttpServletRequest request ) {
		ResponseBase response = new ResponseBase();
		try {
			List<Long> idList = entity.data;
			for(Long id : idList) {
				fabricPriceService.deleteById(id);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getall",method = RequestMethod.POST)
	public ResponseEntity<FabricPrice_getList_response> getall(HttpServletRequest request ) {
		FabricPrice_getList_response response = new FabricPrice_getList_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			long orgrootid_link = user.getRootorgid_link();
			response.data = fabricPriceService.findAll();
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<FabricPrice_getList_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<FabricPrice_getList_response>(response,HttpStatus.OK);
		}
	}
}
