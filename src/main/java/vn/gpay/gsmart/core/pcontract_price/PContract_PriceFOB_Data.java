package vn.gpay.gsmart.core.pcontract_price;

import java.io.Serializable;

public class PContract_PriceFOB_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private String fob_price;
	private String provider_name;
	public String getFob_price() {
		return fob_price;
	}
	public void setFob_price(String fob_price) {
		this.fob_price = fob_price;
	}
	public String getProvider_name() {
		return provider_name;
	}
	public void setProvider_name(String provider_name) {
		this.provider_name = provider_name;
	}
	
	
}
