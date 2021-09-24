package vn.gpay.gsmart.core.api.recon;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

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
import vn.gpay.gsmart.core.utils.AtributeFixValues;

public class Recon_SKU implements Runnable{
	private Thread t;
	private Long pcontractid_link;
	private Long stockid_link;
	private Long pcontract_poid_link;
	private Long porderid_link;
	private Recon_MatSKU_Data mat_sku;

	String token;
	CountDownLatch latch;
	
	Recon_SKU(List<Recon_MatSKU_Data> ls_skubalance, Long pcontractid_link, Long stockid_link, Long pcontract_poid_link,
			Long porderid_link, Recon_MatSKU_Data mat_sku, String token, CountDownLatch latch) {
		this.pcontractid_link = pcontractid_link;
		this.stockid_link = stockid_link;
		this.pcontract_poid_link = pcontract_poid_link;
		this.porderid_link = porderid_link;
		this.mat_sku = mat_sku;
		this.token = token;
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
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", this.token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockin/stockind_bypcontract_and_sku";

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode appParNode = objectMapper.createObjectNode();
			appParNode.put("pcontractid_link", pcontractid_link);
			appParNode.put("stockid_link", stockid_link);
			appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
			String jsonReq = objectMapper.writeValueAsString(appParNode);

			HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
			String result = restTemplate.postForObject(urlPost, request, String.class);
//            System.out.println(result);
			Jitin_StockinList_Response ls_stockind = objectMapper.readValue(result, Jitin_StockinList_Response.class);
			if (null != ls_stockind) {
				Float met_stockin = (float) 0;
				for (Jitin_Stockin_D_Data stockinD : ls_stockind.data) {
					met_stockin += null != stockinD.getTotalmet_check() ? stockinD.getTotalmet_check() : 0;
				}
				mat_sku.setMat_sku_stockin(met_stockin);
				mat_sku.setMat_sku_dif(mat_sku.getMat_sku_stockin() - mat_sku.getMat_sku_demand());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Tinh SL NPL da xuat kho sang san xuat theo đơn hàng
	private void cal_stockout_bycontract() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", this.token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockout/stockoutd_bypcontract_and_sku";

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode appParNode = objectMapper.createObjectNode();
			appParNode.put("pcontractid_link", pcontractid_link);
			appParNode.put("pcontract_poid_link", pcontract_poid_link);
			appParNode.put("porderid_link", porderid_link);
			appParNode.put("stockid_link", stockid_link);
			appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
			String jsonReq = objectMapper.writeValueAsString(appParNode);

			HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
			String result = restTemplate.postForObject(urlPost, request, String.class);
//            System.out.println(result);
			Jitin_StockoutList_Response ls_stockoutd = objectMapper.readValue(result,
					Jitin_StockoutList_Response.class);

			if (null != ls_stockoutd) {

				Float met_stockout = (float) 0;
				for (Jitin_StockOutD_Data stockoutD : ls_stockoutd.data) {
//            		System.out.println(this.mat_sku.getMat_skuid_link() + "-" + stockoutD.getTotalmet_check());
					met_stockout += null != stockoutD.getTotalmet_check() ? stockoutD.getTotalmet_check() : 0;
				}
				mat_sku.setMat_sku_stockout(met_stockout);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
