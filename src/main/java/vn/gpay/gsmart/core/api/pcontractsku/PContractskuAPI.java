package vn.gpay.gsmart.core.api.pcontractsku;

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

import vn.gpay.gsmart.core.api.pcontract_po.pcontractsku_getby_po_product_mat_request;
import vn.gpay.gsmart.core.api.pcontract_po.pcontractsku_getby_po_product_request;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.IPContract_bom2_npl_poline_sku_Service;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractattributevalue.PContractAttributeValue;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKUBinding;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder.POrder;
import vn.gpay.gsmart.core.porder_grant.IPOrderGrant_SKUService;
import vn.gpay.gsmart.core.porder_grant.POrderGrant_SKU;
import vn.gpay.gsmart.core.porder_product_sku.get_sku_by_porder_requets;
import vn.gpay.gsmart.core.porder_product_sku.get_sku_by_porder_response;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.Common;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractsku")
public class PContractskuAPI {
	@Autowired IPContractProductSKUService pskuservice;
	@Autowired IPContractProductAtrributeValueService pcpavservice;
	@Autowired ISKU_AttributeValue_Service skuavService;
	@Autowired IPOrder_Service porder_Service;
	@Autowired Common commonService;
	@Autowired IPContract_POService poService;
	@Autowired IPOrderGrant_SKUService porderGrantSkuService;
	@Autowired IPContract_bom2_npl_poline_sku_Service po_npl_sku_Service;
	
	@RequestMapping(value = "/getbyporder",method = RequestMethod.POST)
	public ResponseEntity<get_sku_by_porder_response> GetByPOrder
	(HttpServletRequest request, @RequestBody get_sku_by_porder_requets entity ) {
		get_sku_by_porder_response response = new get_sku_by_porder_response();
		try {
			long porderid_link = entity.porderid_link;
			POrder porder = porder_Service.findOne(porderid_link);
			Long parentpo_id_link = porder.getPOParentid_link();
			Long productid_link = porder.getProductid_link();
			
			response.data = pskuservice.gettotalsku_bypo_parent_and_product(parentpo_id_link, productid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<get_sku_by_porder_response>(response, HttpStatus.OK);
	}
	
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
	
	@RequestMapping(value = "/getby_po_product",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> SKU_GetbyProduct_and_PO
	(HttpServletRequest request, @RequestBody pcontractsku_getby_po_product_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			long orgrootid_link = user.getRootorgid_link();
			
			long pcontract_poid_link = entity.pcontract_poid_link;
			long productid_link = entity.productid_link;
			
			response.data = pskuservice.getbypo_and_product(pcontract_poid_link ,productid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getby_po_product_linesku",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> SKU_GetbyProduct_and_PO_andMaterial
	(HttpServletRequest request, @RequestBody pcontractsku_getby_po_product_mat_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			long orgrootid_link = user.getRootorgid_link();
			
			long pcontract_poid_link = entity.pcontract_poid_link;
			long productid_link = entity.productid_link;
//			long material_skuid_link = entity.material_skuid_link;
			
			response.data = pskuservice.getbypo_and_product(pcontract_poid_link ,productid_link);
//			List<PContract_bom2_npl_poline_sku> list_poline_sku = po_npl_sku_Service.getby_po(orgrootid_link, pcontract_poid_link, material_skuid_link, productid_link);
//			for(PContractProductSKU sku : response.data) {
//				for(PContract_bom2_npl_poline_sku npl_sku : list_poline_sku) {
//					if(npl_sku.getProduct_skuid_link().equals(sku.getSkuid_link())) {
//						sku.setPquantity_total(npl_sku.getQuantity());
//					}
//				}
//			}
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getbypcontract_po",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> SKU_GetbyPO
	(HttpServletRequest request, @RequestBody PContractSKU_getbypo_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long pcontract_poid_link = entity.pcontract_poid_link;
			
			response.data = pskuservice.getlistsku_bypo_and_pcontract(orgrootid_link, pcontract_poid_link, pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	//Lay danh sach cac SKU cua PO chua dc phan cho POrder nao
	@RequestMapping(value = "/getposku_free",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> getPOSKU_Free
	(HttpServletRequest request, @RequestBody PContractSKU_getbypo_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long pcontract_poid_link = entity.pcontract_poid_link;
			
			response.data = pskuservice.getlistsku_bypo_and_pcontract_free(orgrootid_link, pcontract_poid_link, pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_getbyproduct_response>(response, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getposku_free_byproduct",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_getbyproduct_response> getPOSKU_Free_ByProduct
	(HttpServletRequest request, @RequestBody PContractSKU_getbypo_request entity ) {
		PContractSKU_getbyproduct_response response = new PContractSKU_getbyproduct_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//			long orgrootid_link = user.getRootorgid_link();
//			long pcontractid_link = entity.pcontractid_link;
			long pcontract_poid_link = entity.pcontract_poid_link;
			long productid_link = entity.productid_link;
			
			response.data = pskuservice.getPOSKU_Free_ByProduct(productid_link, pcontract_poid_link);
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
	public ResponseEntity<PContractSKU_update_response> SKU_Update
	(HttpServletRequest request, @RequestBody PContractSKU_update_request entity ) {
		PContractSKU_update_response response = new PContractSKU_update_response();
		try {
			PContractProductSKU sku = entity.data;
			if(entity.isupdte_amount) {
				sku.setPquantity_production(commonService.Calculate_pquantity_production(sku.getPquantity_porder()));
				sku.setPquantity_total(sku.getPquantity_production() + sku.getPquantity_sample());
			}
			sku = pskuservice.save(sku);
			response.amount = sku.getPquantity_production();
			
			PContract_PO po = poService.findOne(sku.getPcontract_poid_link());
			response.checkamount = po.getCheckamount();
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_update_response>(response, HttpStatus.OK);
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
			long pcontract_poid_link = entity.pcontract_poid_link;
			long orgrootid_link = user.getRootorgid_link();
			
			 for(Long skuid_link : entity.listskuid_link) {
				 //Kiem tra sku da co trong don hang chua? chua co thi moi insert vao
				 List<PContractProductSKU> list = pskuservice.getlistsku_bysku_and_product_PO(skuid_link, pcontract_poid_link, productid_link);
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
					 psku.setPcontract_poid_link(pcontract_poid_link);
					 
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
	
	@RequestMapping(value = "/getSkuByPcontractPoForPorderDetail",method = RequestMethod.POST)
	public ResponseEntity<PContractSKU_binding_response> getSkuByPcontractPoForPorderDetail
	(HttpServletRequest request, @RequestBody PContractSKU_getbypo_request entity ) {
		PContractSKU_binding_response response = new PContractSKU_binding_response();
		try {
//			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			Long pcontract_poid_link = entity.pcontract_poid_link;
			Long porderid_link = entity.porderid_link;
			
			POrder porder = porder_Service.findOne(porderid_link);
			Long productid_link = porder.getProductid_link();
			
			List<PContractProductSKU> listPContractProductSKU = pskuservice.getbypo_and_product(
					pcontract_poid_link, productid_link
					);
			List<PContractProductSKUBinding> data = new ArrayList<PContractProductSKUBinding>();
			
			for(PContractProductSKU pcontractProductSKU : listPContractProductSKU) {
				PContractProductSKUBinding temp = new PContractProductSKUBinding();

				temp.setId(pcontractProductSKU.getId());
				temp.setOrgrootid_link(pcontractProductSKU.getOrgrootid_link());
				temp.setPcontractid_link(pcontractProductSKU.getPcontractid_link());
				temp.setPcontract_poid_link(pcontractProductSKU.getPcontract_poid_link());
				temp.setProductid_link(pcontractProductSKU.getProductid_link());
				temp.setSkuid_link(pcontractProductSKU.getSkuid_link());
				temp.setPquantity_sample(pcontractProductSKU.getPquantity_sample());
				temp.setPquantity_porder(pcontractProductSKU.getPquantity_porder());
				temp.setPquantity_total(pcontractProductSKU.getPquantity_total());
				temp.setSkuName(pcontractProductSKU.getSkuName());
				temp.setSkuCode(pcontractProductSKU.getSkuCode());
				temp.setSortValue(pcontractProductSKU.getSort_value());
				temp.setMauSanPham(pcontractProductSKU.getMauSanPham());
				temp.setCoSanPham(pcontractProductSKU.getCoSanPham());
				temp.setSizeId(pcontractProductSKU.getSizeid_link());
				temp.setColorId(pcontractProductSKU.getColor_id());
				
				// tinh pquantity_granted, pquantity_ungranted
				temp.setPquantity_granted(0);
				temp.setPquantity_ungranted(0);
				List<POrderGrant_SKU> listPorderGrantSku = porderGrantSkuService.getByPContractPOAndSKU(pcontract_poid_link, temp.getSkuid_link());
				Integer granted = 0;
				for(POrderGrant_SKU porderGrantSku : listPorderGrantSku) {
					granted += porderGrantSku.getGrantamount();
				}
				temp.setPquantity_granted(granted);
				temp.setPquantity_ungranted(temp.getPquantity_total() - granted);
				data.add(temp);
			}
			
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		
		return new ResponseEntity<PContractSKU_binding_response>(response, HttpStatus.OK);
	}
}
