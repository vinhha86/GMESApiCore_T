package vn.gpay.gsmart.core.api.recon;

import java.util.ArrayList;
import java.util.List;
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

import vn.gpay.gsmart.core.api.pcontractproductbom.PContractProductBom2API;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.IPContract_bom2_npl_poline_sku_Service;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/recon")
public class ReconAPI {
	@Autowired IPContractService pcontractService;
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPContractProductSKUService po_SKU_Service;
	@Autowired ISKU_Service skuService;
	@Autowired PContractProductBom2API bom2Service;
	@Autowired IPOrder_Product_SKU_Service pOrder_SKU_Service;
	@Autowired IPOrder_Service porder_Service;
	@Autowired IOrgService orgService;
	
	@Autowired IPContract_bom2_npl_poline_Service bomPOLine_Service;
	@Autowired IPContract_bom2_npl_poline_sku_Service bomPOLine_SKU_Service;
	
	@RequestMapping(value = "/cal_recon_bycontract", method = RequestMethod.POST)
	public ResponseEntity<Recon_Response> cal_recon_bycontract(HttpServletRequest request,
			@RequestBody Recon_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Recon_Response response = new Recon_Response();
		try {
			//Lay danh sách sku (chi tiết màu cỡ) của pcontract (có SKU nghĩa là đã chốt và có PO chi tiết)
//			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getsumsku_bypcontract(entity.pcontractid_link);
			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getlistsku_bypcontract(user.getRootorgid_link(),entity.pcontractid_link);
			
			List<Long> ls_productid = new ArrayList<Long>();
			//Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0){
				String[] s_productid = entity.list_productid.split(";"); 
				for(String sID:s_productid){
					Long lID = Long.valueOf(sID);
					ls_productid.add(lID);
				}
			}
			
			//Duyệt qua từng màu, cỡ của sản phẩm (SKU) để tính nhu cầu NPL cho màu, cỡ đó theo định mức cân đối
			List<SKURecon_Data> ls_SKURecon = new ArrayList<SKURecon_Data>();
			for (PContractProductSKU thePContractSKU: ls_Product_SKU){
//				SKU theProduct_SKU = skuService.findOne(thePContractSKU.getSkuid_link());
				//Chỉ tính các sku của SP trong danh sách chọn
				if (ls_productid.contains(thePContractSKU.getProductid_link())){
//					System.out.println(thePContractSKU.getProductcode() + "-" + thePContractSKU.getMauSanPham() + "-" + thePContractSKU.getCoSanPham()
//					+ "-" + thePContractSKU.getPcontract_poid_link() + "-" + thePContractSKU.getPquantity_total());
					cal_demand_bysku(ls_SKURecon, entity.pcontractid_link,thePContractSKU.getPcontract_poid_link(), 
							thePContractSKU.getProductid_link(), 
							thePContractSKU.getSkuid_link(),
							thePContractSKU.getSkuCode(),
							thePContractSKU.getMauSanPham(),
							thePContractSKU.getCoSanPham(),
							thePContractSKU.getPquantity_total());
				}
			}
			
			//3. Tinh toan quyết toán cho tung nguyen phu lieu trong BOM
			CountDownLatch latch = new CountDownLatch(ls_SKURecon.size());

			for(SKURecon_Data mat_sku:ls_SKURecon){
				Recon_SKU theRecon =  new Recon_SKU(
						ls_SKURecon,
						entity.pcontractid_link,
						null,
						null,
						null,
						mat_sku,
						request.getHeader("Authorization"),
						latch);
				theRecon.start();
			}
			latch.await();	
			
            response.data = ls_SKURecon;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Recon_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Recon_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}	

	@RequestMapping(value = "/get_material_bypcontract", method = RequestMethod.POST)
	public ResponseEntity<Recon_Response> get_material_bypcontract(HttpServletRequest request,
			@RequestBody Recon_Request entity) {
//		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//		long orgrootid_link = user.getRootorgid_link();
		
		Recon_Response response = new Recon_Response();
		try {
			//Lay danh sach PO cua PContract
//			List<PContract_PO> ls_PO = pcontract_POService.getPOByContract(orgrootid_link, entity.pcontractid_link);
			List<Long> ls_productid = new ArrayList<Long>();
			//Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0){
				String[] s_productid = entity.list_productid.split(";"); 
				for(String sID:s_productid){
					Long lID = Long.valueOf(sID);
					ls_productid.add(lID);
				}
			}
			
			List<SKURecon_Data> ls_SKURecon = new ArrayList<SKURecon_Data>();
            response.data = ls_SKURecon;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Recon_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Recon_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}	
	private void cal_demand_bysku(List<SKURecon_Data> ls_SKURecon, 
			Long pcontractid_link, 
			Long pcontract_poid_link, 
			Long productid_link, 
			Long product_skuid_link, 
			String product_sku_code, 
			String product_sku_color, 
			String product_sku_size, 
			Integer p_amount){
	
		List<PContractBOM2SKU> bom_response = bom2Service.getBOM_By_PContractSKU(pcontractid_link, product_skuid_link);
		for (PContractBOM2SKU skubom:bom_response){
//			if (skubom.getMaterialCode().contains("CXI55020")){
//				System.out.println(skubom.getMaterial_skuid_link() + "/" + skubom.getMaterialCode() + "-" + p_amount);
//			}
			//Kiểm tra xem NPL có trong danh sách giới hạn PO không (pcontract_bom2_npl_poline)?
			//Nếu có, kiểm tra tiếp xem có giới hạn áp dụng cụ thể cho từng product_sku ko? SL áp dụng là bao nhiêu
			List<PContract_bom2_npl_poline> ls_poline = bomPOLine_Service.getby_product_and_npl(productid_link, pcontractid_link, skubom.getMaterial_skuid_link());
			if (ls_poline.size() > 0){
				boolean isfound = false;
				//Check nếu poline gửi vào ko có trong danh sách --> Bỏ qua NPL này
				for(PContract_bom2_npl_poline thebom_poline: ls_poline){
					if (pcontract_poid_link.equals(thebom_poline.getPcontract_poid_link())){
						isfound = true;
						//Kiem tra tiep xem có giới hạn sâu hơn trong pcontract_bom2_npl_poline_sku ko?
						
					}
				}
				//Bo qua NPL
				if (!isfound) continue;
			}
			
			SKURecon_Data newSKURecon = ls_SKURecon.stream().filter(sku -> sku.getMat_skuid_link().equals(skubom.getMaterial_skuid_link())).findAny().orElse(null);
			if (null!=newSKURecon){
				//Tinh tong dinh muc
				Float f_skudemand =skubom.getAmount()*p_amount;
				Float f_lost = (f_skudemand*skubom.getLost_ratio())/100;
				
				//Tinh trung binh dinh muc
				Float f_skubomamount = (newSKURecon.getMat_sku_bom_amount() + skubom.getAmount())/2;
				newSKURecon.setMat_sku_bom_amount(f_skubomamount);
				
				newSKURecon.setMat_sku_demand(newSKURecon.getMat_sku_demand() + f_skudemand + f_lost);
				newSKURecon.setMat_sku_product_total(newSKURecon.getMat_sku_product_total() + p_amount);

				
			} else {
				newSKURecon = new SKURecon_Data();
				newSKURecon.setMat_skuid_link(skubom.getMaterial_skuid_link());
				newSKURecon.setMat_sku_product_total(p_amount);
				
				newSKURecon.setMat_sku_code(skubom.getMaterialCode());
				newSKURecon.setMat_sku_name(skubom.getMaterialCode());
				newSKURecon.setMat_sku_desc(skubom.getDescription_product());
				newSKURecon.setMat_sku_unit_name(skubom.getUnitName());
				newSKURecon.setMat_sku_size_name(skubom.getCoKho());
				newSKURecon.setMat_sku_color_name(skubom.getTenMauNPL());
				newSKURecon.setMat_sku_product_typename(skubom.getProduct_typeName());
				
				newSKURecon.setMat_sku_bom_lostratio(skubom.getLost_ratio());
				newSKURecon.setMat_sku_bom_amount(skubom.getAmount());
				
				Float f_skudemand =skubom.getAmount()*p_amount;
				Float f_lost = (f_skudemand*skubom.getLost_ratio())/100;
				newSKURecon.setMat_sku_demand(f_skudemand + f_lost);
				
			
				ls_SKURecon.add(newSKURecon);
			}			
		}
	}
}
