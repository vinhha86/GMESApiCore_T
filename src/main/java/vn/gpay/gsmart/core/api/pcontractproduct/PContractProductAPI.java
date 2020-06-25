package vn.gpay.gsmart.core.api.pcontractproduct;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

import vn.gpay.gsmart.core.base.ResponseBase;
import vn.gpay.gsmart.core.pcontractattributevalue.IPContractProductAtrributeValueService;
import vn.gpay.gsmart.core.pcontractattributevalue.PContractAttributeValue;
import vn.gpay.gsmart.core.pcontractproduct.IPContractProductService;
import vn.gpay.gsmart.core.pcontractproduct.PContractProduct;
import vn.gpay.gsmart.core.pcontractproduct.PContractProductBinding;
import vn.gpay.gsmart.core.pcontractproductdocument.IPContractProducDocumentService;
import vn.gpay.gsmart.core.pcontractproductdocument.PContractProductDocument;
import vn.gpay.gsmart.core.pcontractproductpairing.IPContractProductPairingService;
import vn.gpay.gsmart.core.pcontractproductpairing.PContractProductPairing;
import vn.gpay.gsmart.core.pcontratproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontratproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.product.IProductService;
import vn.gpay.gsmart.core.product.Product;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_AttributeValue_Service;
import vn.gpay.gsmart.core.sku.SKU_Attribute_Value;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;


@RestController
@RequestMapping("/api/v1/pcontractproduct")
public class PContractProductAPI {
	@Autowired IPContractProductService pcpservice;
	@Autowired IPContractProductAtrributeValueService pcpavservice;
	@Autowired IProductService pservice;
	@Autowired IPContractProductSKUService pskuservice;
	@Autowired IPContractProducDocumentService docService;
	@Autowired ISKU_AttributeValue_Service skuavService;
	@Autowired IPContractProductPairingService pppairService;
	
	@RequestMapping(value = "/create", method = RequestMethod.POST)
 	public ResponseEntity<ResponseBase> Create(HttpServletRequest request,
			@RequestBody PContractProduct_create_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			for (long productid_link : entity.listIdProduct) {
				List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
				if(lst.size() == 0) {
					PContractProduct pcontractp = new PContractProduct();
					pcontractp.setId((long)0);
					pcontractp.setPcontractid_link(pcontractid_link);
					pcontractp.setProductid_link(productid_link);
					pcontractp.setOrgrootid_link(orgrootid_link);
					pcontractp.setPquantity(0);
					
					pcpservice.save(pcontractp);
					
					//insert cac thuoc tinh cua san pham sang
					Product p = pservice.findOne(productid_link);
					for (Long productattributeid_link : p.getProductAttribute()) {
						PContractAttributeValue cav = new PContractAttributeValue();
						cav.setId((long)0);
						cav.setOrgrootid_link(orgrootid_link);
						cav.setAttributeid_link(productattributeid_link);
						cav.setProductid_link(productid_link);
						cav.setPcontractid_link(pcontractid_link);
						cav.setAttributevalueid_link((long)0);
						
						pcpavservice.save(cav);
					}
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
	
	@RequestMapping(value = "/create_with_sku", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> CreateWithSKU(HttpServletRequest request,
			@RequestBody PContractProduct_create_with_sku_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			long productid_link = entity.productid_link;
			
			//Them product
			List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
			if(lst.size() == 0) {
				PContractProduct pcontractp = new PContractProduct();
				pcontractp.setId((long)0);
				pcontractp.setPcontractid_link(pcontractid_link);
				pcontractp.setProductid_link(productid_link);
				pcontractp.setOrgrootid_link(orgrootid_link);
				pcontractp.setPquantity(0);
				
				pcpservice.save(pcontractp);
				
				//insert cac thuoc tinh cua san pham sang
				Product p = pservice.findOne(productid_link);
				for (Long productattributeid_link : p.getProductAttribute()) {
					PContractAttributeValue cav = new PContractAttributeValue();
					cav.setId((long)0);
					cav.setOrgrootid_link(orgrootid_link);
					cav.setAttributeid_link(productattributeid_link);
					cav.setProductid_link(productid_link);
					cav.setPcontractid_link(pcontractid_link);
					cav.setAttributevalueid_link((long)0);
					
					pcpavservice.save(cav);
				}
			}
			
			//Them SKU
			
			for(Long skuid_link : entity.listskuid) {
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
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/update", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Update(HttpServletRequest request,
			@RequestBody PContractProduct_update_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			PContractProduct pcontractproduct = entity.data;
			
			pcontractproduct.setOrgrootid_link(orgrootid_link);
			pcpservice.save(pcontractproduct);
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<ResponseBase>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
	public ResponseEntity<ResponseBase> Update(HttpServletRequest request,
			@RequestBody PContractProduct_delete_product_request entity) {
		ResponseBase response = new ResponseBase();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			PContractProduct pproduct = pcpservice.findOne(entity.id);
			long pcontractid_link = pproduct.getPcontractid_link();
			long productid_link = pproduct.getProductid_link();
			
			//Xoa san pham trong don hang
			pcpservice.deleteById(entity.id);
			
			//Xoa thuoc tinh cua san pham
			
			List<PContractAttributeValue> listAttvalue = pcpavservice.getattribute_by_product_and_pcontract(orgrootid_link, pcontractid_link, productid_link);
			for(PContractAttributeValue pav : listAttvalue) {
				pcpavservice.delete(pav);
			}
			
			//Xóa sku của sản phẩm
			List<PContractProductSKU> listsku = pskuservice.getlistsku_byproduct_and_pcontract(orgrootid_link, productid_link, pcontractid_link);
			for(PContractProductSKU sku : listsku) {
				pskuservice.delete(sku);
			}
			
			//Xoa tai lieu
			
			List<PContractProductDocument> listdoc = docService.getlist_byproduct(orgrootid_link, pcontractid_link, productid_link);
			for(PContractProductDocument doc : listdoc) {
				docService.delete(doc);
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
	
	@RequestMapping(value = "/getbypcontract", method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_getall_response> GetByPContract(HttpServletRequest request,
			@RequestBody PContractProduct_getbycontract_request entity) {
		PContractProduct_getall_response response = new PContractProduct_getall_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, 0, pcontractid_link);
			List<PContractProductBinding> data = new ArrayList<PContractProductBinding>();
			String FolderPath = "upload/product";
			
			for (PContractProduct pContractProduct : lst) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pContractProduct.getId());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pContractProduct.getPcontractid_link());
				binding.setProductid_link(pContractProduct.getProductid_link());
				binding.setProductCode(pContractProduct.getProductCode());
				binding.setProductName(pContractProduct.getProductName());
				binding.setPquantity(pContractProduct.getPquantity());
				binding.setProduction_date(pContractProduct.getProduction_date());
				binding.setDelivery_date(pContractProduct.getDelivery_date());
				binding.setUnitprice(pContractProduct.getUnitprice());
				binding.setProductVendorCode(pContractProduct.getProductVendorCode());
				binding.setProductBuyerCode(pContractProduct.getProductBuyerCode());
				
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				binding.setImgproduct(getimg(pContractProduct.getImgurl1(),uploadRootPath));
				
				data.add(binding);
			}
			
			response.data = data;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractProduct_getall_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContractProduct_getall_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/gettreeproduct", method = RequestMethod.POST)
	public ResponseEntity<PContractProduct_gettreeproduct_response> GetTreeProductByPContract(HttpServletRequest request,
			@RequestBody PContractProduct_getbycontract_request entity) {
		PContractProduct_gettreeproduct_response response = new PContractProduct_gettreeproduct_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, 0, pcontractid_link);
			List<PContractProductBinding> data = new ArrayList<PContractProductBinding>();
			String FolderPath = "upload/product";
			
			for (PContractProduct pContractProduct : lst) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pContractProduct.getId());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pContractProduct.getPcontractid_link());
				binding.setProductid_link(pContractProduct.getProductid_link());
				binding.setProductCode(pContractProduct.getProductCode());
				binding.setProductName(pContractProduct.getProductName());
				binding.setPquantity(pContractProduct.getPquantity());
				binding.setProduction_date(pContractProduct.getProduction_date());
				binding.setDelivery_date(pContractProduct.getDelivery_date());
				binding.setUnitprice(pContractProduct.getUnitprice());
				binding.setProducttypeid_link(pContractProduct.getProducttypeid_link());
				
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				binding.setImgproduct(getimg(pContractProduct.getImgurl1(),uploadRootPath));
				
				data.add(binding);
			}
			
			//Lay nhung bo san pham
			List<PContractProductPairing> listpair = pppairService.getall_bypcontract(orgrootid_link, pcontractid_link);
			for(PContractProductPairing pair : listpair) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pair.getProductpairid_link());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pcontractid_link);
				binding.setProductid_link(pair.getProductpairid_link());
				binding.setProductCode(pair.getproductpairCode());
				binding.setProductName(pair.getproductpairName());
				binding.setProducttypeid_link(5);
				
				data.add(binding);
			}
			
			response.children = pservice.createTree(data, pcontractid_link);
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PContractProduct_gettreeproduct_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PContractProduct_gettreeproduct_response>(response, HttpStatus.OK);
		}
	}
	
	@RequestMapping(value = "/getpair_and_single", method = RequestMethod.POST)
	public ResponseEntity<PcontractProduct_getpair_andsingle_response> getPair_and_single(HttpServletRequest request,
			@RequestBody PContractProduct_getpair_andsingle_request entity) {
		PcontractProduct_getpair_andsingle_response response = new PcontractProduct_getpair_andsingle_response();
		try {
			GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication()
					.getPrincipal();
			long orgrootid_link = user.getRootorgid_link();
			long pcontractid_link = entity.pcontractid_link;
			List<PContractProduct> lst = pcpservice.get_by_product_and_pcontract(orgrootid_link, 0, pcontractid_link);
			List<PContractProductBinding> data = new ArrayList<PContractProductBinding>();
			String FolderPath = "upload/product";
			
			//Lay nhung bo san pham
			List<PContractProductPairing> listpair = pppairService.getall_bypcontract(orgrootid_link, pcontractid_link);
			for(PContractProductPairing pair : listpair) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pair.getProductpairid_link());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pcontractid_link);
				binding.setProductid_link(pair.getProductpairid_link());
				binding.setProductCode(pair.getproductpairCode());
				binding.setProductName(pair.getproductpairName());
				binding.setProducttypeid_link(5);
				
				data.add(binding);
				
				List<Product> listproduct = pservice.getby_pairid(pair.getProductpairid_link());
				for(Product product : listproduct) {
					lst.removeIf(c->c.getProductid_link() == product.getId());
				}
			}
			
			
			
			for (PContractProduct pContractProduct : lst) {
				PContractProductBinding binding = new PContractProductBinding();
				binding.setId(pContractProduct.getId());
				binding.setOrgrootid_link(orgrootid_link);
				binding.setPcontractid_link(pContractProduct.getPcontractid_link());
				binding.setProductid_link(pContractProduct.getProductid_link());
				binding.setProductCode(pContractProduct.getProductCode());
				binding.setProductName(pContractProduct.getProductName());
				binding.setPquantity(pContractProduct.getPquantity());
				binding.setProduction_date(pContractProduct.getProduction_date());
				binding.setDelivery_date(pContractProduct.getDelivery_date());
				binding.setUnitprice(pContractProduct.getUnitprice());
				binding.setProducttypeid_link(pContractProduct.getProducttypeid_link());
				
				String uploadRootPath = request.getServletContext().getRealPath(FolderPath);
				
				binding.setImgproduct(getimg(pContractProduct.getImgurl1(),uploadRootPath));
				
				data.add(binding);
			}
			
			
			
			response.data = data;
			
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<PcontractProduct_getpair_andsingle_response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<PcontractProduct_getpair_andsingle_response>(response, HttpStatus.OK);
		}
	}
	
	
	
	private byte[] getimg(String filename, String uploadRootPath) {
		String filePath = uploadRootPath+"\\"+ filename;
		Path path = Paths.get(filePath);
		byte[] data;
		try {
			data = Files.readAllBytes(path);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			data = null;
		}
		return data;
	}
}
