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

public class Balance_SKU implements Runnable{
	private Thread t;
	private Long pcontractid_link;
	private Long poid_link;
	private SKUBalance_Data mat_sku;

	String token;
	CountDownLatch latch;
	
	Balance_SKU(
			List<SKUBalance_Data> ls_skubalance,
			Long pcontractid_link,
			Long poid_link,
			SKUBalance_Data mat_sku,
			String token,
			CountDownLatch latch){
		this.pcontractid_link = pcontractid_link;
		this.poid_link = poid_link;
		this.mat_sku = mat_sku;
		this.token =  token;
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
//			cal_invoice();
			cal_stockin_bycontract();
			cal_stockout_bycontract();
			latch.countDown();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void start () {
		if (t == null) {
			int unboundedRandomValue = ThreadLocalRandom.current().nextInt();
			t = new Thread (this, String.valueOf(unboundedRandomValue));
			t.start ();
		}
	}
	
	//Tinh SL da dat hang qua Invoice
	private void cal_invoice(){
		try {
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
//	        headers.setBearerAuth(this.token);
	        headers.set("authorization", this.token);
	        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
	        headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin+"/api/v1/invoice/invoiced_bycontract_and_sku";
            
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode appParNode = objectMapper.createObjectNode();
            appParNode.put("pcontractid_link", this.pcontractid_link);
            appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
            String jsonReq = objectMapper.writeValueAsString(appParNode);
            
            HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
            String result = restTemplate.postForObject(urlPost, request, String.class);
//            System.out.println(result);
            Jitin_Invoice_Response ls_invoiced = objectMapper.readValue(result, Jitin_Invoice_Response.class);
            if (null != ls_invoiced){
            	Float met_invoice = (float) 0;
            	for(Jitin_Invoice_D_Data invoiceD: ls_invoiced.data){
            		met_invoice+=invoiceD.getMet();
            		mat_sku.setMat_sku_invoice_date(invoiceD.getInvoice_shipdateto());
            		
            		//Tinh so luong da nhap kho theo Invoice
            		cal_stockin(invoiceD.getInvoiceid_link());
            	}
            	mat_sku.setMat_sku_invoice(met_invoice);
            	
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	//Tinh SL da nhap kho
	private void cal_stockin(Long invoiceid_link){
		try {
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("authorization", this.token);
	        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
	        headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin+"/api/v1/stockin/stockind_byinvoice_and_sku";
            
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode appParNode = objectMapper.createObjectNode();
            appParNode.put("material_invoiceid_link", invoiceid_link);
            appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
            String jsonReq = objectMapper.writeValueAsString(appParNode);
            
            HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
            String result = restTemplate.postForObject(urlPost, request, String.class);
//            System.out.println(result);
            Jitin_StockinList_Response ls_stockind = objectMapper.readValue(result, Jitin_StockinList_Response.class);
            if (null != ls_stockind){
            	Float met_stockin = (float) 0;
            	for(Jitin_Stockin_D_Data stockinD: ls_stockind.data){
            		met_stockin+=stockinD.getTotalmet_check();
            	}
            	mat_sku.setMat_sku_stockin(met_stockin);
            	mat_sku.setMat_sku_dif(mat_sku.getMat_sku_stockin() - mat_sku.getMat_sku_demand());
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	//Tinh SL da nhap kho
	private void cal_stockin_bycontract(){
		try {
	    	RestTemplate restTemplate = new RestTemplate();
	    	HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        headers.set("authorization", this.token);
	        headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
	        headers.setAccessControlRequestMethod(HttpMethod.POST);
			String urlPost = AtributeFixValues.url_jitin+"/api/v1/stockin/stockind_bypcontract_and_sku";
            
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode appParNode = objectMapper.createObjectNode();
            appParNode.put("pcontractid_link", pcontractid_link);
            appParNode.put("skuid_link", this.mat_sku.getMat_skuid_link());
            String jsonReq = objectMapper.writeValueAsString(appParNode);
            
            HttpEntity<String> request = new HttpEntity<String>(jsonReq, headers);
            String result = restTemplate.postForObject(urlPost, request, String.class);
//            System.out.println(result);
            Jitin_StockinList_Response ls_stockind = objectMapper.readValue(result, Jitin_StockinList_Response.class);
            if (null != ls_stockind){
            	Float met_stockin = (float) 0;
            	for(Jitin_Stockin_D_Data stockinD: ls_stockind.data){
            		met_stockin+=stockinD.getTotalmet_check();
            	}
            	mat_sku.setMat_sku_stockin(met_stockin);
            	mat_sku.setMat_sku_dif(mat_sku.getMat_sku_stockin() - mat_sku.getMat_sku_demand());
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	//Tinh SL da xuat kho
	private void cal_stockout_bycontract(){
		return;
	}
}
