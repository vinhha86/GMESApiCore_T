package vn.gpay.gsmart.core.invoice;

import java.util.Date;


public class InvoiceList {

	private String invoicenumber;
	private Date invoicedate;
	private Long orgfrom_code;
	private String orgfrom_name;
	private Long stockcode;
	private Date shipdatefrom;
	private Date shipdateto;
	private String shipperson;
	private String customsnumber;
	private Float totalpackage;
	private Float totalm3;
	private Float totalnetweight;
	private Float totalgrossweight;
	private String productcode;
	
	public InvoiceList(Object[] columns) {
	 	this.invoicenumber = (String) columns[0];
        this.invoicedate = (Date) columns[1];
        this.orgfrom_code = (Long) columns[2];
        this.orgfrom_name = (String) columns[3];
        this.stockcode = (Long) columns[4];
        this.shipdatefrom = (Date) columns[5];
        this.shipdateto = (Date) columns[6];
        this.shipperson = (String) columns[7];
        this.customsnumber = (String) columns[8];
        this.totalpackage = (Float) columns[9];
        this.totalm3 = (Float) columns[10];
        this.totalnetweight = (Float) columns[11];
        this.totalgrossweight = (Float) columns[12];
        this.productcode = (String) columns[13];
	}

	public String getInvoicenumber() {
		return invoicenumber;
	}

	public void setInvoicenumber(String invoicenumber) {
		this.invoicenumber = invoicenumber;
	}

	public Date getInvoicedate() {
		return invoicedate;
	}

	public void setInvoicedate(Date invoicedate) {
		this.invoicedate = invoicedate;
	}

	public Long getOrgfrom_code() {
		return orgfrom_code;
	}

	public void setOrgfrom_code(Long orgfrom_code) {
		this.orgfrom_code = orgfrom_code;
	}

	public String getOrgfrom_name() {
		return orgfrom_name;
	}

	public void setOrgfrom_name(String orgfrom_name) {
		this.orgfrom_name = orgfrom_name;
	}

	public Long getStockcode() {
		return stockcode;
	}

	public void setStockcode(Long stockcode) {
		this.stockcode = stockcode;
	}

	public Date getShipdatefrom() {
		return shipdatefrom;
	}

	public void setShipdatefrom(Date shipdatefrom) {
		this.shipdatefrom = shipdatefrom;
	}

	public Date getShipdateto() {
		return shipdateto;
	}

	public void setShipdateto(Date shipdateto) {
		this.shipdateto = shipdateto;
	}

	public String getShipperson() {
		return shipperson;
	}

	public void setShipperson(String shipperson) {
		this.shipperson = shipperson;
	}

	public String getCustomsnumber() {
		return customsnumber;
	}

	public void setCustomsnumber(String customsnumber) {
		this.customsnumber = customsnumber;
	}

	public Float getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Float totalpackage) {
		this.totalpackage = totalpackage;
	}

	public Float getTotalm3() {
		return totalm3;
	}

	public void setTotalm3(Float totalm3) {
		this.totalm3 = totalm3;
	}

	public Float getTotalnetweight() {
		return totalnetweight;
	}

	public void setTotalnetweight(Float totalnetweight) {
		this.totalnetweight = totalnetweight;
	}

	public Float getTotalgrossweight() {
		return totalgrossweight;
	}

	public void setTotalgrossweight(Float totalgrossweight) {
		this.totalgrossweight = totalgrossweight;
	}

	public String getProductcode() {
		return productcode;
	}

	public void setProductcode(String productcode) {
		this.productcode = productcode;
	}
	
}
