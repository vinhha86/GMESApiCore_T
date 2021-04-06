package vn.gpay.gsmart.core.api.porder_bom;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBom2ColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.PContractBom2Color;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom2;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_bom_color.IPOrderBomColor_Service;
import vn.gpay.gsmart.core.porder_bom_color.PorderBomColor;
import vn.gpay.gsmart.core.porder_bom_product.IPOrderBomProduct_Service;
import vn.gpay.gsmart.core.porder_bom_product.POrderBomProduct;
import vn.gpay.gsmart.core.porder_bom_sku.IPOrderBOMSKU_Service;
import vn.gpay.gsmart.core.porder_bom_sku.POrderBOMSKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/porderbom")
public class POrderBomAPI {
	@Autowired IPOrder_Service porderService;
	@Autowired IPContractProductBom2Service pcontractbomproductService;
	@Autowired IPOrderBomProduct_Service porderbomproductService;
	@Autowired IPOrderBomColor_Service porderbomcolorService;
	@Autowired IPContractBom2ColorService pcontractbomcolorService;
	@Autowired IPOrderBOMSKU_Service porderbomskuService;
	@Autowired IPContractBOM2SKUService pcontractbomskuService;
	@Autowired IPContractProductSKUService ppskuService;
	@Autowired ISKU_AttributeValue_Service skuavService;
	
	@RequestMapping(value = "/update_poder_bom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBom(HttpServletRequest request,
			@RequestBody update_porderbom_product_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			
			porderbomproductService.save(entity.data);
			//Xóa trong bom_color và bom_sku
			
			if(entity.isUpdateBOM) {
				long porderid_link = entity.data.getPorderid_link();
				long materialid_link = entity.data.getMaterialid_link();
				List<PorderBomColor> listcolor = porderbomcolorService.getby_porder_and_material(porderid_link, materialid_link);
				for (PorderBomColor pContractBOMColor : listcolor) {
					porderbomcolorService.delete(pContractBOMColor);
				}
				
				List<POrderBOMSKU> listsku = porderbomskuService.getby_porder_and_material(porderid_link, materialid_link);
				for (POrderBOMSKU pContractBOMSKU : listsku) {
					porderbomskuService.delete(pContractBOMSKU);
				}
			}
			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_porder_bomcolor", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomColor(HttpServletRequest request,
			@RequestBody update_porderbom_color_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long porderid_link = entity.data.getPorderid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long colorid_link = entity.colorid_link;
			
			List<PorderBomColor> listcolor = porderbomcolorService.getby_porder_and_material_and_color(porderid_link, materialid_link, colorid_link);

			PorderBomColor pContractBOMColor = new PorderBomColor();
			if(listcolor.size() > 0) {
				pContractBOMColor = listcolor.get(0);
				pContractBOMColor.setAmount(entity.data.getAmount_color());
			}
			else {
				pContractBOMColor.setAmount(entity.data.getAmount_color());
				pContractBOMColor.setColorid_link(colorid_link);
				pContractBOMColor.setCreateddate(new Date());
				pContractBOMColor.setCreateduserid_link(user.getId());
				pContractBOMColor.setDescription(entity.data.getDescription());
				pContractBOMColor.setId(null);
				pContractBOMColor.setMaterialid_link(materialid_link);
				pContractBOMColor.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMColor.setPcontractid_link(pcontractid_link);
				pContractBOMColor.setProductid_link(productid_link);
				pContractBOMColor.setPorderid_link(porderid_link);
			}
			porderbomcolorService.save(pContractBOMColor);
			
			//update lại các màu khác từ chung về chung màu
			POrderBomProduct pContractProductBom = porderbomproductService.findOne(entity.data.getId());
			float amount_color = pContractProductBom.getAmount();
			
			if(amount_color > 0) {
				//Lấy các màu trong sản phẩm của đơn hàng
				List<Long> list_color = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
				list_color.remove(colorid_link);
				
				for(Long colorid : list_color) {
					PorderBomColor color = new PorderBomColor();

					color.setAmount(amount_color);
					color.setPorderid_link(porderid_link);
					color.setColorid_link(colorid);
					color.setCreateddate(new Date());
					color.setCreateduserid_link(user.getId());
					color.setDescription(entity.data.getDescription());
					color.setId((long) 0);
					color.setMaterialid_link(materialid_link);
					color.setOrgrootid_link(user.getRootorgid_link());
					color.setPcontractid_link(pcontractid_link);
					color.setProductid_link(productid_link);
					color.setPorderid_link(porderid_link);
					
					porderbomcolorService.save(color);
				}
			}			
			
			//update lai bang bom amount = 0
			pContractProductBom.setAmount((float) 0);
			porderbomproductService.save(pContractProductBom);
			
			//update lai bang sku bom
			List<POrderBOMSKU> listsku = porderbomskuService.getby_porder_and_material_and_color(porderid_link, materialid_link, colorid_link);
			for (POrderBOMSKU pContractBOMSKU : listsku) {
				porderbomskuService.delete(pContractBOMSKU);
			}			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/get_npl_by_type", method = RequestMethod.POST)
	public ResponseEntity<getnpl_by_type_response> GetNPLByType(HttpServletRequest request,
			@RequestBody getnpl_by_type_request entity) {
		getnpl_by_type_response response = new getnpl_by_type_response();
		try {
			Long porderid_link = entity.porderid_link;
			int type_from = entity.type_from;
			int type_to = entity.type_to;
					
			response.data = porderbomproductService.getby_porder_and_type(porderid_link, type_from, type_to);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getnpl_by_type_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_porder_bomsku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomSKU(HttpServletRequest request,
			@RequestBody update_porderbom_sku_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long sizeid_link = entity.sizeid_link;
			long colorid_link = entity.colorid_link;
			long porderid_link = entity.data.getPorderid_link();
			long skuid_link = skuavService.getsku_byproduct_and_valuemau_valueco(productid_link, colorid_link, sizeid_link);
			
			//Kiem tra neu chua co thi insert neu co roi thi update
			List<POrderBOMSKU> list_sku = porderbomskuService.getby_porder_and_material_and_color_and_size(porderid_link, productid_link, materialid_link, colorid_link, sizeid_link);
			

			POrderBOMSKU pContractBOMSKU = new POrderBOMSKU();
			if(list_sku.size() > 0) {
				pContractBOMSKU = list_sku.get(0);
				pContractBOMSKU.setAmount(entity.value);
			}
			else {
				pContractBOMSKU.setAmount(entity.value);
				pContractBOMSKU.setCreateddate(new Date());
				pContractBOMSKU.setCreateduserid_link(user.getId());
				pContractBOMSKU.setDescription(entity.data.getDescription());
				pContractBOMSKU.setId((long) 0);
				pContractBOMSKU.setMaterialid_link(materialid_link);
				pContractBOMSKU.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMSKU.setPcontractid_link(pcontractid_link);
				pContractBOMSKU.setProductid_link(productid_link);
				pContractBOMSKU.setLost_ratio(entity.data.getLost_ratio());
				pContractBOMSKU.setSkuid_link(skuid_link);   
				pContractBOMSKU.setPorderid_link(porderid_link);
			}
			porderbomskuService.save(pContractBOMSKU);
			
			//update lai bang bom amount = 0
			POrderBomProduct pContractProductBom = porderbomproductService.findOne(entity.data.getId());
			pContractProductBom.setAmount((float) 0);
			porderbomproductService.update(pContractProductBom);
			
			//update lai bang sku bom
			List<PorderBomColor> listcolor = porderbomcolorService.getby_porder_and_material_and_color(porderid_link, materialid_link, colorid_link);
			
			for (PorderBomColor pColor : listcolor) {
				porderbomcolorService.delete(pColor);
			}			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/sync",method = RequestMethod.POST)
	public ResponseEntity<PorderBom_sync_response> Sync(@RequestBody PorderBom_sync_request entity,HttpServletRequest request ) {
		PorderBom_sync_response response = new PorderBom_sync_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long orgrootid_link = user.getRootorgid_link();
			POrder porder = porderService.findOne(entity.porderid_link);
			long pcontractid_link = porder.getPcontractid_link();
			long productid_link = porder.getProductid_link();
			long porderid_link = entity.porderid_link;
			
			//Xoa porder_bom_product cu
			List<POrderBomProduct> list_porder_bom_product = porderbomproductService.getby_porder(porderid_link);
			for(POrderBomProduct porder_bom_product : list_porder_bom_product) {
				porderbomproductService.delete(porder_bom_product);
			}
			 
			// dong bo tu pcontract_bom_product
			List<PContractProductBom2> list_bom_product = pcontractbomproductService.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			for(PContractProductBom2 bom_product : list_bom_product) {
				POrderBomProduct porder_bom_product = new POrderBomProduct();
				porder_bom_product.setAmount(bom_product.getAmount());
				porder_bom_product.setCreateddate(new Date());
				porder_bom_product.setCreateduserid_link(user.getId());
				porder_bom_product.setId(null);
				porder_bom_product.setLost_ratio(bom_product.getLost_ratio());
				porder_bom_product.setMaterialid_link(bom_product.getMaterialid_link());
				porder_bom_product.setOrgrootid_link(orgrootid_link);
				porder_bom_product.setPcontractid_link(pcontractid_link);
				porder_bom_product.setPorderid_link(porderid_link);
				porder_bom_product.setProductid_link(bom_product.getProductid_link());
				porder_bom_product.setUnitid_link(bom_product.getUnitid_link());
				
				porderbomproductService.save(porder_bom_product);
			}
			
			//Xoa porder_bom_color
			List<PorderBomColor> list_porder_bom_color = porderbomcolorService.getby_porder(porderid_link);
			for(PorderBomColor porderbomcolor : list_porder_bom_color) {
				porderbomcolorService.delete(porderbomcolor);
			}
			
			//dong bo tu pcontract_bom_color
			List<PContractBom2Color> list_bom_color = pcontractbomcolorService.getall_byproduct(pcontractid_link, productid_link);
			for(PContractBom2Color bom_color: list_bom_color) {
				PorderBomColor porderbomcolor =  new PorderBomColor();
				porderbomcolor.setAmount(bom_color.getAmount());
				porderbomcolor.setColorid_link(bom_color.getColorid_link());
				porderbomcolor.setCreateddate(new Date());
				porderbomcolor.setDescription(bom_color.getDescription());
				porderbomcolor.setId(null);
				porderbomcolor.setMaterialid_link(bom_color.getMaterialid_link());
				porderbomcolor.setOrgrootid_link(orgrootid_link);
				porderbomcolor.setPcontractid_link(pcontractid_link);
				porderbomcolor.setPorderid_link(porderid_link);
				porderbomcolor.setProductid_link(productid_link);
				porderbomcolorService.save(porderbomcolor);
			}
			
			//Xoa porder_bom_sku
			List<POrderBOMSKU> list_bom_sku = porderbomskuService.getByPOrderID(porderid_link);
			for(POrderBOMSKU bom_sku : list_bom_sku) {
				porderbomskuService.delete(bom_sku);
			}
			
			//dong bo tu pcontract_bom_sku
			List<PContractBOM2SKU> list_p_bom_sku = pcontractbomskuService.getbypcontract_and_product(pcontractid_link, productid_link);
			for(PContractBOM2SKU bom_sku : list_p_bom_sku) {
				POrderBOMSKU porderbomsku = new POrderBOMSKU();
				porderbomsku.setAmount(bom_sku.getAmount());
				porderbomsku.setCreateddate(new Date());
				porderbomsku.setCreateduserid_link(user.getId());
				porderbomsku.setDescription(bom_sku.getDescription());
				porderbomsku.setId(null);
				porderbomsku.setLost_ratio(bom_sku.getLost_ratio());
				porderbomsku.setMaterialid_link(bom_sku.getMaterialid_link());
				porderbomsku.setOrgrootid_link(orgrootid_link);
				porderbomsku.setPcontractid_link(pcontractid_link);
				porderbomsku.setPorderid_link(porderid_link);
				porderbomsku.setProductid_link(productid_link);
				porderbomsku.setSkuid_link(bom_sku.getSkuid_link());
				porderbomskuService.save(porderbomsku);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PorderBom_sync_response>(response,HttpStatus.OK);
		}catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		    return new ResponseEntity<PorderBom_sync_response>(response, HttpStatus.OK);
		}
	}    
	
	@RequestMapping(value = "/getlist_poder_bomcolor", method = RequestMethod.POST)
	public ResponseEntity<getlist_porderbom_color_response> GetListProductBomColor(HttpServletRequest request,
			@RequestBody getlist_porderbom_color_request entity) {
		getlist_porderbom_color_response response = new getlist_porderbom_color_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
//					.getPrincipal();
			POrder porder = porderService.findOne(entity.porderid_link);
			long pcontractid_link = porder.getPcontractid_link();
			long productid_link = porder.getProductid_link();
			long porderid_link = entity.porderid_link;
			long colorid_link = entity.colorid_link;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();			
			
			List<POrderBomProduct> listbom = porderbomproductService.getby_porder(porderid_link);
			List<PorderBomColor> listbomcolor = porderbomcolorService.getby_porder_and_color(porderid_link, colorid_link);
			List<POrderBOMSKU> listbomsku = porderbomskuService.getby_porder_and_color(porderid_link, colorid_link);
			
			List<Long> List_size = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, (long)30);
//			List<Long> List_size = ppatt_service.getvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, (long) 30);
					//ppbomskuservice.getsize_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for (POrderBomProduct pContractProductBom : listbom) {
				Map<String, String> map = new HashMap<String, String>();
				List<PorderBomColor> listbomcolorclone = new ArrayList<PorderBomColor>(listbomcolor);
				listbomcolorclone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));
				
				Float amount_color = (float) 0;
				if(listbomcolorclone.size() > 0)
					amount_color = listbomcolorclone.get(0).getAmount();
				
				map.put("amount", pContractProductBom.getAmount().toString());
				
				map.put("amount_color", amount_color.toString());
				
				map.put("coKho", pContractProductBom.getCoKho().toString());
				
				map.put("createddate", pContractProductBom.getCreateddate().toString());
				
				map.put("createduserid_link", pContractProductBom.getCreateduserid_link().toString());
				
				map.put("description", pContractProductBom.getDescription()+"");
				
				map.put("id", pContractProductBom.getId().toString());
				
				map.put("lost_ratio", pContractProductBom.getLost_ratio().toString());
				
				map.put("materialid_link", pContractProductBom.getMaterialid_link().toString());
				
				map.put("materialName", pContractProductBom.getMaterialName()+"");
				
				map.put("materialCode", pContractProductBom.getMaterialCode()+"");
				
				map.put("orgrootid_link", pContractProductBom.getOrgrootid_link().toString());
				
				map.put("pcontractid_link", pContractProductBom.getPcontractid_link().toString());
				
				map.put("product_type", pContractProductBom.getProduct_type()+"");
				
				map.put("product_typeName", pContractProductBom.getProduct_typeName().toString());
				
				map.put("productid_link", pContractProductBom.getProductid_link().toString());
				
				map.put("tenMauNPL", pContractProductBom.getTenMauNPL().toString());
				
				map.put("thanhPhanVai", pContractProductBom.getThanhPhanVai().toString());
				
				map.put("unitName", pContractProductBom.getUnitName()+"");
				
				map.put("porderid_link", pContractProductBom.getPorderid_link()+"");
				
				map.put("unitid_link", pContractProductBom.getUnitid_link()+"");
				
				for(Long size : List_size) {
					List<POrderBOMSKU> listbomsku_clone = new ArrayList<POrderBOMSKU>(listbomsku);
					long skuid_link = skuavService.getsku_byproduct_and_valuemau_valueco(productid_link, colorid_link, size);
					listbomsku_clone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()) || 
							!c.getSkuid_link().equals(skuid_link));
					Float amount_size = (float) 0;
					if(listbomsku_clone.size() > 0)
						amount_size = listbomsku_clone.get(0).getAmount();
					map.put(""+size, amount_size+"");
				}
				
				listdata.add(map);
			}
			
			response.data = listdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<getlist_porderbom_color_response>(response, HttpStatus.OK);
	}
}
