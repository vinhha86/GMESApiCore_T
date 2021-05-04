package vn.gpay.gsmart.core.api.pcontractproductbom;

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

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractbomcolor.IPContractBom2ColorService;
import vn.gpay.gsmart.core.pcontractbomcolor.PContractBom2Color;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproductbom.IPContractProductBom2Service;
import vn.gpay.gsmart.core.pcontractproductbom.PContractProductBom2;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.task.ITask_Service;
import vn.gpay.gsmart.core.task_checklist.ITask_CheckList_Service;
import vn.gpay.gsmart.core.task_flow.ITask_Flow_Service;
import vn.gpay.gsmart.core.task_object.ITask_Object_Service;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractproductbom2")
public class PContractProductBom2API {
	@Autowired IPContractProductBom2Service ppbom2service;
	@Autowired IPContractBom2ColorService ppbomcolor2service;
	@Autowired IPContractBOM2SKUService ppbom2skuservice;
	@Autowired IPContractProductAtrributeValueService ppatt_service;
	@Autowired IPContractProductSKUService ppskuService;
	@Autowired IPContractProductService ppService;
	@Autowired ITask_Object_Service taskobjectService;
	@Autowired ITask_Service taskService;
	@Autowired ITask_CheckList_Service checklistService;
	@Autowired ITask_Flow_Service commentService;
	@Autowired ISKU_Service skuService;
	@Autowired IAttributeValueService avService;
	@Autowired IPContract_bom2_npl_poline_Service po_npl_Service;
	
	@RequestMapping(value = "/create_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateProductBom(HttpServletRequest request,
			@RequestBody PContractProductBom_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long productid_link = entity.productid_link;
			long pcontractid_link = entity.pcontractid_link;
			
			for (Long materialid_link : entity.listnpl) {
				//them vao bang product_bom
				List<PContractProductBom2> listBom = ppbom2service.getby_pcontract_product_material(productid_link, pcontractid_link, materialid_link);
				if(listBom.size() == 0) {
					SKU sku = skuService.findOne(materialid_link);
					
					PContractProductBom2 productbom2 = new PContractProductBom2(); 
					productbom2.setProductid_link(productid_link);
					productbom2.setMaterialid_link(materialid_link);
					productbom2.setAmount((float)0);
					productbom2.setLost_ratio((float)0);
					productbom2.setDescription("");
					productbom2.setCreateduserid_link(user.getId());
					productbom2.setCreateddate(new Date());
					productbom2.setOrgrootid_link(user.getRootorgid_link());
					productbom2.setPcontractid_link(pcontractid_link);
					productbom2.setUnitid_link(sku.getUnitid_link());
					
					ppbom2service.save(productbom2);
				}
				
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/confim_bom2", method = RequestMethod.POST)
	public ResponseEntity<confim_bom2_response> ConfimBom2(HttpServletRequest request,
			@RequestBody confim_bom1_request entity) {
		confim_bom2_response response = new confim_bom2_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			List<PContractProduct> list_pp = ppService.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
			if(list_pp.size()>0) {
				PContractProduct pp = list_pp.get(0);
				pp.setIsbom2done(true);
				ppService.save(pp);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<confim_bom2_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBom(HttpServletRequest request,
			@RequestBody PContractProductBom2_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			
			ppbom2service.save(entity.data);
			//Xóa trong bom_color và bom_sku
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			
			//cap nhat lai vao bang bom-sku
			float amount = entity.data.getAmount();
			float lost_ratio = entity.data.getLost_ratio();
			
			List<Long> list_skuid = ppskuService.getsku_bypcontract_and_product(pcontractid_link, productid_link);
			for(Long skuid : list_skuid) {
				SKU sku = skuService.findOne(skuid);
				
				long sizeid_link = sku.getSize_id();
				long colorid = sku.getColor_id();
				List<PContractBOM2SKU> list_pContractBOMSKU = ppbom2skuservice.getall_material_in_productBOMSKU(pcontractid_link, productid_link, sizeid_link, colorid, materialid_link);
				PContractBOM2SKU bomsku = null;
				
				if(list_pContractBOMSKU.size() == 0) {
					if(amount> 0) {
						bomsku = new PContractBOM2SKU();
						bomsku.setAmount(amount);
						bomsku.setCreateddate(new Date());
						bomsku.setCreateduserid_link(user.getId());
						bomsku.setId(null);
						bomsku.setLost_ratio(lost_ratio);
						bomsku.setMaterial_skuid_link(materialid_link);
						bomsku.setOrgrootid_link(user.getRootorgid_link());
						bomsku.setPcontractid_link(pcontractid_link);
						bomsku.setProduct_skuid_link(sku.getId());
						bomsku.setProductid_link(productid_link);
					}
				}
				else {
					bomsku = list_pContractBOMSKU.get(0);
					bomsku.setLost_ratio(lost_ratio);
					if(amount>0)
						bomsku.setAmount(amount);
				}
				
				ppbom2skuservice.save(bomsku);
			}
			
			if(entity.isUpdateBOM) {
				List<PContractBom2Color> listcolor = ppbomcolor2service.getcolor_bymaterial_in_productBOMColor
						(pcontractid_link, productid_link, materialid_link);
				for (PContractBom2Color pContractBOMColor : listcolor) {
					ppbomcolor2service.delete(pContractBOMColor);
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
	
	@RequestMapping(value = "/update_pcontract_productbomcolor", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomColor(HttpServletRequest request,
			@RequestBody PContractBOMColor_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long colorid_link = entity.colorid_link;
			
			List<PContractBom2Color> listcolor = ppbomcolor2service.getall_material_in_productBOMColor
					(pcontractid_link, productid_link, colorid_link, materialid_link);

			PContractBom2Color pContractBOMColor = new PContractBom2Color();
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
				pContractBOMColor.setId((long) 0);
				pContractBOMColor.setMaterialid_link(materialid_link);
				pContractBOMColor.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMColor.setPcontractid_link(pcontractid_link);
				pContractBOMColor.setProductid_link(productid_link);
			}
			ppbomcolor2service.save(pContractBOMColor);
			
			//update lại các màu khác từ chung về chung màu
			PContractProductBom2 pContractProductBom = ppbom2service.findOne(entity.data.getId());
			float amount_color = pContractProductBom.getAmount();
			
			if(amount_color > 0) {
				//Lấy các màu trong sản phẩm của đơn hàng
				List<Long> list_color = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
				list_color.remove(colorid_link);
				
				for(Long colorid : list_color) {
					PContractBom2Color color = new PContractBom2Color();

					color.setAmount(amount_color);
					color.setColorid_link(colorid);
					color.setCreateddate(new Date());
					color.setCreateduserid_link(user.getId());
					color.setDescription(entity.data.getDescription());
					color.setId((long) 0);
					color.setMaterialid_link(materialid_link);
					color.setOrgrootid_link(user.getRootorgid_link());
					color.setPcontractid_link(pcontractid_link);
					color.setProductid_link(productid_link);
					
					ppbomcolor2service.save(color);
				}
			}			
			
			//update lai bang bom amount = 0
			pContractProductBom.setAmount((float) 0);
			ppbom2service.update(pContractProductBom);
			
			//update lai bang sku bom
			float amount = entity.data.getAmount_color();
			float lost_ratio = entity.data.getLost_ratio();
			List<Long> list_skuid = ppskuService.getsku_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for(Long skuid : list_skuid) {
				SKU sku = skuService.findOne(skuid);
				
				long sizeid_link = sku.getSize_id();
				long colorid = sku.getColor_id();
				List<PContractBOM2SKU> list_pContractBOMSKU = ppbom2skuservice.getall_material_in_productBOMSKU(pcontractid_link, productid_link, sizeid_link, colorid, materialid_link);
				PContractBOM2SKU bomsku = null;
				
				if(list_pContractBOMSKU.size() == 0) {
					bomsku = new PContractBOM2SKU();
					bomsku.setAmount(amount);
					bomsku.setCreateddate(new Date());
					bomsku.setCreateduserid_link(user.getId());
					bomsku.setId(null);
					bomsku.setLost_ratio(lost_ratio);
					bomsku.setMaterial_skuid_link(materialid_link);
					bomsku.setOrgrootid_link(user.getRootorgid_link());
					bomsku.setPcontractid_link(pcontractid_link);
					bomsku.setProduct_skuid_link(sku.getId());
					bomsku.setProductid_link(productid_link);
					
				}
				else {
					bomsku = list_pContractBOMSKU.get(0);
					bomsku.setAmount(amount);
				}
				
				ppbom2skuservice.save(bomsku);
			}			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/update_pcontract_productbomsku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> UpdateProductBomSKU(HttpServletRequest request,
			@RequestBody PContractBOMSKU_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long pcontractid_link = entity.data.getPcontractid_link();
			long productid_link = entity.data.getProductid_link();
			long materialid_link = entity.data.getMaterialid_link();
			long sizeid_link = entity.sizeid_link;
			long colorid_link = entity.colorid_link;
			long skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link, sizeid_link, productid_link);
			
			//Kiem tra neu chua co thi insert neu co roi thi update
			List<PContractBOM2SKU> list_sku = ppbom2skuservice.getall_material_in_productBOMSKU(pcontractid_link, productid_link,
					sizeid_link, colorid_link, materialid_link);
			

			PContractBOM2SKU pContractBOMSKU = new PContractBOM2SKU();
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
				pContractBOMSKU.setMaterial_skuid_link(materialid_link);
				pContractBOMSKU.setOrgrootid_link(user.getRootorgid_link());
				pContractBOMSKU.setPcontractid_link(pcontractid_link);
				pContractBOMSKU.setProductid_link(productid_link);
				pContractBOMSKU.setLost_ratio(entity.data.getLost_ratio());
				pContractBOMSKU.setProduct_skuid_link(skuid_link);   
			}
			ppbom2skuservice.save(pContractBOMSKU);
			
			//update lai bang bom amount = 0
			PContractProductBom2 pContractProductBom = ppbom2service.findOne(entity.data.getId());
			pContractProductBom.setAmount((float) 0);
			ppbom2service.update(pContractProductBom);
			
			//update lai bang sku bom
			List<PContractBom2Color> listcolor = ppbomcolor2service.getall_material_in_productBOMColor(
					pcontractid_link, productid_link, colorid_link, materialid_link);
			
			for (PContractBom2Color pColor : listcolor) {
				ppbomcolor2service.delete(pColor);
			}			
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getlist_pcontract_productbom", method = RequestMethod.POST)
	public ResponseEntity<PContractProductBOM2_getbyproduct_response> GetListProductBom(HttpServletRequest request,
			@RequestBody PContractProductBOM_getbyproduct_request entity) {
		PContractProductBOM2_getbyproduct_response response = new PContractProductBOM2_getbyproduct_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			response.data = ppbom2service.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<PContractProductBOM2_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/deletematerial", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> DeleteMaterial(HttpServletRequest request,
			@RequestBody PContractProductBom_delete_material_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long materialid_link = entity.materialid_link;
			
			
			//xoa trong bang pcontract_product_bom2
			List<PContractProductBom2> list_bom2 = ppbom2service.getby_pcontract_product_material(productid_link, pcontractid_link, materialid_link);
			for(PContractProductBom2 bom : list_bom2) {
				ppbom2service.delete(bom);
			}
			
			//Xoa trong bang pcontract_product_color_bom2
			List<PContractBom2Color> list_bom_color2 = ppbomcolor2service.getcolor_bymaterial_in_productBOMColor(pcontractid_link, productid_link, materialid_link);
			for(PContractBom2Color color : list_bom_color2) {
				ppbomcolor2service.delete(color);
			}
			
			//Xoa trong bang pcontract_sku_bom2
			List<PContractBOM2SKU> list_bom_sku2 = ppbom2skuservice.getcolor_bymaterial_in_productBOMSKU(pcontractid_link, productid_link, materialid_link);
			for(PContractBOM2SKU sku : list_bom_sku2) {
				ppbom2skuservice.delete(sku);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getlist_pcontract_productbomcolor", method = RequestMethod.POST)
	public ResponseEntity<PContractProductBOM_getbomcolor_response> GetListProductBomColor(HttpServletRequest request,
			@RequestBody PContractProductBOM_getbomcolor_request entity) {
		PContractProductBOM_getbomcolor_response response = new PContractProductBOM_getbomcolor_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
//					.getPrincipal();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long colorid_link = entity.colorid_link;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();			
			
			List<PContractProductBom2> listbom = ppbom2service.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			List<PContractBom2Color> listbomcolor = ppbomcolor2service.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, (long)0);
			List<PContractBOM2SKU> listbomsku = ppbom2skuservice.getmaterial_bycolorid_link(pcontractid_link, productid_link, colorid_link, 0);
			
			List<Long> List_size = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, (long)30);
//			List<Long> List_size = ppatt_service.getvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, (long) 30);
					//ppbomskuservice.getsize_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for (PContractProductBom2 pContractProductBom : listbom) {
				Map<String, String> map = new HashMap<String, String>();
				List<PContractBom2Color> listbomcolorclone = new ArrayList<PContractBom2Color>(listbomcolor);
				listbomcolorclone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));
				
				Float amount_color = (float) 0;
				if(listbomcolorclone.size() > 0)
					amount_color = listbomcolorclone.get(0).getAmount();
				
				map.put("amount", "0"+pContractProductBom.getAmount());
				
				map.put("amount_color", "0"+amount_color);
				
				map.put("coKho", pContractProductBom.getCoKho()+"");
				
				map.put("createddate", pContractProductBom.getCreateddate()+"");
				
				map.put("createduserid_link", "0"+pContractProductBom.getCreateduserid_link());
				
				map.put("description", pContractProductBom.getDescription()+"");
				
				map.put("id", "0"+pContractProductBom.getId());
				
				map.put("lost_ratio", "0"+pContractProductBom.getLost_ratio());
				
				map.put("materialid_link", "0"+pContractProductBom.getMaterialid_link());
				
				map.put("materialName", pContractProductBom.getMaterialName()+"");
				
				map.put("materialCode", pContractProductBom.getMaterialCode()+"");
				
				map.put("orgrootid_link", "0"+pContractProductBom.getOrgrootid_link());
				
				map.put("pcontractid_link", "0"+pContractProductBom.getPcontractid_link());
				
				map.put("product_type", pContractProductBom.getProduct_type()+"");
				
				map.put("product_typename", pContractProductBom.getProduct_typeName()+"");
				
				map.put("productid_link", pContractProductBom.getProductid_link()+"");
				
				map.put("tenMauNPL", pContractProductBom.getTenMauNPL()+"");
				
				map.put("thanhPhanVai", pContractProductBom.getThanhPhanVai()+"");
				
				map.put("unitName", pContractProductBom.getUnitName()+"");
				
				map.put("unitid_link", "0"+pContractProductBom.getUnitid_link());
				
				if(pContractProductBom.getAmount() == 0 && amount_color == 0) {
					for(Long size : List_size) {
						List<PContractBOM2SKU> listbomsku_clone = new ArrayList<PContractBOM2SKU>(listbomsku);
						long skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link, size, productid_link);
						listbomsku_clone.removeIf(c -> !c.getMaterial_skuid_link().equals(pContractProductBom.getMaterialid_link()) || 
								!c.getProduct_skuid_link().equals(skuid_link));
						Float amount_size = (float) 0;
						if(listbomsku_clone.size() > 0)
							amount_size = listbomsku_clone.get(0).getAmount();
						map.put(""+size, amount_size+"");
					}
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
		return new ResponseEntity<PContractProductBOM_getbomcolor_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getbom_by_product", method = RequestMethod.POST)
	public ResponseEntity<get_bom_by_product_response> GetBomByProduct(HttpServletRequest request,
			@RequestBody get_bom_by_product_request entity) {
		get_bom_by_product_response response = new get_bom_by_product_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long colorid_link = 0;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();		
			
			List<Long> list_colorid = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, AtributeFixValues.ATTR_COLOR);
			
			List<PContractProductBom2> listbom = ppbom2service.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			List<PContractBom2Color> listbomcolor = ppbomcolor2service.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, (long)0);
			List<PContractBOM2SKU> listbomsku = ppbom2skuservice.getmaterial_bycolorid_link(pcontractid_link, productid_link, colorid_link, 0);
			
			List<Long> List_size = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, (long)30);
//			List<Long> List_size = ppatt_service.getvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, (long) 30);
					//ppbomskuservice.getsize_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for (PContractProductBom2 pContractProductBom : listbom) {
				List<PContractBom2Color> listbomcolorclone = new ArrayList<PContractBom2Color>(listbomcolor);
				listbomcolorclone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));
				
				Float amount_color = (float) 0;
				if(listbomcolorclone.size() > 0)
					amount_color = listbomcolorclone.get(0).getAmount();
				
				//Lay ds poline cua sku
				List<PContract_bom2_npl_poline> list_po = po_npl_Service.getby_pcontract_and_npl(pcontractid_link, pContractProductBom.getMaterialid_link());
				String str_po = "";
				for(PContract_bom2_npl_poline po_npl : list_po) {
					if(str_po == "") {
						str_po = po_npl.getPO_Buyer();
					}
					else {
						str_po += ", "+ po_npl.getPO_Buyer();
					}
					
				}
				//Chay de lay tung mau san pham
				for(Long colorid : list_colorid) {
					Map<String, String> map = new HashMap<String, String>();
					
					map.put("amount_color", "0"+amount_color);
					
					map.put("coKho", pContractProductBom.getCoKho()+"");
					
					map.put("createddate", pContractProductBom.getCreateddate()+"");
					
					map.put("createduserid_link", "0"+pContractProductBom.getCreateduserid_link());
					
					map.put("description", pContractProductBom.getDescription_product()+"");
					
					map.put("id", "0"+pContractProductBom.getId());
					
					map.put("lost_ratio", "0"+pContractProductBom.getLost_ratio());
					
					map.put("materialid_link", "0"+pContractProductBom.getMaterialid_link());
					
					map.put("materialName", pContractProductBom.getMaterialName()+"");
					
					map.put("materialCode", pContractProductBom.getMaterialCode()+"");
					
					map.put("orgrootid_link", "0"+pContractProductBom.getOrgrootid_link());
					
					map.put("pcontractid_link", "0"+pContractProductBom.getPcontractid_link());
					
					map.put("product_type", pContractProductBom.getProduct_type()+"");
					
					map.put("product_typename", pContractProductBom.getProduct_typeName()+"");
					
					map.put("productid_link", pContractProductBom.getProductid_link()+"");
					
					map.put("tenMauNPL", pContractProductBom.getTenMauNPL()+"");
					
					map.put("thanhPhanVai", pContractProductBom.getDescription_product()+"");
					
					map.put("unitName", pContractProductBom.getUnitName()+"");
					
					map.put("unitid_link", "0"+pContractProductBom.getUnitid_link());
					
					map.put("colorid_link", "0"+colorid);
					
					map.put("po_line", str_po);
					
					Attributevalue value = avService.findOne(colorid);
					String color_name = value.getValue();
					map.put("color_name", ""+color_name);
					
					Float total_amount = (float) 0;
					int total_size=0;
					if(amount_color == 0) {
						
					}
					for(Long size : List_size) {
						List<PContractBOM2SKU> listbomsku_clone = new ArrayList<PContractBOM2SKU>(listbomsku);
						long skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid, size, productid_link);
						listbomsku_clone.removeIf(c -> !c.getMaterial_skuid_link().equals(pContractProductBom.getMaterialid_link()) || 
								!c.getProduct_skuid_link().equals(skuid_link));
						Float amount_size = (float) 0;
						if(listbomsku_clone.size() > 0)
							amount_size = listbomsku_clone.get(0).getAmount();
						map.put(""+size, amount_size+"");
						
						if (amount_size>0){
							total_amount += amount_size;
							total_size++;
						}
					}
					
					if (total_size>0)
						map.put("amount", "0"+(total_amount/total_size));
					else
						map.put("amount", "0");
					
					listdata.add(map);	
				}
			}
			
			//lay trang thai cua dinh muc
			List<PContractProduct> list_product = ppService.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
			if(list_product.size() > 0) {
				response.isbomdone = list_product.get(0).getIsbom2done() == null ? false : list_product.get(0).getIsbom2done();
			}
			
			response.data = listdata;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<get_bom_by_product_response>(response, HttpStatus.OK);
	}
	
	
	public List<Map<String, String>> GetListProductBomColor(PContractProductBOM_getbomcolor_request entity) {
		PContractProductBOM_getbomcolor_response response = new PContractProductBOM_getbomcolor_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
//					.getPrincipal();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long colorid_link = entity.colorid_link;
			List<Map<String, String>> listdata = new ArrayList<Map<String, String>>();			
			
			List<PContractProductBom2> listbom = ppbom2service.get_pcontract_productBOMbyid(productid_link, pcontractid_link);
			List<PContractBom2Color> listbomcolor = ppbomcolor2service.getall_material_in_productBOMColor(pcontractid_link, productid_link, colorid_link, (long)0);
			List<PContractBOM2SKU> listbomsku = ppbom2skuservice.getmaterial_bycolorid_link(pcontractid_link, productid_link, colorid_link, 0);
			
			List<Long> List_size = ppskuService.getlistvalue_by_product(pcontractid_link, productid_link, (long)30);
//			List<Long> List_size = ppatt_service.getvalueid_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, (long) 30);
					//ppbomskuservice.getsize_bycolor(pcontractid_link, productid_link, colorid_link);
			
			for (PContractProductBom2 pContractProductBom : listbom) {
				Map<String, String> map = new HashMap<String, String>();
				List<PContractBom2Color> listbomcolorclone = new ArrayList<PContractBom2Color>(listbomcolor);
				listbomcolorclone.removeIf(c -> !c.getMaterialid_link().equals(pContractProductBom.getMaterialid_link()));
				
				Float amount_color = (float) 0;
				if(listbomcolorclone.size() > 0)
					amount_color = null!=listbomcolorclone.get(0).getAmount()?listbomcolorclone.get(0).getAmount():0;
				
				map.put("amount", "0"+pContractProductBom.getAmount());
				
				map.put("amount_color", "0"+amount_color);
				
				map.put("coKho", pContractProductBom.getCoKho()+"");
				
				map.put("createddate", pContractProductBom.getCreateddate()+"");
				
				map.put("createduserid_link", "0"+pContractProductBom.getCreateduserid_link());
				
				map.put("description", pContractProductBom.getDescription()+"");
				
				map.put("id", "0"+pContractProductBom.getId());
				
				map.put("lost_ratio", "0"+pContractProductBom.getLost_ratio());
				
				map.put("materialid_link", "0"+pContractProductBom.getMaterialid_link());
				
				map.put("materialName", pContractProductBom.getMaterialName()+"");
				
				map.put("materialCode", pContractProductBom.getMaterialCode()+"");
				
				map.put("orgrootid_link", "0"+pContractProductBom.getOrgrootid_link());
				
				map.put("pcontractid_link", "0"+pContractProductBom.getPcontractid_link());
				
				map.put("product_type", pContractProductBom.getProduct_type()+"");
				
				map.put("product_typename", pContractProductBom.getProduct_typeName()+"");
				
				map.put("productid_link", pContractProductBom.getProductid_link()+"");
				
				map.put("tenMauNPL", pContractProductBom.getTenMauNPL()+"");
				
				map.put("thanhPhanVai", pContractProductBom.getThanhPhanVai()+"");
				
				map.put("unitName", pContractProductBom.getUnitName()+"");
				
				map.put("unitid_link", "0"+pContractProductBom.getUnitid_link());
				
				for(Long size : List_size) {
					List<PContractBOM2SKU> listbomsku_clone = new ArrayList<PContractBOM2SKU>(listbomsku);
					long skuid_link = ppbom2skuservice.getskuid_link_by_color_and_size(colorid_link, size, productid_link);
					listbomsku_clone.removeIf(c -> !c.getMaterial_skuid_link().equals(pContractProductBom.getMaterialid_link()) || 
							!c.getProduct_skuid_link().equals(skuid_link));
					Float amount_size = (float) 0;
					if(listbomsku_clone.size() > 0)
						amount_size = listbomsku_clone.get(0).getAmount();
					map.put(""+size, amount_size+"");
				}
				
				listdata.add(map);
			}
			
			return listdata;
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return null;
		}
	}
	
	public List<PContractBOM2SKU> getBOM_By_PContractSKU(Long pcontractid_link, Long skuid_link){
		return ppbom2skuservice.getBOM_By_PContractSKU(pcontractid_link, skuid_link);
	}
}
