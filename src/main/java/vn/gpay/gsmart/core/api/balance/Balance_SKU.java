package vn.gpay.gsmart.core.api.balance;

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

import vn.gpay.gsmart.core.utils.AtributeFixValues;

public class Balance_SKU implements Runnable {
	private Thread t;
	private Long pcontractid_link;
	private Long stockid_link;
	private Long pcontract_poid_link;
	private Long porderid_link;
	private SKUBalance_Data mat_sku;

	String token;
	CountDownLatch latch;

	Balance_SKU(List<SKUBalance_Data> ls_skubalance, Long pcontractid_link, Long stockid_link, Long pcontract_poid_link,
			Long porderid_link, SKUBalance_Data mat_sku, String token, CountDownLatch latch) {
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
//			cal_invoice();
			cal_stockin_bycontract();
			cal_stockout_order_bycontract();
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

	// Tinh SL da dat hang qua Invoice
//	private void cal_invoice(){
//		try {
//	    	RestTemplate restTemplate = new RestTemplate();
//	    	HttpHeaders headers = new HttpHeaders();
//	        headers.setContentType(MediaType.APPLICATION_JSON);
////	        headers.setBearerAuth(this.token);
//	        headers.set("authorization", this.token);
//	        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//	        headers.setAccessControlRequestMethod(HttpMethod.POST);
//			String urlPost = AtributeFixValues.url_jitin+"/api/v1/invoice/invoiced_bycontract_and_sku";
//            
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectNode appParNode = objectMapper.createObjectNode();
//            appParNode.put("pcontractid_link", this.pcontractid_link);
//            appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
//            String jsonReq = objectMapper.writeValueAsString(appParNode);
//            
//            HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
//            String result = restTemplate.postForObject(urlPost, request, String.class);
////            System.out.println(result);
//            Jitin_Invoice_Response ls_invoiced = objectMapper.readValue(result, Jitin_Invoice_Response.class);
//            if (null != ls_invoiced){
//            	Float met_invoice = (float) 0;
//            	for(Jitin_Invoice_D_Data invoiceD: ls_invoiced.data){
//            		met_invoice+=invoiceD.getMet();
//            		mat_sku.setMat_sku_invoice_date(invoiceD.getInvoice_shipdateto());
//            		
//            		//Tinh so luong da nhap kho theo Invoice
//            		cal_stockin(invoiceD.getInvoiceid_link());
//            	}
//            	mat_sku.setMat_sku_invoice(met_invoice);
//            	
//            }
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//	}
	// Tinh SL da nhap kho
//	private void cal_stockin(Long invoiceid_link){
//		try {
//	    	RestTemplate restTemplate = new RestTemplate();
//	    	HttpHeaders headers = new HttpHeaders();
//	        headers.setContentType(MediaType.APPLICATION_JSON);
//	        headers.set("authorization", this.token);
//	        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
//	        headers.setAccessControlRequestMethod(HttpMethod.POST);
//			String urlPost = AtributeFixValues.url_jitin+"/api/v1/stockin/stockind_byinvoice_and_sku";
//            
//            ObjectMapper objectMapper = new ObjectMapper();
//            ObjectNode appParNode = objectMapper.createObjectNode();
//            appParNode.put("material_invoiceid_link", invoiceid_link);
//            appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
//            String jsonReq = objectMapper.writeValueAsString(appParNode);
//            
//            HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
//            String result = restTemplate.postForObject(urlPost, request, String.class);
////            System.out.println(result);
//            Jitin_StockinList_Response ls_stockind = objectMapper.readValue(result, Jitin_StockinList_Response.class);
//            if (null != ls_stockind){
//            	Float met_stockin = (float) 0;
//            	for(Jitin_Stockin_D_Data stockinD: ls_stockind.data){
//            		met_stockin+=stockinD.getTotalmet_check();
//            	}
//            	mat_sku.setMat_sku_stockin(met_stockin);
//            	mat_sku.setMat_sku_dif(mat_sku.getMat_sku_stockin() - mat_sku.getMat_sku_demand());
//            }
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}	
//	}
	
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
//					System.out.println(stockinD.getStockinid_link() + "-" + stockinD.getTotalmet_check());
					met_stockin += null != stockinD.getTotalmet_check() ? stockinD.getTotalmet_check() : 0;
//					System.out.println(met_stockin);
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
//			appParNode.put("porderid_link", porderid_link);
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

	// Tinh SL da lap yeu cau xuat kho sang san xuat
	private void cal_stockout_order_bycontract() {
		try {
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("authorization", this.token);
			headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
			headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin + "/api/v1/stockoutorder/stockout_orderd_bypcontract_and_sku";

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
			Jitin_Stockout_OrderList_Response ls_stockout_orderd = objectMapper.readValue(result,
					Jitin_Stockout_OrderList_Response.class);

			if (null != ls_stockout_orderd) {

				Float met_stockout_order = (float) 0;
				for (Jitin_Stockout_order_d_Data stockout_OrderD : ls_stockout_orderd.data) {
//            		System.out.println(this.mat_sku.getMat_skuid_link() + "-" + stockout_OrderD.getTotalmet());
					met_stockout_order += null != stockout_OrderD.getTotalmet() ? stockout_OrderD.getTotalmet() : 0;
				}
				mat_sku.setMat_sku_stockout_order(met_stockout_order);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
