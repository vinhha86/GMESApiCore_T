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
	private SKUBalance_Data mat_sku;

	String token;
	CountDownLatch latch;
	
	Balance_SKU(
			List<SKUBalance_Data> ls_skubalance,
			Long pcontractid_link,
			SKUBalance_Data mat_sku,
			String token,
			CountDownLatch latch){
		this.pcontractid_link = pcontractid_link;
		this.mat_sku = mat_sku;
		this.token =  token;
		this.latch = latch;
	}
	@Override
	public void run() {
		try {
			cal_invoice();
			cal_stockin();
			cal_stockout();
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
            System.out.println(result);
            Balance_Invoice_Response ls_invoiced = objectMapper.readValue(result, Balance_Invoice_Response.class);
            if (null != ls_invoiced){
            	Float yds_invoice = (float) 0;
            	for(Balance_Invoice_Data invoiceD: ls_invoiced.data){
            		yds_invoice+=invoiceD.getYds();
            	}
            	mat_sku.setMat_sku_invoice(yds_invoice);
            }
			
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	//Tinh SL da nhap kho
	private void cal_stockin(){
		return;
	}
	//Tinh SL da xuat kho
	private void cal_stockout(){
		return;
	}
}
