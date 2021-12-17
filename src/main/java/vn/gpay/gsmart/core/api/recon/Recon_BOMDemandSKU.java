package vn.gpay.gsmart.core.api.recon;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.apache.commons.collections4.IterableUtils;
import org.apache.commons.collections4.Predicate;

import vn.gpay.gsmart.core.api.balance.SKUBalance_Product_D_Data;
import vn.gpay.gsmart.core.api.pcontractproductbom.PContractProductBom2API;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.IPContract_bom2_npl_poline_Service;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.PContract_bom2_npl_poline_sku;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;

public class Recon_BOMDemandSKU implements Runnable{
	private Thread t;
	private final List<Recon_MatSKU_Data> ls_SKUBalance;
	private Long pcontract_poid_link;
	private Long product_skuid_link;
	private PContractProductSKU product_sku;
	private Integer Recon_Type;

	private CountDownLatch latch;
	
	private List<PContract_bom2_npl_poline> ls_bom_poline;
	private List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku;
	private List<PContractBOM2SKU> bom_contract;
	
	Recon_BOMDemandSKU(
			List<PContractBOM2SKU> bom_contract,
			List<Recon_MatSKU_Data> ls_SKUBalance, 
			PContractProductSKU product_sku,
			PContractProductBom2API bom2Service,
			List<PContract_bom2_npl_poline> ls_bom_poline,
			List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku,
			CountDownLatch latch,
			Integer Recon_Type
			){
		this.bom_contract = bom_contract;
		this.ls_SKUBalance = ls_SKUBalance;
		this.product_sku = product_sku;
		this.pcontract_poid_link = product_sku.getPcontract_poid_link();
		this.product_skuid_link = product_sku.getSkuid_link();
		
		this.Recon_Type = Recon_Type;
		this.ls_bom_poline = ls_bom_poline;
		this.ls_bom_poline_sku = ls_bom_poline_sku;
		
		this.latch = latch;
	}
	
	@Override
	public void run() {
		try {
			//Lay danh sach BOM cua product_sku
//			List<PContractBOM2SKU> bom_response = bom2Service.getBOM_By_PContractSKU(pcontractid_link, product_skuid_link);
			List<PContractBOM2SKU> bom_response = bom_contract.stream().filter(sku -> sku.getProduct_skuid_link().equals(product_skuid_link)).collect(Collectors.toList());
			for (PContractBOM2SKU mat_skubom : bom_response) {
				calDemand_Candoi(mat_skubom);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			latch.countDown();
		}
	}
	
	public void start() {
		if (t == null) {
			int unboundedRandomValue = ThreadLocalRandom.current().nextInt();
			t = new Thread(this, String.valueOf(unboundedRandomValue));
			t.start();
		}
	}
	
	//Tinh nhu cau NPL theo dinh muc can doi --> Quyet toan voi Vendor
	private void calDemand_Candoi(PContractBOM2SKU mat_skubom) {
		try {
			Integer p_amount =  0;
			//Tuy theo loai quyet toan de lay so luong dua vao tinh toan
			if (Recon_Type == 0) {//Quyet toan can doi
				p_amount = product_sku.getPquantity_porder();
			} else if (Recon_Type == 1) {//Quyet toan Hai quan
				p_amount = product_sku.getPquantity_porder();
			} else if (Recon_Type == 2) {//Quyet toan san xuat (noi bo)
				p_amount = product_sku.getPquantity_total();
			}
		
			//Kiem tra xem NPL co nam trong danh sach gioi han po-line khong?
			List<PContract_bom2_npl_poline> ls_bom_poline_check = ls_bom_poline.stream().filter(sku -> sku.getNpl_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
			
			if (ls_bom_poline_check.size() > 0) {
				if (!ls_bom_poline_check.stream().anyMatch(po -> po.getPcontract_poid_link().equals(pcontract_poid_link))) {
					return;
				} else {
					List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku_check = ls_bom_poline_sku.stream().filter(sku -> sku.getMaterial_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
					//Kiem tra xem NPL co trong danh sach gioi han mau co ko
					if (ls_bom_poline_sku_check.size() > 0) {
						if (!ls_bom_poline_sku_check.stream().anyMatch(po -> po.getProduct_skuid_link().equals(product_skuid_link))) {
							return;
						}
					}
				}
			}
			

			Recon_MatSKU_Data theSKUBalance = ls_SKUBalance.stream()
					.filter(sku -> sku.getMat_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).findFirst()
					.orElse(null);
			
//			Recon_MatSKU_Data theSKUBalance = IterableUtils.find(ls_SKUBalance,
//					  new Predicate<Recon_MatSKU_Data>() {
//					      public boolean evaluate(Recon_MatSKU_Data matsku) {
//					          return matsku.getMat_skuid_link().equals(mat_skubom.getMaterial_skuid_link());
//					      }
//					  });
			
			if (null != theSKUBalance) {
				// Tinh tong dinh muc
				Float f_skudemand = mat_skubom.getAmount() * p_amount * mat_skubom.getLost_ratio();
				// Tinh trung binh dinh muc
				Float f_skubomamount = (theSKUBalance.getMat_sku_bom_amount() + mat_skubom.getAmount()) / 2;
				theSKUBalance.setMat_sku_bom_amount(f_skubomamount);

				theSKUBalance.setMat_sku_demand(theSKUBalance.getMat_sku_demand() + f_skudemand);
////				// Thong tin chi tiet mau co
////				SKUBalance_Product_D_Data product_d = new SKUBalance_Product_D_Data();
////				product_d.setP_skuid_link(product_skuid_link);
////				product_d.setP_sku_code(product_sku.getSkuCode());
//////				product_d.setP_sku_color(product_sku.getMauSanPham());
//////				product_d.setP_sku_size(product_sku.getCoSanPham());
////				product_d.setP_amount(p_amount);
////				product_d.setP_bom_amount(skubom.getAmount());
////				product_d.setP_bom_lostratio(skubom.getLost_ratio());
////				product_d.setP_bom_demand(f_skudemand);
////				theSKUBalance.getProduct_d().add(product_d);
//
			} else {
				System.out.println(mat_skubom.getMaterialCode());
				Recon_MatSKU_Data newSKUBalance = new Recon_MatSKU_Data();
				newSKUBalance.setMat_skuid_link(mat_skubom.getMaterial_skuid_link());
				newSKUBalance.setMat_sku_product_total(p_amount);

				newSKUBalance.setMat_sku_code(mat_skubom.getMaterialCode());
				newSKUBalance.setMat_sku_name(mat_skubom.getMaterialCode());
				newSKUBalance.setMat_sku_desc(mat_skubom.getDescription_product());
				newSKUBalance.setMat_sku_unit_name(mat_skubom.getUnitName());
//				newSKUBalance.setMat_sku_size_name(skubom.getCoKho());
//				newSKUBalance.setMat_sku_color_name(skubom.getTenMauNPL());
				newSKUBalance.setMat_sku_product_typename(mat_skubom.getProduct_typeName());
				newSKUBalance.setMat_sku_product_typeid_link(mat_skubom.getProduct_type());

				newSKUBalance.setMat_sku_bom_lostratio(mat_skubom.getLost_ratio());
				newSKUBalance.setMat_sku_bom_amount(mat_skubom.getAmount());

				Float f_skudemand = mat_skubom.getAmount() * p_amount * mat_skubom.getLost_ratio();
				newSKUBalance.setMat_sku_demand(f_skudemand);

				ls_SKUBalance.add(newSKUBalance);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
