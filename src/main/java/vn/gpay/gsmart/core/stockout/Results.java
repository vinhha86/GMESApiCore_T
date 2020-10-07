package vn.gpay.gsmart.core.stockout;

public class Results {

	public Results(Object[] columns) {
		 	this.stockincode = (String) columns[0];
	        this.invoicenumber = (String) columns[1];
	}
	private String stockincode ;
	private String invoicenumber ;
	public String getStockincode() {
		return stockincode;
	}
	public void setStockincode(String stockincode) {
		this.stockincode = stockincode;
	}
	public String getInvoicenumber() {
		return invoicenumber;
	}
	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}
	
	
}
