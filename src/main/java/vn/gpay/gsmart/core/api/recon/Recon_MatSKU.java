package vn.gpay.gsmart.core.api.recon;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import vn.gpay.gsmart.core.api.balance.Jitin_StockOutD_Data;
import vn.gpay.gsmart.core.api.balance.Jitin_Stockin_D_Data;

public class Recon_MatSKU implements Runnable{
	private Thread t;
	private List<Jitin_Stockin_D_Data> ls_MStockin;
	private List<Jitin_StockOutD_Data> ls_MStockout;
	private Recon_MatSKU_Data mat_sku;

	String token;
	CountDownLatch latch;
	
	Recon_MatSKU(
			List<Jitin_Stockin_D_Data> ls_MStockin,
			List<Jitin_StockOutD_Data> ls_MStockout,
			Recon_MatSKU_Data mat_sku,
			CountDownLatch latch) {
		this.ls_MStockin = ls_MStockin;
		this.ls_MStockout = ls_MStockout;
		this.mat_sku = mat_sku;
		this.latch = latch;

		// Nếu tính cân bằng theo PO line --> Lấy danh sách SKU của PO Line

		// Nếu tính cân bằng theo POrder --> Lấy danh sách SKU của POrder
	}
	
	@Override
	public void run() {
		try {
			cal_stockin_bycontract();
			cal_stockout_bycontract();
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
	
	// Tinh SL NPL da nhap kho theo đơn hàng
	private void cal_stockin_bycontract() {
		try {
			List<Jitin_Stockin_D_Data> ls_stockind = ls_MStockin.stream().parallel().filter(sku -> sku.getSkuid_link().equals(mat_sku.getMat_skuid_link())).collect(Collectors.toList());
			if (ls_stockind.size() > 0) {
				Float met_stockin = (float) 0;
				for (Jitin_Stockin_D_Data stockinD : ls_stockind) {
					met_stockin += null != stockinD.getTotalmet_check() ? stockinD.getTotalmet_check() : 0;
				}
				mat_sku.setMat_sku_stockin(met_stockin);
			}
//			Double met_stockin = ls_MStockin.stream().parallel().filter(sku -> sku.getSkuid_link().equals(mat_sku.getMat_skuid_link())).mapToDouble(x -> x.getTotalpackage()).sum();
//			mat_sku.setMat_sku_stockin(met_stockin.floatValue());
			System.out.println("stockin: " + mat_sku.getMat_skuid_link() + " : " + mat_sku.getMat_sku_stockin());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Tinh SL NPL da xuat kho sang san xuat theo đơn hàng
	private void cal_stockout_bycontract() {
		try {
			
			List<Jitin_StockOutD_Data> ls_stockoutd = ls_MStockout.stream().parallel().filter(sku -> sku.getSkuid_link().equals(mat_sku.getMat_skuid_link())).collect(Collectors.toList());
			if (ls_stockoutd.size() > 0) {
				Float met_stockout = (float)0;
				for (Jitin_StockOutD_Data stockoutD : ls_stockoutd) {
//            		System.out.println(this.mat_sku.getMat_skuid_link() + "-" + stockoutD.getTotalmet_check());
					met_stockout += null != stockoutD.getTotalmet_check() ? stockoutD.getTotalmet_check() : 0;
				}
				mat_sku.setMat_sku_stockout(met_stockout);
			}
//			Double met_stockout = ls_MStockout.stream().parallel().filter(sku -> sku.getSkuid_link().equals(mat_sku.getMat_skuid_link())).mapToDouble(x -> x.getTotalpackage()).sum();
//			mat_sku.setMat_sku_stockout(met_stockout.floatValue());
			System.out.println("stockout: " + mat_sku.getMat_skuid_link() + " : " + mat_sku.getMat_sku_stockout());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
