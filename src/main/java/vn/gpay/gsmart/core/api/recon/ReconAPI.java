package vn.gpay.gsmart.core.api.recon;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.gpay.gsmart.core.api.balance.Jitin_StockOutD_Data;
import vn.gpay.gsmart.core.api.balance.Jitin_StockinList_Response;
import vn.gpay.gsmart.core.api.balance.Jitin_Stockin_D_Data;
import vn.gpay.gsmart.core.api.balance.Jitin_StockoutList_Response;

import vn.gpay.gsmart.core.api.pcontractproductbom.PContractProductBom2API;
import vn.gpay.gsmart.core.org.IOrgService;
import vn.gpay.gsmart.core.pcontract.IPContractService;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.IPContract_bom2_npl_poline_sku_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.PContract_bom2_npl_poline_sku;
import vn.gpay.gsmart.core.pcontract_po.IPContract_POService;
import vn.gpay.gsmart.core.pcontractbomsku.IPContractBOM2SKUService;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService;
import vn.gpay.gsmart.core.pcontractproductsku.IPContractProductSKUService_NoLink;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU_NoLink;
import vn.gpay.gsmart.core.porder.IPOrder_Service;
import vn.gpay.gsmart.core.porder_product_sku.IPOrder_Product_SKU_Service;
import vn.gpay.gsmart.core.security.GpayUser;
import vn.gpay.gsmart.core.sku.ISKU_Service;
import vn.gpay.gsmart.core.utils.AtributeFixValues;
import vn.gpay.gsmart.core.utils.ResponseMessage;

@RestController
@RequestMapping("/api/v1/recon")
public class ReconAPI {
	@Autowired IPContractService pcontractService;
	@Autowired IPContract_POService pcontract_POService;
	@Autowired IPContractProductSKUService po_SKU_Service;
	@Autowired IPContractProductSKUService_NoLink po_SKU_Service_NoLink;
	@Autowired ISKU_Service skuService;
	@Autowired PContractProductBom2API bom2Service;
	@Autowired IPOrder_Product_SKU_Service pOrder_SKU_Service;
	@Autowired IPOrder_Service porder_Service;
	@Autowired IOrgService orgService;

	@Autowired IPContract_bom2_npl_poline_Service bomPOLine_Service;
	@Autowired IPContract_bom2_npl_poline_sku_Service bomPOLine_SKU_Service;
	@Autowired IPContractBOM2SKUService ppbom2skuservice;

	List<PContractBOM2SKU> bom_contract = new ArrayList<PContractBOM2SKU>();
	// Lay danh sach cac Ma NPL chi ap dung dac biet cho 1 so PO Line nhat dinh
	private static List<PContract_bom2_npl_poline> ls_bom_poline = new ArrayList<PContract_bom2_npl_poline>();
	
	// Lay danh sach cac Ma NPL chi ap dung dac biet cho 1 so PO Line va product_sku nhat dinh
	private static List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku = new ArrayList<PContract_bom2_npl_poline_sku>();
	
	private static HashMap<Long, Recon_MatSKU_Data> hash_MatSKUBalance = new HashMap<Long, Recon_MatSKU_Data>();

	@RequestMapping(value = "/cal_recon_bycontract", method = RequestMethod.POST)
	public ResponseEntity<Recon_Response> cal_recon_bycontract_new(HttpServletRequest request,
			@RequestBody Recon_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Recon_Response response = new Recon_Response();
		try {
			// Lay danh sách sku của pcontract (có SKU nghĩa là đã chốt và có PO chi tiết)
			//Luu y: 1 Contract co the co nhieu PO cung san xuat cho 1 SKU --> Neu trung SKU can phan don vao
//			List<PContractProductSKU> ls_Product_SKU_Contract = po_SKU_Service.getsumsku_bypcontract(entity.pcontractid_link);
//			List<PContractProductSKU> ls_Product_SKU = new ArrayList<PContractProductSKU>();
			System.out.println("Start:" + entity.list_productid);
			List<Long> ls_productid = new ArrayList<Long>();
			// Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0) {
				String[] s_productid = entity.list_productid.split(";");
				for (String sID : s_productid) {
					Long lID = Long.valueOf(sID);
					ls_productid.add(lID);
				}
			}
			
			List<PContractProductSKU_NoLink> ls_Product_SKU = po_SKU_Service_NoLink.getsumsku_bypcontract(entity.pcontractid_link, ls_productid);
			
//			//Loai bo cac product_sku khong co trong danh sach
//			if (ls_productid.size() > 0) {
//				for (PContractProductSKU thePContractSKU : ls_Product_SKU_Contract) {
//					if (ls_productid.contains(thePContractSKU.getProductid_link())) {
//						ls_Product_SKU.add(thePContractSKU);
//					}
//				}
//			} else {
//				ls_Product_SKU = ls_Product_SKU_Contract;
//			}
			if (ls_Product_SKU.size() > 0) {
				//2. Tinh so luong sku da xuat kho thanh pham tra cho khach
				List<Jitin_Stockin_D_Data> ls_PStockin = get_Pstockin_bycontract(request.getHeader("Authorization"),entity.pcontractid_link, null);
				List<Jitin_StockOutD_Data> ls_PStockout = get_Pstockout_bycontract(request.getHeader("Authorization"),entity.pcontractid_link, null);
				
				System.out.println("SL SKU San pham:" + ls_Product_SKU.size());
				CountDownLatch p_latch = new CountDownLatch(ls_Product_SKU.size());
				
				for (PContractProductSKU_NoLink product_sku : ls_Product_SKU) {
					Recon_ProductSKU theProduct_Stockout = new Recon_ProductSKU(ls_PStockin, ls_PStockout, product_sku, p_latch);
					theProduct_Stockout.start();
				}
				p_latch.await();
				
				// Lay danh sach chi tiet mau co cua tung sp theo tung po line (vi moi poline co the co bom dac biet rieng cho cac muc dich xuat hang rieng)
//				List<PContractProductSKU_NoLink> ls_Product_SKU_Poline = po_SKU_Service_NoLink.getlistsku_bypcontract_nolink(user.getRootorgid_link(), entity.pcontractid_link);		
				ls_bom_poline = bomPOLine_Service.getby_pcontract(entity.pcontractid_link);
				ls_bom_poline_sku = bomPOLine_SKU_Service.getby_pcontract(entity.pcontractid_link);
	
				//Dinh muc can doi.
				bom_contract = ppbom2skuservice.getall_bypcontract(user.getRootorgid_link(), entity.pcontractid_link);
				
				// Tinh nhu cau NPL theo dinh muc va so luong thanh pham da xuat
				hash_MatSKUBalance.clear();
//				System.out.println("Start BOM:" + new Date());
//				System.out.println("PO Line:" + ls_Product_SKU_Poline.size());
	
				CountDownLatch bomdemand_latch = new CountDownLatch(ls_Product_SKU.size());
				for (PContractProductSKU_NoLink product_sku : ls_Product_SKU) {
					Recon_BOMDemandSKU theBOM_Demand = new Recon_BOMDemandSKU(
							bom_contract, 
							hash_MatSKUBalance, 
							product_sku, 
							ls_bom_poline,
							ls_bom_poline_sku, 
							bomdemand_latch, 
							entity.recon_type
							);
					theBOM_Demand.start();
//					cal_recon_bomdemand(product_sku, entity.materialid_link);
				}
				bomdemand_latch.await();
				System.out.println("End BOM:" + new Date());
				System.out.println(hash_MatSKUBalance.size());
				
				// 3. Tinh toan nguyen phu lieu thuc nhap, thuc xuat theo phieu xuat kho cua cac NPL trong danh sach
				// Duyệt qua từng màu, cỡ của sản phẩm (SKU) để tính nhu cầu NPL cho màu, cỡ đó
				List<Recon_MatSKU_Data> ls_MatSKUBalance = new ArrayList<Recon_MatSKU_Data>(hash_MatSKUBalance.values());
				System.out.println("SL SKU NPL:" + ls_MatSKUBalance.size());
				
				CountDownLatch latch = new CountDownLatch(ls_MatSKUBalance.size());
				
//				System.out.println("Start get Stockin:" + new Date());
				List<Jitin_Stockin_D_Data> ls_MStockin = get_Mstockin_bycontract(request.getHeader("Authorization"),entity.pcontractid_link, null);
//				System.out.println("End get Stockin:" + ls_MStockin.size() + "-" + new Date());
				List<Jitin_StockOutD_Data> ls_MStockout = get_Mstockout_bycontract(request.getHeader("Authorization"),entity.pcontractid_link, null);
//				System.out.println("End get Stockout:" + ls_MStockout.size() + "-" + new Date());
				
				
				List<Recon_MatSKU_Data> ls_MatSKUBalance_NULL = new ArrayList<Recon_MatSKU_Data>(); 
				for (Recon_MatSKU_Data mat_sku : ls_MatSKUBalance) {
					if (null == mat_sku) ls_MatSKUBalance_NULL.add(mat_sku);
					Recon_MatSKU theReconMat = new Recon_MatSKU(ls_MStockin,ls_MStockout, mat_sku, latch);
					theReconMat.start();
				}
				latch.await();
//				System.out.println("End NPL:" + new Date());
				System.out.println("SL SKU NULL:" + ls_MatSKUBalance_NULL.size());
				ls_MatSKUBalance.removeAll(ls_MatSKUBalance_NULL);
				System.out.println("SL SKU OK:" + ls_MatSKUBalance.size());
				response.data = ls_MatSKUBalance;
			}

			response.productsku_data = ls_Product_SKU;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Recon_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			e.printStackTrace();
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Recon_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}
	
	private List<Jitin_Stockin_D_Data> get_Pstockin_bycontract(
			String token,
			Long pcontractid_link,
			Long stockid_link
			){
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockin/p_stockind_bypcontract";

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode appParNode = objectMapper.createObjectNode();
			appParNode.put("pcontractid_link", pcontractid_link);
			appParNode.put("stockid_link", stockid_link);
			String jsonReq = objectMapper.writeValueAsString(appParNode);

			HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
			String result = restTemplate.postForObject(urlPost, request, String.class);
			Jitin_StockinList_Response ls_stockind = objectMapper.readValue(result, Jitin_StockinList_Response.class);
			
			return ls_stockind.data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<Jitin_Stockin_D_Data> get_Mstockin_bycontract(
			String token,
			Long pcontractid_link,
			Long stockid_link
			){
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockin/stockind_bypcontract";

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode appParNode = objectMapper.createObjectNode();
			appParNode.put("pcontractid_link", pcontractid_link);
			appParNode.put("stockid_link", stockid_link);
			String jsonReq = objectMapper.writeValueAsString(appParNode);

			HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
			String result = restTemplate.postForObject(urlPost, request, String.class);
			Jitin_StockinList_Response ls_stockind = objectMapper.readValue(result, Jitin_StockinList_Response.class);
			
			return ls_stockind.data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private List<Jitin_StockOutD_Data> get_Pstockout_bycontract(
			String token,
			Long pcontractid_link,
			Long stockid_link
			){
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockout/to_vendor_p_stockoutd_bypcontract";

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode appParNode = objectMapper.createObjectNode();
			appParNode.put("pcontractid_link", pcontractid_link);
			appParNode.put("stockid_link", stockid_link);
			String jsonReq = objectMapper.writeValueAsString(appParNode);

			HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
			String result = restTemplate.postForObject(urlPost, request, String.class);
			Jitin_StockoutList_Response ls_stockoutd = objectMapper.readValue(result,
					Jitin_StockoutList_Response.class);
			
			return ls_stockoutd.data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	private List<Jitin_StockOutD_Data> get_Mstockout_bycontract(
			String token,
			Long pcontractid_link,
			Long stockid_link
			){
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockout/stockoutd_bypcontract";

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode appParNode = objectMapper.createObjectNode();
			appParNode.put("pcontractid_link", pcontractid_link);
			appParNode.put("stockid_link", stockid_link);
			String jsonReq = objectMapper.writeValueAsString(appParNode);

			HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
			String result = restTemplate.postForObject(urlPost, request, String.class);
			Jitin_StockoutList_Response ls_stockoutd = objectMapper.readValue(result,
					Jitin_StockoutList_Response.class);
			
			return ls_stockoutd.data;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	private void cal_recon_bomdemand (
			PContractProductSKU_NoLink product_sku,
			Long materialid_link
			){
		try {
			List<PContractBOM2SKU> bom_response = bom_contract.stream().filter(sku -> sku.getProduct_skuid_link().equals(product_sku.getSkuid_link())).collect(Collectors.toList());
			
			ExecutorService executor = Executors.newFixedThreadPool(bom_response.size() + 1);
			
			for (PContractBOM2SKU mat_skubom : bom_response) {
				if (null != materialid_link && !materialid_link.equals(mat_skubom.getMaterial_skuid_link())) {
					continue;
				} else {
					Runnable demand = new calDemand_Candoi(mat_skubom, product_sku);
					executor.execute(demand);
				}
			}
			executor.shutdown();
			// Wait until all threads are finish
			while (!executor.isTerminated()) {

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static class calDemand_Candoi implements Runnable {
		private final PContractBOM2SKU mat_skubom;
		private final PContractProductSKU_NoLink product_sku;
		calDemand_Candoi(
				PContractBOM2SKU mat_skubom, 
				PContractProductSKU_NoLink product_sku
		) {
			this.mat_skubom = mat_skubom;
			this.product_sku = product_sku;
		}

		@Override
		public void run() {
			try {
				Integer p_amount =  0;
				p_amount = null != product_sku.getPquantity_porder()?product_sku.getPquantity_porder():0;
//				//Tuy theo loai quyet toan de lay so luong dua vao tinh toan
//				if (Recon_Type == 0) {//Quyet toan can doi
//					p_amount = product_sku.getPquantity_porder();
//				} else if (Recon_Type == 1) {//Quyet toan Hai quan
//					p_amount = product_sku.getPquantity_porder();
//				} else if (Recon_Type == 2) {//Quyet toan san xuat (noi bo)
//					p_amount = product_sku.getPquantity_total();
//				}
			
				//Kiem tra xem NPL co nam trong danh sach gioi han po-line khong?
				List<PContract_bom2_npl_poline> ls_bom_poline_check = ls_bom_poline.stream().filter(sku -> sku.getNpl_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
				
				if (ls_bom_poline_check.size() > 0) {
					if (!ls_bom_poline_check.stream().anyMatch(po -> po.getPcontract_poid_link().equals(product_sku.getPcontract_poid_link()))) {
						return;
					} else {
						List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku_check = ls_bom_poline_sku.stream().filter(sku -> sku.getMaterial_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
						//Kiem tra xem NPL co trong danh sach gioi han mau co ko
						if (ls_bom_poline_sku_check.size() > 0) {
							if (!ls_bom_poline_sku_check.stream().anyMatch(po -> po.getProduct_skuid_link().equals(product_sku.getSkuid_link()))) {
								return;
							}
						}
					}
				}
				
//				List<Recon_MatSKU_Data> lsSKUBalance = ls_MatSKUBalance.stream().parallel()
//						.filter(sku -> sku.getMat_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
				
				Recon_MatSKU_Data theSKUBalance = hash_MatSKUBalance.get(mat_skubom.getMaterial_skuid_link());
				
				if (null != theSKUBalance) {
					// Tinh tong dinh muc
					Float f_skudemand = (float) 0;
					if (null!=mat_skubom.getAmount()) {
						if (null != mat_skubom.getLost_ratio())
							f_skudemand = mat_skubom.getAmount() * p_amount * mat_skubom.getLost_ratio();
						else
							f_skudemand = mat_skubom.getAmount() * p_amount;
					}
					// Tinh trung binh dinh muc
					Float f_skubomamount = ((null != theSKUBalance.getMat_sku_bom_amount()?theSKUBalance.getMat_sku_bom_amount():0) + (null != mat_skubom.getAmount()?mat_skubom.getAmount():0)) / 2;
					theSKUBalance.setMat_sku_bom_amount(f_skubomamount);

					theSKUBalance.setMat_sku_demand(theSKUBalance.getMat_sku_demand() + f_skudemand);
					
////					// Thong tin chi tiet mau co
////					SKUBalance_Product_D_Data product_d = new SKUBalance_Product_D_Data();
////					product_d.setP_skuid_link(product_skuid_link);
////					product_d.setP_sku_code(product_sku.getSkuCode());
//////					product_d.setP_sku_color(product_sku.getMauSanPham());
//////					product_d.setP_sku_size(product_sku.getCoSanPham());
////					product_d.setP_amount(p_amount);
////					product_d.setP_bom_amount(skubom.getAmount());
////					product_d.setP_bom_lostratio(skubom.getLost_ratio());
////					product_d.setP_bom_demand(f_skudemand);
////					theSKUBalance.getProduct_d().add(product_d);
	//
				} else {
//					System.out.println(mat_skubom.getMaterialCode());
					Recon_MatSKU_Data newSKUBalance = new Recon_MatSKU_Data();
					newSKUBalance.setMat_skuid_link(mat_skubom.getMaterial_skuid_link());
					newSKUBalance.setMat_sku_product_total(p_amount);

					newSKUBalance.setMat_sku_code(mat_skubom.getMaterialCode());
					newSKUBalance.setMat_sku_name(mat_skubom.getMaterialCode());
					newSKUBalance.setMat_sku_desc(mat_skubom.getDescription_product());
					newSKUBalance.setMat_sku_unit_name(mat_skubom.getUnitName());
//					newSKUBalance.setMat_sku_size_name(skubom.getCoKho());
//					newSKUBalance.setMat_sku_color_name(skubom.getTenMauNPL());
					newSKUBalance.setMat_sku_product_typename(mat_skubom.getProduct_typeName());
					newSKUBalance.setMat_sku_product_typeid_link(mat_skubom.getProduct_type());

					newSKUBalance.setMat_sku_bom_lostratio(mat_skubom.getLost_ratio());
					newSKUBalance.setMat_sku_bom_amount(mat_skubom.getAmount());

					Float f_skudemand = (float) 0;
					if (null!=mat_skubom.getAmount()) {
						if (null != mat_skubom.getLost_ratio())
							f_skudemand = mat_skubom.getAmount() * p_amount * mat_skubom.getLost_ratio();
						else
							f_skudemand = mat_skubom.getAmount() * p_amount;
					}
					newSKUBalance.setMat_sku_demand(f_skudemand);

					hash_MatSKUBalance.put(mat_skubom.getMaterial_skuid_link(), newSKUBalance);
				}
				
//				System.out.println(mat_skubom.getMaterial_skuid_link() + ":" + hash_MatSKUBalance.size());
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
