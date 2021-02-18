package vn.gpay.gsmart.core.api.balance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.api.pcontractproductbom.PContractProductBOM_getbomcolor_request;
import vn.gpay.gsmart.core.api.pcontractproductbom.PContractProductBom2API;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontract_po.PContract_PO;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.sku.SKU;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/balance")
public class BalanceAPI {
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPContractProductSKUService po_SKU_Service;
	@Autowired ISKU_Service skuService;
	@Autowired PContractProductBom2API bom2Service;
	
	@RequestMapping(value = "/cal_balance_bypo", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> cal_balance_bypo(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Balance_Response response = new Balance_Response();
		try {
			//Check if PO exist
			PContract_PO thePO = pcontract_POService.findOne(entity.pcontract_poid_link);
			if (null!=thePO){
				//1. Lay danh sach Product, Color va SL yeu cau SX theo PO 
				//(Neu la PO cha thi lay tong yeu cau cua cac PO con)
				List<Balance_Product_Data> ls_Product = get_BalanceProduct_List(entity.pcontract_poid_link);
				if (null!=ls_Product){
					//2. Lay tong hop BOM theo PContractid_link, Productid_link, Colorid_link
					List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
					for(Balance_Product_Data theProduct:ls_Product){
						cal_demand(ls_SKUBalance, thePO.getPcontractid_link(), theProduct.productid_link,theProduct.colorid_link, theProduct.amount);
					}
		
					//3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
					CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());
					for(SKUBalance_Data mat_sku:ls_SKUBalance){
						Balance_SKU theBalance =  new Balance_SKU(
								ls_SKUBalance,
								thePO.getPcontractid_link(),
								thePO.getId(),
								mat_sku,
								request.getHeader("Authorization"),
								latch);
						theBalance.start();
					}
					latch.await();
		            response.data = ls_SKUBalance;
		            response.product_data = ls_Product;
					response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
					response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
					return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
				} else {
					response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
					response.setMessage("Chưa khai báo chi tiết màu cỡ");
					return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
				}
			} else {
				response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
				response.setMessage("PO Không tồn tại");
				return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	@RequestMapping(value = "/get_material_bypcontract", method = RequestMethod.POST)
	public ResponseEntity<Balance_Response> get_material_bypcontract(HttpServletRequest request,
			@RequestBody Balance_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		long orgrootid_link = user.getRootorgid_link();
		
		Balance_Response response = new Balance_Response();
		try {
			//Lay danh sach PO cua PContract
			List<PContract_PO> ls_PO = pcontract_POService.getPOByContract(orgrootid_link, entity.pcontractid_link);
			
			List<SKUBalance_Data> ls_SKUBalance = new ArrayList<SKUBalance_Data>();
			List<Balance_Product_Data> ls_Product = new ArrayList<Balance_Product_Data>();
			for(PContract_PO thePO: ls_PO){
				//1. Lay danh sach Product, Color va SL yeu cau SX theo PO 
				//(Neu la PO cha thi lay tong yeu cau cua cac PO con)
				List<Balance_Product_Data> ls_Product_PO = get_BalanceProduct_List(thePO.getId());
				if (null!=ls_Product){
					//2. Lay tong hop BOM theo PContractid_link, Productid_link, Colorid_link
					for(Balance_Product_Data theProduct:ls_Product_PO){
						cal_demand(ls_SKUBalance, thePO.getPcontractid_link(), theProduct.productid_link,theProduct.colorid_link, theProduct.amount);
					}
					ls_Product.addAll(ls_Product_PO);
				}
			}
            response.data = ls_SKUBalance;
            response.product_data = ls_Product;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Balance_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Balance_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}	
	//Lay danh sach SKU cua Product trong PO
	private List<Balance_Product_Data> get_BalanceProduct_List(Long pcontract_poid_link){
		try {
			PContract_PO thePO =  pcontract_POService.findOne(pcontract_poid_link);
			if (null!=thePO){
				List<PContractProductSKU> ls_SKU_Balance = new ArrayList<PContractProductSKU>();
				//Kiem tra xem co phai la PO chao gia khong
				if (null==thePO.getParentpoid_link()){
					//Neu la PO chao gia --> Load sum theo SKU của các PO line con
					ls_SKU_Balance = po_SKU_Service.getsumsku_bypo_parent(pcontract_poid_link);
					
				} else{
					//Neu la PO Line --> Load danh sach chi tiet SKU
					ls_SKU_Balance = po_SKU_Service.getlistsku_bypo(pcontract_poid_link);
				}

				//Tổng hợp theo Productid, Color_id
				List<Balance_Product_Data> ls_Product_Balance = new ArrayList<Balance_Product_Data>();
				for(PContractProductSKU poSKU: ls_SKU_Balance){
					SKU theSKU = skuService.findOne(poSKU.getSkuid_link());
					if (null != theSKU){
						Balance_Product_Data theBalance = ls_Product_Balance.stream().filter(prod -> prod.productid_link.equals(poSKU.getProductid_link()) && prod.colorid_link.equals(theSKU.getColorid_link())).findAny().orElse(null);
						if (null!=theBalance){
							theBalance.amount += poSKU.getPquantity_total();
						} else {
							Balance_Product_Data newBalance = new Balance_Product_Data();
							newBalance.productid_link = poSKU.getProductid_link();
							newBalance.product_code = theSKU.getCode();
							newBalance.product_name = theSKU.getName();
							newBalance.colorid_link = theSKU.getColorid_link();
							newBalance.color_name = theSKU.getColor_name();
							newBalance.amount = poSKU.getPquantity_total();
							ls_Product_Balance.add(newBalance);
						}
					}
				}	
				return ls_Product_Balance;
			} else {
				return null;
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
	}
	//Tinh nhu cau NPL theo định mức
	private void cal_demand(List<SKUBalance_Data> ls_SKUBalance, Long pcontractid_link, Long productid_link, Long colorid_link, Integer p_amount){
		PContractProductBOM_getbomcolor_request entity =  new PContractProductBOM_getbomcolor_request();
		entity.pcontractid_link = pcontractid_link;
		entity.colorid_link = colorid_link;
		entity.productid_link = productid_link;
		
		List<Map<String, String>> ls_bomdata = bom2Service.GetListProductBomColor(entity);
		if (null!=ls_bomdata){
			for(Map<String, String> bomdata:ls_bomdata){
//				System.out.println(bomdata);
				Long materialid_link = Long.valueOf(bomdata.get("materialid_link"));
				Float amount_color = (float) 0;
				if (null!=bomdata.get("amount") && Float.valueOf(bomdata.get("amount")) > (float) 0){
					amount_color = Float.valueOf(bomdata.get("amount"));
				} else {
					amount_color = null!=bomdata.get("amount_color")?Float.valueOf(bomdata.get("amount_color")):0;
				}
				
				Float lostratio = null!=bomdata.get("lost_ratio")?Float.valueOf(bomdata.get("lost_ratio")):0;
				SKUBalance_Data theSKUBalance = ls_SKUBalance.stream().filter(sku -> sku.getMat_skuid_link().equals(materialid_link)).findAny().orElse(null);
				if (null!=theSKUBalance){
					//Tinh tong dinh muc
//					theSKUBalance.setMat_sku_bom_amount(theSKUBalance.getMat_sku_bom_amount() + amount_color);
					theSKUBalance.setMat_sku_demand(theSKUBalance.getMat_sku_demand() + amount_color*p_amount);
				} else {
					SKUBalance_Data newSKUBalance = new SKUBalance_Data();
					newSKUBalance.setMat_skuid_link(materialid_link);
					
					newSKUBalance.setMat_sku_code(bomdata.get("materialCode"));
					newSKUBalance.setMat_sku_name(bomdata.get("materialName"));
					newSKUBalance.setMat_sku_unit_name(bomdata.get("unitName"));
					newSKUBalance.setMat_sku_size_name(bomdata.get("coKho"));
					newSKUBalance.setMat_sku_color_name(bomdata.get("tenMauNPL"));
					newSKUBalance.setMat_sku_product_typename(bomdata.get("product_typename"));
					
					newSKUBalance.setMat_sku_bom_lostratio(lostratio);
					newSKUBalance.setMat_sku_bom_amount(amount_color);
					newSKUBalance.setMat_sku_demand(amount_color*p_amount);
					
					ls_SKUBalance.add(newSKUBalance);
				}
			}
		}
	}
}
