package vn.gpay.gsmart.core.api.recon;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import vn.gpay.gsmart.core.api.balance.SKUBalance_Product_D_Data;
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
	@Autowired
	IPContractService pcontractService;
	@Autowired
	IPContract_POService pcontract_POService;
	@Autowired
	IPContractProductSKUService po_SKU_Service;
	@Autowired
	ISKU_Service skuService;
	@Autowired
	PContractProductBom2API bom2Service;
	@Autowired
	IPOrder_Product_SKU_Service pOrder_SKU_Service;
	@Autowired
	IPOrder_Service porder_Service;
	@Autowired
	IOrgService orgService;

	@Autowired
	IPContract_bom2_npl_poline_Service bomPOLine_Service;
	@Autowired
	IPContract_bom2_npl_poline_sku_Service bomPOLine_SKU_Service;


	@RequestMapping(value = "/cal_recon_bycontract", method = RequestMethod.POST)
	public ResponseEntity<Recon_Response> cal_recon_bycontract_new(HttpServletRequest request,
			@RequestBody Recon_Request entity) {
		GpayUser user = (GpayUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Recon_Response response = new Recon_Response();
		try {
			// Lay danh sách sku của pcontract (có SKU nghĩa là đã chốt và có PO chi tiết)
//			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getsumsku_bypcontract(entity.pcontractid_link);
			List<PContractProductSKU> ls_Product_SKU = po_SKU_Service.getlistsku_bypcontract(user.getRootorgid_link(),
					entity.pcontractid_link);

			List<Long> ls_productid = new ArrayList<Long>();
			// Nếu có danh sách SP --> Chỉ tính các SP trong danh sách
			if (null != entity.list_productid && entity.list_productid.length() > 0) {
				String[] s_productid = entity.list_productid.split(";");
				for (String sID : s_productid) {
					Long lID = Long.valueOf(sID);
					ls_productid.add(lID);
				}
			}
			
			//Loai bo cac product_sku khong co trong danh sach
			for (PContractProductSKU thePContractSKU : ls_Product_SKU) {
				if (!ls_productid.contains(thePContractSKU.getProductid_link())) {
					ls_Product_SKU.remove(thePContractSKU);
				}
			}
			// Duyệt qua từng màu, cỡ của sản phẩm (SKU) để tính nhu cầu NPL cho màu, cỡ đó
			List<Recon_MatSKU_Data> ls_SKUBalance = new ArrayList<Recon_MatSKU_Data>();
			
			//2. Tinh so luong sku da xuat kho thanh pham tra cho khach
			CountDownLatch p_latch = new CountDownLatch(ls_Product_SKU.size());

			for (PContractProductSKU product_sku : ls_Product_SKU) {
				Recon_ProductSKU theProduct_Stockout = new Recon_ProductSKU(entity.pcontractid_link, null, null, null,
						product_sku, request.getHeader("Authorization"), p_latch);
				theProduct_Stockout.start();
			}
			p_latch.await();
			
		
			//Tinh nhu cau NPL va so da xuat cung SP theo dinh muc
			for (PContractProductSKU thePContractSKU : ls_Product_SKU) {
				cal_demand_bysku(ls_SKUBalance, entity.pcontractid_link, thePContractSKU.getPcontract_poid_link(),
						thePContractSKU.getProductid_link(), thePContractSKU.getSkuid_link(),
						thePContractSKU.getSkuCode(), thePContractSKU.getMauSanPham(),
						thePContractSKU.getCoSanPham(), thePContractSKU.getPquantity_total(),
						thePContractSKU.getPo_buyer(), thePContractSKU.getPquantity_porder(), 
						thePContractSKU.getPquantity_stockout(), entity.balance_limit);
			}

			// 3. Tinh toan can doi cho tung nguyen phu lieu trong BOM
			CountDownLatch latch = new CountDownLatch(ls_SKUBalance.size());

			for (Recon_MatSKU_Data mat_sku : ls_SKUBalance) {
				Recon_MatSKU theBalance = new Recon_MatSKU(entity.pcontractid_link, null, null, null,
						mat_sku, request.getHeader("Authorization"), latch);
				theBalance.start();
			}
			latch.await();

			response.data = ls_SKUBalance;
			response.setRespcode(ResponseMessage.KEY_RC_SUCCESS);
			response.setMessage(ResponseMessage.getMessage(ResponseMessage.KEY_RC_SUCCESS));
			return new ResponseEntity<Recon_Response>(response, HttpStatus.OK);
		} catch (Exception e) {
			response.setRespcode(ResponseMessage.KEY_RC_EXCEPTION);
			response.setMessage(e.getMessage());
			return new ResponseEntity<Recon_Response>(response, HttpStatus.BAD_REQUEST);
		}
	}

	private void cal_demand_bysku(List<Recon_MatSKU_Data> ls_SKUBalance, Long pcontractid_link, Long pcontract_poid_link,
			Long productid_link, Long product_skuid_link, String product_sku_code, String product_sku_color,
			String product_sku_size, Integer p_amount, String po_buyer, Integer p_amount_dh, Integer p_amount_stockout, Integer balance_limit) {
		try {
			List<PContractBOM2SKU> bom_response = bom2Service.getBOM_By_PContractSKU(pcontractid_link,
					product_skuid_link);

			ExecutorService executor = Executors.newFixedThreadPool(bom_response.size() + 1);
			for (PContractBOM2SKU skubom : bom_response) {
				if (balance_limit == 1) {// Chi tinh nguyen lieu
					if (skubom.getProduct_type() >= 30 || skubom.getProduct_type() < 20)
						continue;
				} else if (balance_limit == 2) {// Chi tinh phu lieu
					if (skubom.getProduct_type() < 30 || skubom.getProduct_type() > 50)
						continue;
				}
				Runnable demand = new calDemand(skubom, ls_SKUBalance, pcontractid_link, pcontract_poid_link,
						productid_link, product_skuid_link, product_sku_code, product_sku_color, product_sku_size,
						p_amount, bomPOLine_Service, po_buyer, p_amount_dh, p_amount_stockout);
				executor.execute(demand);
			}
			executor.shutdown();
			// Wait until all threads are finish
			while (!executor.isTerminated()) {

			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static class calDemand implements Runnable {
		private final PContractBOM2SKU skubom;
		private final List<Recon_MatSKU_Data> ls_SKUBalance;
		private final Long pcontractid_link;
		private final Long pcontract_poid_link;
		private final Long productid_link;
		private final Long product_skuid_link;
		private final String product_sku_code;
		private final String product_sku_color;
		private final String product_sku_size;
		private final Integer p_amount;
		private final String po_buyer;
		private final Integer p_amount_dh;
		private final Integer p_amount_stockout;

		private final IPContract_bom2_npl_poline_Service bomPOLine_Service;

		calDemand(PContractBOM2SKU skubom, List<Recon_MatSKU_Data> ls_SKUBalance, Long pcontractid_link,
				Long pcontract_poid_link, Long productid_link, Long product_skuid_link, String product_sku_code,
				String product_sku_color, String product_sku_size, Integer p_amount,
				IPContract_bom2_npl_poline_Service bomPOLine_Service, String po_buyer, Integer p_amount_dh, Integer p_amount_stockout) {
			this.skubom = skubom;
			this.ls_SKUBalance = ls_SKUBalance;
			this.pcontractid_link = pcontractid_link;
			this.pcontract_poid_link = pcontract_poid_link;
			this.productid_link = productid_link;
			this.product_skuid_link = product_skuid_link;
			this.product_sku_code = product_sku_code;
			this.product_sku_color = product_sku_color;
			this.product_sku_size = product_sku_size;
			this.p_amount = p_amount;
			this.bomPOLine_Service = bomPOLine_Service;
			this.po_buyer = po_buyer;
			this.p_amount_dh = p_amount_dh;
			this.p_amount_stockout = p_amount_stockout;
		}

		@Override
		public void run() {
			try {
				// if (skubom.getMaterialCode().contains("CXI55020")){
				// System.out.println(skubom.getMaterial_skuid_link() + "/" +
				// skubom.getMaterialCode() + "-" + p_amount);
				// }
				// Kiểm tra xem NPL có trong danh sách giới hạn PO không
				// (pcontract_bom2_npl_poline)?
				// Nếu có, kiểm tra tiếp xem có giới hạn áp dụng cụ thể cho từng product_sku ko?
				// SL áp dụng là bao nhiêu
				List<PContract_bom2_npl_poline> ls_poline = bomPOLine_Service.getby_product_and_npl(productid_link,
						pcontractid_link, skubom.getMaterial_skuid_link());
				if (ls_poline.size() > 0) {
					boolean isfound = false;
					// Check nếu poline gửi vào ko có trong danh sách --> Bỏ qua NPL này
					for (PContract_bom2_npl_poline thebom_poline : ls_poline) {
						if (pcontract_poid_link.equals(thebom_poline.getPcontract_poid_link())) {
							isfound = true;
							// Kiem tra tiep xem có giới hạn sâu hơn trong pcontract_bom2_npl_poline_sku ko?

						}
					}
					// Bo qua NPL
					if (!isfound)
						return;
				}

				Recon_MatSKU_Data theSKUBalance = ls_SKUBalance.stream()
						.filter(sku -> sku.getMat_skuid_link().equals(skubom.getMaterial_skuid_link())).findAny()
						.orElse(null);
				if (null != theSKUBalance) {
					// Tinh tong dinh muc
					Float f_skudemand = skubom.getAmount() * p_amount * skubom.getLost_ratio();
					Float f_skudemand_dh = skubom.getAmount() * p_amount_dh * skubom.getLost_ratio();
					//So NPL da xuat kho theo san pham xuat
					Float f_skudemand_stockout = skubom.getAmount() * p_amount_stockout * skubom.getLost_ratio();
//					Float f_lost = (f_skudemand*skubom.getLost_ratio())/100;

					// Tinh trung binh dinh muc
					Float f_skubomamount = (theSKUBalance.getMat_sku_bom_amount() + skubom.getAmount()) / 2;
					theSKUBalance.setMat_sku_bom_amount(f_skubomamount);

					theSKUBalance.setMat_sku_demand(theSKUBalance.getMat_sku_demand() + f_skudemand);
					theSKUBalance.setMat_sku_product_total(theSKUBalance.getMat_sku_product_total() + p_amount);
					theSKUBalance.setMat_sku_product(theSKUBalance.getMat_sku_product() + p_amount_dh);
					theSKUBalance.setMat_sku_demand_dh(theSKUBalance.getMat_sku_demand_dh() + f_skudemand_dh);
					//So NPL da xuat kho theo san pham xuat
					theSKUBalance.setMat_sku_byproduct_stockout(theSKUBalance.getMat_sku_byproduct_stockout() + f_skudemand_stockout);

					// Thong tin chi tiet mau co
					SKUBalance_Product_D_Data product_d = new SKUBalance_Product_D_Data();
					product_d.setP_skuid_link(product_skuid_link);
					product_d.setP_sku_code(product_sku_code);
					product_d.setP_sku_color(product_sku_color);
					product_d.setP_sku_size(product_sku_size);
					product_d.setP_amount(p_amount);
					product_d.setP_bom_amount(skubom.getAmount());
					product_d.setP_bom_lostratio(skubom.getLost_ratio());
					product_d.setP_bom_demand(f_skudemand);
					product_d.setP_bom_demand_dh(f_skudemand_dh);
					product_d.setPo_buyer(po_buyer);
					product_d.setP_amount_dh(p_amount_dh);
					theSKUBalance.getProduct_d().add(product_d);

				} else {
					Recon_MatSKU_Data newSKUBalance = new Recon_MatSKU_Data();
					newSKUBalance.setMat_skuid_link(skubom.getMaterial_skuid_link());
					newSKUBalance.setMat_sku_product_total(p_amount);
					newSKUBalance.setMat_sku_product(p_amount_dh);

					newSKUBalance.setMat_sku_code(skubom.getMaterialCode());
					newSKUBalance.setMat_sku_name(skubom.getMaterialCode());
					newSKUBalance.setMat_sku_desc(skubom.getDescription_product());
					newSKUBalance.setMat_sku_unit_name(skubom.getUnitName());
					newSKUBalance.setMat_sku_size_name(skubom.getCoKho());
					newSKUBalance.setMat_sku_color_name(skubom.getTenMauNPL());
					newSKUBalance.setMat_sku_product_typename(skubom.getProduct_typeName());
					newSKUBalance.setMat_sku_product_typeid_link(skubom.getProduct_type());

					newSKUBalance.setMat_sku_bom_lostratio(skubom.getLost_ratio());
					newSKUBalance.setMat_sku_bom_amount(skubom.getAmount());

					Float f_skudemand = skubom.getAmount() * p_amount * skubom.getLost_ratio();
					Float f_skudemand_dh = skubom.getAmount() * p_amount_dh * skubom.getLost_ratio();
					//So NPL da xuat kho theo san pham xuat
					Float f_skudemand_stockout = skubom.getAmount() * p_amount_stockout * skubom.getLost_ratio();
//					Float f_lost = (f_skudemand*skubom.getLost_ratio())/100;
					newSKUBalance.setMat_sku_demand(f_skudemand);
					newSKUBalance.setMat_sku_demand_dh(f_skudemand_dh);
					//So NPL da xuat kho theo san pham xuat
					newSKUBalance.setMat_sku_byproduct_stockout(f_skudemand_stockout);

					// Thong tin chi tiet mau co
//					SKUBalance_Product_D_Data product_d = new SKUBalance_Product_D_Data();
//					product_d.setP_skuid_link(product_skuid_link);
//					product_d.setP_sku_code(product_sku_code);
//					product_d.setP_sku_color(product_sku_color);
//					product_d.setP_sku_size(product_sku_size);
//					product_d.setP_amount(p_amount);
//					product_d.setP_bom_amount(skubom.getAmount());
//					product_d.setP_bom_lostratio(skubom.getLost_ratio());
//					product_d.setP_bom_demand(f_skudemand);
//					product_d.setP_bom_demand_dh(f_skudemand_dh);
//					product_d.setPo_buyer(po_buyer);
//					product_d.setP_amount_dh(p_amount_dh);
//					newSKUBalance.getProduct_d().add(product_d);

					ls_SKUBalance.add(newSKUBalance);
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
