package vn.gpay.gsmart.core.api.productattributevalue;

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

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.attributevalue.IAttributeValueService;
import vn.gpay.gsmart.core.base.ResponseBase;
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
@RequestMapping("/api/v1/productattribute")
public class ProductAttributeValueAPI {
	@Autowired
	IProductAttributeService pavService;
	@Autowired
	IAttributeValueService avService;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	IProductService productService;
	@Autowired
	ISKU_AttributeValue_Service savService;

	@RequestMapping(value = "/createvalue", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValueCreate(@RequestBody ProductAttribute_createvalue_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		List<ProductAttributeValue> list_old = pavService.getList_byAttId(entity.attributeid_link,
				entity.productid_link);
		try {
			for (ProductAttributeValue productAttributeValue : list_old) {
				pavService.delete(productAttributeValue);
			}
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			for (Long id : entity.listvalue) {
				ProductAttributeValue data = new ProductAttributeValue();
				data.setId(null);
				data.setAttributeid_link(entity.attributeid_link);
				data.setAttributevalueid_link(id);
				data.setProductid_link(entity.productid_link);
				data.setOrgrootid_link(user.getRootorgid_link());

				pavService.save(data);
			}

//			Long ID_Mau = (long) 4, ID_Co = (long) 30;
//			Long ID_Mau_NPL = (long) 35, ID_Co_Kho = (long) 36;

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
					Long skuid_link = (long) 0;
					// Lấy danh sách SKU theo màu cua product
					lstSKUMau = savService.getlist_byProduct_and_value(entity.productid_link,
							pavMau.getAttributevalueid_link());
					// Lấy danh sách SKU theo cỡ cua product
					lstSKUCo = savService.getlist_byProduct_and_value(entity.productid_link,
							pavCo.getAttributevalueid_link());
					if(lstSKUMau.size() ==0 || lstSKUCo.size() ==0)
						skuid_link = (long)-1;
					// Lấy SKU của màu và cỡ
					for (SKU_Attribute_Value savMau : lstSKUMau) {
						for (SKU_Attribute_Value savCo : lstSKUCo) {
							if(savMau.getIsdefaultvalue() || savCo.getIsdefaultvalue()) {
								skuid_link = (long)-1;
							}
							else {
								long skuMau = savMau.getSkuid_link();
								long skuCo = savCo.getSkuid_link();
								if (skuMau == skuCo) {
									skuid_link = savMau.getSkuid_link();
									break;
								}
							}							
						}
						if (skuid_link != 0)
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

							savService.save(savMau);

							SKU_Attribute_Value savCo = new SKU_Attribute_Value();
							savCo.setId((long) 0);
							savCo.setAttributevalueid_link(pavCo.getAttributevalueid_link());
							savCo.setAttributeid_link(pavCo.getAttributeid_link());
							savCo.setOrgrootid_link(user.getRootorgid_link());
							savCo.setSkuid_link(skuid_link);
							savCo.setUsercreateid_link(user.getId());
							savCo.setTimecreate(new Date());

							savService.save(savCo);
						}
					}

				}
			}
			List<SKU_Attribute_Value> listSKUMau = new ArrayList<SKU_Attribute_Value>();
			List<SKU_Attribute_Value> listSKUCo = listSKUMau;

			if (product.getProducttypeid_link() > 9 && product.getProducttypeid_link() < 20) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || entity.attributeid_link == AtributeFixValues.ATTR_SIZE) {
					listSKUMau = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_COLOR);
					listSKUCo = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_SIZE);
				}
			} else if (product.getProducttypeid_link() > 19) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || entity.attributeid_link == AtributeFixValues.ATTR_SIZEWIDTH) {
					listSKUMau = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_COLOR);
					listSKUCo = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_SIZEWIDTH);
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
					savService.delete(savMau);
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
					savService.delete(savCo);
				}

			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/createvalue_fromsearch", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeValueCreateFromSearch(@RequestBody ProductAttributeValue_create_fromSearch_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {

			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			List<ProductAttributeValue> list_color_old = pavService.getList_byAttId(AtributeFixValues.ATTR_COLOR,
					entity.productid_link);
			for (ProductAttributeValue productAttributeValue : list_color_old) {
				pavService.delete(productAttributeValue);
			}
			
			List<ProductAttributeValue> list_size_old = pavService.getList_byAttId(AtributeFixValues.ATTR_SIZE,
					entity.productid_link);
			for (ProductAttributeValue productAttributeValue : list_size_old) {
				pavService.delete(productAttributeValue);
			}
			
			for (Long color : entity.listMau) {
				ProductAttributeValue data = new ProductAttributeValue();
				data.setId(null);
				data.setAttributeid_link(AtributeFixValues.ATTR_COLOR);
				data.setAttributevalueid_link(color);
				data.setProductid_link(entity.productid_link);
				data.setOrgrootid_link(user.getRootorgid_link());

				pavService.save(data);	
			}
			
			for (Long size : entity.listCo) {
				ProductAttributeValue data = new ProductAttributeValue();
				data.setId(null);
				data.setAttributeid_link(AtributeFixValues.ATTR_SIZE);
				data.setAttributevalueid_link(size);
				data.setProductid_link(entity.productid_link);
				data.setOrgrootid_link(user.getRootorgid_link());

				pavService.save(data);	
			}

//			Long ID_Mau = (long) 4, ID_Co = (long) 30;
//			Long ID_Mau_NPL = (long) 35, ID_Co_Kho = (long) 36;

			Product product = productService.findOne(entity.productid_link);

			List<ProductAttributeValue> lstmau = new ArrayList<ProductAttributeValue>();
			List<ProductAttributeValue> lstco = lstmau;

			List<SKU_Attribute_Value> lstSKUMau = new ArrayList<SKU_Attribute_Value>();
			List<SKU_Attribute_Value> lstSKUCo = lstSKUMau;

			// Nếu là sản phẩm thì sinh sku theo màu và cỡ
			if (product.getProducttypeid_link() > 9 && product.getProducttypeid_link() < 20) {
				lstmau = pavService.getList_byAttId(AtributeFixValues.ATTR_COLOR, entity.productid_link);
				lstco = pavService.getList_byAttId(AtributeFixValues.ATTR_SIZE, entity.productid_link);
			} else if (product.getProducttypeid_link() > 19) {
				lstmau = pavService.getList_byAttId(AtributeFixValues.ATTR_COLOR, entity.productid_link);
				lstco = pavService.getList_byAttId(AtributeFixValues.ATTR_SIZEWIDTH, entity.productid_link);
			}

			for (ProductAttributeValue pavMau : lstmau) {
				for (ProductAttributeValue pavCo : lstco) {	
					Long skuid_link = (long) 0;
					
					// Lấy danh sách SKU theo màu cua product
					lstSKUMau = savService.getlist_byProduct_and_value(entity.productid_link,
							pavMau.getAttributevalueid_link());
					// Lấy danh sách SKU theo cỡ cua product
					lstSKUCo = savService.getlist_byProduct_and_value(entity.productid_link,
							pavCo.getAttributevalueid_link());
					if(lstSKUMau.size() ==0 || lstSKUCo.size() ==0)
						skuid_link = (long)-1;
					// Lấy SKU của màu và cỡ
					for (SKU_Attribute_Value savMau : lstSKUMau) {
						for (SKU_Attribute_Value savCo : lstSKUCo) {
							if(savMau.getIsdefaultvalue() || savCo.getIsdefaultvalue()) {
								skuid_link = (long)-1;
							}
							else {
								long skuMau = savMau.getSkuid_link();
								long skuCo = savCo.getSkuid_link();
								if (skuMau == skuCo) {
									skuid_link = savMau.getSkuid_link();
									break;
								}
							}							
						}
						if (skuid_link != 0)
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

							savService.save(savMau);

							SKU_Attribute_Value savCo = new SKU_Attribute_Value();
							savCo.setId((long) 0);
							savCo.setAttributevalueid_link(pavCo.getAttributevalueid_link());
							savCo.setAttributeid_link(pavCo.getAttributeid_link());
							savCo.setOrgrootid_link(user.getRootorgid_link());
							savCo.setSkuid_link(skuid_link);
							savCo.setUsercreateid_link(user.getId());
							savCo.setTimecreate(new Date());

							savService.save(savCo);
						}
					}

				}
			}
			List<SKU_Attribute_Value> listSKUMau = new ArrayList<SKU_Attribute_Value>();
			List<SKU_Attribute_Value> listSKUCo = listSKUMau;

			if (product.getProducttypeid_link() > 9 && product.getProducttypeid_link() < 20) {
				listSKUMau = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_COLOR);
				listSKUCo = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_SIZE);
			} else if (product.getProducttypeid_link() > 19) {
				listSKUMau = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_COLOR);
				listSKUCo = savService.getlist_byProduct_and_attribute(entity.productid_link, AtributeFixValues.ATTR_SIZEWIDTH);
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
					savService.delete(savMau);
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
					savService.delete(savCo);
				}

			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
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

	@RequestMapping(value = "/getvalue", method = RequestMethod.POST)
	public ResponseEntity<ProductAttributeValue_getbyid_Response> AttributeValueget(
			@RequestBody ProductAttribute_createvalue_request entity, HttpServletRequest request) {
		ProductAttributeValue_getbyid_Response response = new ProductAttributeValue_getbyid_Response();
		List<ProductAttributeValue> list_old = pavService.getList_byAttId(entity.attributeid_link,
				entity.productid_link);
		response.data = new ArrayList<Attributevalue>();
		try {
			for (ProductAttributeValue productAttributeValue : list_old) {
				Attributevalue av = new Attributevalue();
				av.setAttributeid_link(entity.attributeid_link);
				av.setId(productAttributeValue.getAttributevalueid_link());
				av.setDatatype(0);
				av.setValue(productAttributeValue.getAttributeValueName());
				response.data.add(av);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ProductAttributeValue_getbyid_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ProductAttributeValue_getbyid_Response>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/createatt", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeCreate(@RequestBody ProductAttribute_createatt_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {

			for (Long id : entity.listId) {
				ProductAttributeValue data = new ProductAttributeValue();
				data.setId((long) 0);
				data.setAttributeid_link(id);
				data.setAttributevalueid_link((long) 0);
				data.setProductid_link(entity.productid_link);

				pavService.save(data);
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}

	@RequestMapping(value = "/deleteatt", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> AttributeDelete(@RequestBody ProductAttribute_delete_byAtt_request entity,
			HttpServletRequest request) {
		ResponseBase response = new ResponseBase();
		try {

			List<ProductAttributeValue> lst = pavService.getList_byAttId(entity.attributeid_link,
					entity.productid_link);
			Product product = productService.findOne(entity.productid_link);
			int type = product.getProducttypeid_link();

			for (ProductAttributeValue productAttributeValue : lst) {
				pavService.delete(productAttributeValue);
			}

			if (type == 10) {
				if (entity.attributeid_link == AtributeFixValues.ATTR_COLOR || AtributeFixValues.ATTR_SIZE == 30) {
					List<SKU> listSKU = skuService.getlist_byProduct(entity.productid_link);
					for (SKU sku : listSKU) {
						skuService.delete(sku);
					}
				}
			}
			else if (type >= 20 && type <= 40) {
				if (entity.attributeid_link == 35 || entity.attributeid_link == 36) {
					List<SKU> listSKU = skuService.getlist_byProduct(entity.productid_link);
					for (SKU sku : listSKU) {
						skuService.delete(sku);
					}
				}
			}

			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(HttpStatus.OK);
		}
	}
}
