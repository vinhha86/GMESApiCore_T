package vn.gpay.gsmart.core.api.recon;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import vn.gpay.gsmart.core.api.balance.Jitin_StockOutD_Data;
import vn.gpay.gsmart.core.api.balance.Jitin_StockinList_Response;
import vn.gpay.gsmart.core.api.balance.Jitin_Stockin_D_Data;
import vn.gpay.gsmart.core.api.balance.Jitin_StockoutList_Response;
import vn.gpay.gsmart.core.pcontractproductsku.PContractProductSKU;
import vn.gpay.gsmart.core.utils.AtributeFixValues;

public class Recon_ProductSKU implements Runnable{
	private Thread t;
	private List<Jitin_Stockin_D_Data> ls_PStockin;
	private List<Jitin_StockOutD_Data> ls_PStockout;
	private PContractProductSKU product_sku;

	String token;
	CountDownLatch latch;
	
	Recon_ProductSKU(
			List<Jitin_Stockin_D_Data> ls_PStockin,
			List<Jitin_StockOutD_Data> ls_PStockout,
			PContractProductSKU product_sku, 
			CountDownLatch latch) {
		this.ls_PStockin = ls_PStockin;
		this.ls_PStockout = ls_PStockout;
		this.product_sku = product_sku;
		this.latch = latch;
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
	
	// Tinh SL thanh pham da nhap kho theo đơn hàng
	private void cal_stockin_bycontract() {
		try {
			List<Jitin_Stockin_D_Data> ls_stockind = ls_PStockin.stream().filter(sku -> sku.getSkuid_link().equals(product_sku.getSkuid_link())).collect(Collectors.toList());
			if (ls_stockind.size() > 0) {
				Integer package_stockin = 0;
				for (Jitin_Stockin_D_Data stockinD : ls_stockind) {
					package_stockin += null != stockinD.getTotalpackage() ? stockinD.getTotalpackage() : 0;
				}
				product_sku.setPquantity_stockin(package_stockin);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Tinh SL thanh pham da xuat kho thanh pham theo đơn hàng
	private void cal_stockout_bycontract() {
		try {
			List<Jitin_StockOutD_Data> ls_stockoutd = ls_PStockout.stream().filter(sku -> sku.getSkuid_link().equals(product_sku.getSkuid_link())).collect(Collectors.toList());
			if (ls_stockoutd.size() > 0) {
				Integer package_stockout = 0;
				for (Jitin_StockOutD_Data stockoutD : ls_stockoutd) {
//            		System.out.println(this.mat_sku.getMat_skuid_link() + "-" + stockoutD.getTotalmet_check());
					package_stockout += null != stockoutD.getTotalpackage() ? stockoutD.getTotalpackage() : 0;
				}
				product_sku.setPquantity_stockout(package_stockout);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
