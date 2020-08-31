package vn.gpay.gsmart.core.api.pcontractattributevalue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
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

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractattributevalue.PContractAttributeValueBinding;
import vn.gpay.gsmart.core.pcontractproductcolor.IPContractProductColorService;
import vn.gpay.gsmart.core.pcontractproductcolor.PContractProductColor;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.productattributevalue.IProductAttributeService;
import vn.gpay.gsmart.core.productattributevalue.ProductAttributeValue;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractattvalue")
public class PContractAttrValueAPI {
	@Autowired IPContractProductAtrributeValueService ppavservice;
	@Autowired IProductService productService;
	@Autowired IProductAttributeService productavService;
	@Autowired IPContractService pcontractService;
	@Autowired IPContractProductColorService ppcolorService;
	@Autowired ISKU_AttributeValue_Service skuavService;
	@Autowired ISKU_Service skuService;
	@Autowired IPContractProductSKUService ppskuService;
	@Autowired IProductAttributeService pavService;
	@Autowired IAttributeValueService attributeValueService;
	
	@RequestMapping(value = "/getattributebyproduct",method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_getbyproduct_response> Attribute_GetbyProduct(HttpServletRequest request, @RequestBody PContractProductAttValue_getbyproduct_request entity ) {
		PContractProduct_getbyproduct_response response = new PContractProduct_getbyproduct_response();
		try {
			long productid_link = entity.productid_link;
			
//			List<PContractAttributeValue> lstpav = ppavservice.getattribute_by_product_and_pcontract(orgrootid_link, pcontractid_link, productid_link);
			List<ProductAttributeValue> listpav = pavService.getall_byProductId(productid_link);
			response.data = new ArrayList<PContractAttributeValueBinding>();
			for (ProductAttributeValue productAttributeValue : listpav) {
				PContractAttributeValueBinding binding = new PContractAttributeValueBinding();
				
				binding.setAttributeid_link(productAttributeValue.getAttributeid_link());
				binding.setAttributeName(productAttributeValue.getAttributeName());
				boolean isExist = false;
				
				for (PContractAttributeValueBinding obj : response.data) {
					if (binding.getAttributeid_link() == obj.getAttributeid_link()){
						isExist = true;
						break;
					}
				}
				
				if(!isExist) {
					response.data.add(binding);
				}
			}
			
			for (PContractAttributeValueBinding binding : response.data) {
				String name = "";
				String id = "";
				for (ProductAttributeValue productAttributeValue : listpav) {
					if (binding.getAttributeid_link().equals(productAttributeValue.getAttributeid_link())) {
						if (name.equals("")) {
							name += productAttributeValue.getAttributeValueName();
							id += productAttributeValue.getAttributevalueid_link() == 0 ? "" : productAttributeValue.getAttributevalueid_link();
						} else {
							name += ", " + productAttributeValue.getAttributeValueName();
							id += "," + productAttributeValue.getAttributevalueid_link();
						}
					}
				}
				binding.setAttributeValueName(name);
				binding.setList_attributevalueid(id);
			}
			
			for (PContractAttributeValueBinding binding : response.data) {
				String[] idarray = binding.getList_attributevalueid().split(",");
				List<String> idliststring = new ArrayList<>(Arrays.asList(idarray));
				List<Long> idlist = new ArrayList<Long>();
				for(String idnum : idliststring) {
					if(!idnum.equals("")) {
						idlist.add(Long.parseLong(idnum));
					}
				}
				
				List<Attributevalue> attributeVals = new ArrayList<>();
				for(Long idattributeval : idlist) {
					attributeVals.add(attributeValueService.findOne(idattributeval));
				}
				Comparator<Attributevalue> compareBySortValue = (Attributevalue a1, Attributevalue a2) -> a1.getSortvalue().compareTo( a2.getSortvalue());
				Collections.sort(attributeVals, compareBySortValue);
				
				binding.setAttributeValueName("");
				String name = "";
				for(Attributevalue attributeVal : attributeVals) {
					if (name.equals("")) {
						name += attributeVal.getValue();
					}else {
						name += ", " + attributeVal.getValue();
					}
				}
				binding.setAttributeValueName(name);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractProduct_getbyproduct_response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<PContractProduct_getbyproduct_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getvaluebyproduct",method = RequestMethod.POST)
	public ResponseEntity<PContractAttributeValue_getbyproduct_response> Attributevalue_GetbyProduct(HttpServletRequest request, @RequestBody PContractAttributeValue_getvalue_byprouct_request entity ) {
		PContractAttributeValue_getbyproduct_response response = new PContractAttributeValue_getbyproduct_response();
		try {
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
			response.data = ppavservice.getvalue_by_product_and_pcontract_and_attribute(pcontractid_link, productid_link, attributeid_link);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractAttributeValue_getbyproduct_response>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<PContractAttributeValue_getbyproduct_response>(HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createattribute",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Attribute_Create(HttpServletRequest request, @RequestBody PContractAttrbuteValue_insertattribute_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long productid_link = entity.productid_link;
			
			for (long attributeid_link : entity.listId) {
//				PContractAttributeValue pcav = new PContractAttributeValue();
//				pcav.setId((long)0);
//				pcav.setOrgrootid_link(orgrootid_link);
//				pcav.setAttributeid_link(attributeid_link);
//				pcav.setPcontractid_link(pcontractid_link);
//				pcav.setProductid_link(productid_link);
//				pcav.setAttributevalueid_link((long)0);
//				ppavservice.save(pcav);
				
				ProductAttributeValue pav = new ProductAttributeValue();
				pav.setId(null);
				pav.setAttributeid_link(attributeid_link);
				pav.setAttributevalueid_link((long)0);
				pav.setIsDefault(false);
				pav.setOrgrootid_link(orgrootid_link);
				pav.setProductid_link(productid_link);
				
				pavService.save(pav);
			}
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_EXCEPTION));
		    return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/deleteAtribute", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Attribute_delete(HttpServletRequest request,
			@RequestBody PContractAttributeValue_deleteatt_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
//			List<PContractAttributeValue> lstValue = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, attributeid_link);
			List<ProductAttributeValue> lstValue = pavService.getList_byAttId(attributeid_link, productid_link);
			for (ProductAttributeValue productAttributeValue : lstValue) {
				pavService.delete(productAttributeValue);
			}
			
			if(attributeid_link==4 || attributeid_link == 30) {
				//Nếu xóa thuộc tính màu thì xóa cả trong bảng pcontract_product_color
				if(attributeid_link == 4) {
					List<PContractProductColor> lstcolor = ppcolorService.getcolor_by_pcontract_and_product(orgrootid_link, pcontractid_link, productid_link);
					for (PContractProductColor pContractProductColor : lstcolor) {
						ppcolorService.delete(pContractProductColor);
					}
				}
				//Xóa trong bảng sku
				//Lấy danh sách sku của sản phẩm trong đơn hàng
				List<PContractProductSKU> listsku = ppskuService.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				for (PContractProductSKU pContractProductSKU : listsku) {
					ppskuService.delete(pContractProductSKU);
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
	
	@RequestMapping(value = "/createattributevalue",method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValue_Create(HttpServletRequest request, @RequestBody PConrtactAttributeValue_createvalue_request entity ) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long productid_link = entity.productid_link;
			long attributeid_link = entity.attributeid_link;
			
//			List<PContractAttributeValue> lst = ppavservice.getvalue_by_product_and_pcontract_and_attribute(orgrootid_link, pcontractid_link, productid_link, attributeid_link);
			List<ProductAttributeValue> lst = pavService.getList_byAttId(attributeid_link, productid_link);
			for (ProductAttributeValue productattributevalue : lst) {
				if(productattributevalue.getAttributevalueid_link() > 0)
				ppavservice.deleteById(productattributevalue.getId());
			}

			for (Long id : entity.listAdd) {
//				PContractAttributeValue pcontractattributevalue = new PContractAttributeValue();
//				
//				pcontractattributevalue.setId((long)0);
//				pcontractattributevalue.setAttributeid_link(attributeid_link);
//				pcontractattributevalue.setAttributevalueid_link(id);
//				pcontractattributevalue.setOrgrootid_link(orgrootid_link);
//				pcontractattributevalue.setPcontractid_link(pcontractid_link);
//				pcontractattributevalue.setProductid_link(productid_link);
//				
//				ppavservice.save(pcontractattributevalue);
				
				ProductAttributeValue pav = new ProductAttributeValue();
				pav.setAttributeid_link(attributeid_link);
				pav.setAttributevalueid_link(id);
				pav.setId(null);
				pav.setIsDefault(false);
				pav.setOrgrootid_link(orgrootid_link);
				pav.setProductid_link(productid_link);
				
				pavService.save(pav);
			}
			
			
			Product product = productService.findOne(entity.productid_link);

			List<ProductAttributeValue> lstmau = new ArrayList<ProductAttributeValue>();
			List<ProductAttributeValue> lstco = lstmau;

			List<SKU_Attribute_Value> lstSKUMau = new ArrayList<SKU_Attribute_Value>();
			List<SKU_Attribute_Value> lstSKUCo = lstSKUMau;

			// Nếu là sản phẩm thì sinh sku theo màu và cỡ
			if (product.getProducttypeid_link() > 9 && product.getProducttypeid_link() < 20) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || entity.attributeid_link == AtributeFixValues.ATTR_SIZE) {
					lstmau = pavService.getList_byAttId(AtributeFixValues.ATTR_COLOR, entity.productid_link);
					lstco = pavService.getList_byAttId(AtributeFixValues.ATTR_SIZE, entity.productid_link);
				}
			} else if (product.getProducttypeid_link() > 19) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || entity.attributeid_link == AtributeFixValues.ATTR_SIZEWIDTH) {
					lstmau = pavService.getList_byAttId(AtributeFixValues.ATTR_COLOR, entity.productid_link);
					lstco = pavService.getList_byAttId(AtributeFixValues.ATTR_SIZEWIDTH, entity.productid_link);
				}
			}

			for (ProductAttributeValue pavMau : lstmau) {
				for (ProductAttributeValue pavCo : lstco) {
					// Lấy danh sách SKU theo màu cua product
					lstSKUMau = skuavService.getlist_byProduct_and_value(entity.productid_link,
							pavMau.getAttributevalueid_link());
					// Lấy danh sách SKU theo cỡ cua product
					lstSKUCo = skuavService.getlist_byProduct_and_value(entity.productid_link,
							pavCo.getAttributevalueid_link());
					Long skuid_link = (long) 0;
					// Lấy SKU của màu và cỡ
					for (SKU_Attribute_Value savMau : lstSKUMau) {
						for (SKU_Attribute_Value savCo : lstSKUCo) {
							long skuMau = savMau.getSkuid_link();
							long skuCo = savCo.getSkuid_link();
							if (skuMau == skuCo) {
								skuid_link = savMau.getSkuid_link();
								break;
							}
						}
						if (skuid_link > 0)
							break;
					}

					// Nếu không có skuid và thuộc tính đều có giá trị thì phải thêm vào bảng sku
					// trước
					if (skuid_link == 0) {
						if (pavMau.getAttributevalueid_link() != 0 && pavCo.getAttributevalueid_link() != 0) {
							SKU sku = new SKU();
							sku.setId(skuid_link);
							sku.setCode(genCodeSKU(product));
							sku.setName(product.getName());
							sku.setProductid_link(product.getId());
							sku.setOrgrootid_link(user.getRootorgid_link());
							sku.setSkutypeid_link(product.getProducttypeid_link());

							sku = skuService.save(sku);
							skuid_link = sku.getId();

							// Them vao bang sku_attribute_value
							SKU_Attribute_Value savMau = new SKU_Attribute_Value();
							savMau.setId((long) 0);
							savMau.setAttributevalueid_link(pavMau.getAttributevalueid_link());
							savMau.setAttributeid_link(pavMau.getAttributeid_link());
							savMau.setOrgrootid_link(user.getRootorgid_link());
							savMau.setSkuid_link(skuid_link);
							savMau.setUsercreateid_link(user.getId());
							savMau.setTimecreate(new Date());

							skuavService.save(savMau);

							SKU_Attribute_Value savCo = new SKU_Attribute_Value();
							savCo.setId((long) 0);
							savCo.setAttributevalueid_link(pavCo.getAttributevalueid_link());
							savCo.setAttributeid_link(pavCo.getAttributeid_link());
							savCo.setOrgrootid_link(user.getRootorgid_link());
							savCo.setSkuid_link(skuid_link);
							savCo.setUsercreateid_link(user.getId());
							savCo.setTimecreate(new Date());

							skuavService.save(savCo);
						}
					}

				}
			}
			List<SKU_Attribute_Value> listSKUMau = new ArrayList<SKU_Attribute_Value>();
			List<SKU_Attribute_Value> listSKUCo = listSKUMau;

			if (product.getProducttypeid_link() > 9 && product.getProducttypeid_link() < 20) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || entity.attributeid_link == AtributeFixValues.ATTR_SIZE) {
					listSKUMau = skuavService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_COLOR);
					listSKUCo = skuavService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_SIZE);
				}
			} else if (product.getProducttypeid_link() > 19) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || entity.attributeid_link == AtributeFixValues.ATTR_SIZEWIDTH) {
					listSKUMau = skuavService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_COLOR);
					listSKUCo = skuavService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_SIZEWIDTH);
				}
			}
			// Xoa SKU sau khi thay doi gia tri cua mau va co
			for (SKU_Attribute_Value savMau : listSKUMau) {
				boolean check = false;
				for (ProductAttributeValue pavMau : lstmau) {
					long avidSKU = savMau.getAttributevalueid_link();
					long avidProduct = pavMau.getAttributevalueid_link();
					if (avidSKU == avidProduct) {
						check = true;
						break;
					}
				}
				if (!check) {
					skuService.deleteById(savMau.getSkuid_link());
					skuavService.delete(savMau);
				}
			}

			for (SKU_Attribute_Value savCo : listSKUCo) {
				boolean check = false;
				for (ProductAttributeValue pavCo : lstco) {
					long avidSKU = savCo.getAttributevalueid_link();
					long avidProduct = pavCo.getAttributevalueid_link();
					if (avidSKU == avidProduct) {
						check = true;
						break;
					}
				}

				if (!check) {
					skuService.deleteById(savCo.getSkuid_link());
					skuavService.delete(savCo);
				}

			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
		}
		catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
		}
		return new ResponseEntity<ResponseBase>(response,HttpStatus.OK);
	}
	
	private String genCodeSKU(Product product) {
		List<SKU> lstSKU = skuService.getlist_byProduct(product.getId());
		if (lstSKU.size() == 0) {
			return product.getCode() + "_" + "1";
		}
		String old_code = lstSKU.get(0).getCode();
		String[] obj = old_code.split("_");
		int a = Integer.parseInt(obj[1]);
		return product.getCode() + "_" + (a + 1);
	}
}
