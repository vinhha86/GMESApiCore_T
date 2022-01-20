package vn.gpay.gsmart.core.api.recon;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import vn.gpay.gsmart.core.pcontract_bom2_npl_poline.PContract_bom2_npl_poline;
import vn.gpay.gsmart.core.pcontract_bom2_npl_poline_sku.PContract_bom2_npl_poline_sku;
import vn.gpay.gsmart.core.pcontractbomsku.PContractBOM2SKU;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU_NoLink;

public class Recon_BOMDemandSKU implements Runnable{
	private Thread t;
	HashMap<Long, Recon_MatSKU_Data> hash_MatSKUBalance;
	private PContractProductSKU_NoLink product_sku;

	private CountDownLatch latch;
	
	private List<PContract_bom2_npl_poline> ls_bom_poline;
	private List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku;
	private List<PContractBOM2SKU> bom_contract;
	
	Recon_BOMDemandSKU(
			List<PContractBOM2SKU> bom_contract,
			HashMap<Long, Recon_MatSKU_Data> hash_MatSKUBalance, 
			PContractProductSKU_NoLink product_sku,
			List<PContract_bom2_npl_poline> ls_bom_poline,
			List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku,
			CountDownLatch latch,
			Integer Recon_Type
			){
		this.bom_contract = bom_contract;
		this.hash_MatSKUBalance = hash_MatSKUBalance;
		this.product_sku = product_sku;
		this.ls_bom_poline = ls_bom_poline;
		this.ls_bom_poline_sku = ls_bom_poline_sku;
		
		this.latch = latch;

	}
	
	@Override
	public void run() {
		try {
//			System.out.println("SKU:" + product_sku.getSkuid_link() + "-" + product_sku.getPcontract_poid_link() + "-" + product_sku.getPquantity_porder());
			List<PContractBOM2SKU> bom_response = bom_contract.stream().filter(sku -> sku.getProduct_skuid_link().equals(product_sku.getSkuid_link())).collect(Collectors.toList());
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
			p_amount = null != product_sku.getPquantity_porder()?product_sku.getPquantity_porder():0;
//			//Tuy theo loai quyet toan de lay so luong dua vao tinh toan
//			if (Recon_Type == 0) {//Quyet toan can doi
//				p_amount = product_sku.getPquantity_porder();
//			} else if (Recon_Type == 1) {//Quyet toan Hai quan
//				p_amount = product_sku.getPquantity_porder();
//			} else if (Recon_Type == 2) {//Quyet toan san xuat (noi bo)
//				p_amount = product_sku.getPquantity_total();
//			}
		
			//Kiem tra xem NPL co nam trong danh sach gioi han po-line khong?
			List<PContract_bom2_npl_poline> ls_bom_poline_check = ls_bom_poline.stream().filter(sku -> sku.getNpl_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
			
			if (ls_bom_poline_check.size() > 0) {
				if (!ls_bom_poline_check.stream().anyMatch(po -> po.getPcontract_poid_link().equals(product_sku.getPcontract_poid_link()))) {
//					System.out.println("Ko thuoc POLine: " + product_sku.getSkuid_link() + "-" + product_sku.getPcontract_poid_link() + "-" + product_sku.getPquantity_porder());
					return;
				} else {
					List<PContract_bom2_npl_poline_sku> ls_bom_poline_sku_check = ls_bom_poline_sku.stream().filter(sku -> sku.getMaterial_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
					//Kiem tra xem NPL co trong danh sach gioi han mau co ko
					if (ls_bom_poline_sku_check.size() > 0) {
						if (!ls_bom_poline_sku_check.stream().anyMatch(po -> po.getProduct_skuid_link().equals(product_sku.getSkuid_link()))) {
//							System.out.println("Ko thuoc mau: " + product_sku.getSkuid_link() + "-" + product_sku.getPcontract_poid_link() + "-" + product_sku.getPquantity_porder());
							return;
						}
					}
				}
			}
			
//			List<Recon_MatSKU_Data> lsSKUBalance = ls_MatSKUBalance.stream().parallel()
//					.filter(sku -> sku.getMat_skuid_link().equals(mat_skubom.getMaterial_skuid_link())).collect(Collectors.toList());
			
			Recon_MatSKU_Data theSKUBalance = hash_MatSKUBalance.get(mat_skubom.getMaterial_skuid_link());
			System.out.println("HASH: " + hash_MatSKUBalance.size());
			
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
//				System.out.println("Update MatSKU: " + mat_skubom.getMaterial_skuid_link() + "-" + theSKUBalance.getMat_sku_demand() + "-" + f_skudemand + "-"+ f_skubomamount);
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
				if (null!=mat_skubom.getMaterial_skuid_link()) {
					Recon_MatSKU_Data newSKUBalance = new Recon_MatSKU_Data();
					newSKUBalance.setMat_skuid_link(mat_skubom.getMaterial_skuid_link());
					newSKUBalance.setMat_sku_product_total(p_amount);
	
					newSKUBalance.setMat_sku_code(mat_skubom.getMaterialCode());
					newSKUBalance.setMat_sku_name(mat_skubom.getMaterialCode());
					newSKUBalance.setMat_sku_desc(mat_skubom.getDescription_product());
					newSKUBalance.setMat_sku_unit_name(mat_skubom.getUnitName());
					newSKUBalance.setMat_sku_size_name(mat_skubom.getCoKho());
					newSKUBalance.setMat_sku_color_name(mat_skubom.getTenMauNPL());
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
//					System.out.println("New MatSKU: " + mat_skubom.getMaterial_skuid_link() + "-" + f_skudemand + "-"+ mat_skubom.getAmount());
					hash_MatSKUBalance.put(mat_skubom.getMaterial_skuid_link(), newSKUBalance);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
