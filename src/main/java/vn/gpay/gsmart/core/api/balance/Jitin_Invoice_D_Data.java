package vn.gpay.gsmart.core.api.balance;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Jitin_Invoice_D_Data implements Serializable {
	private static final long serialVersionUID = 1L;
	private Long invoiceid_link;
	private Date invoice_shipdateto;
	private Long skuid_link;
	private Integer unitid_link;
	private Integer totalpackage;
	private Float netweight;
	private Float grossweight;
	private Float foc;
	private Float m3;
	private Float met;
	private Float yds;
	private Float unitprice;
	private Float totalamount;
	
	private String skucode;
	private String size_name;
	private String skuname;
	private String color_name;
	private String hscode;
	private String hsname;
	private String unitname;
	
	private List<Jitin_Invoice_PkList_Data> packinglist = new ArrayList<>();

	public Long getInvoiceid_link() {
		return invoiceid_link;
	}

	public void setInvoiceid_link(Long invoiceid_link) {
		this.invoiceid_link = invoiceid_link;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Integer getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Integer getTotalpackage() {
		return totalpackage;
	}

	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}

	public Float getNetweight() {
		return netweight;
	}

	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}

	public Float getGrossweight() {
		return grossweight;
	}

	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}

	public Float getFoc() {
		return foc;
	}

	public void setFoc(Float foc) {
		this.foc = foc;
	}

	public Float getM3() {
		return m3;
	}

	public void setM3(Float m3) {
		this.m3 = m3;
	}

	public Float getYds() {
		return yds;
	}

	public void setYds(Float yds) {
		this.yds = yds;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Float getTotalamount() {
		return totalamount;
	}

	public void setTotalamount(Float totalamount) {
		this.totalamount = totalamount;
	}

	public String getSkucode() {
		return skucode;
	}

	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}

	public String getSize_name() {
		return size_name;
	}

	public void setSize_name(String size_name) {
		this.size_name = size_name;
	}

	public String getSkuname() {
		return skuname;
	}

	public void setSkuname(String skuname) {
		this.skuname = skuname;
	}

	public String getColor_name() {
		return color_name;
	}

	public void setColor_name(String color_name) {
		this.color_name = color_name;
	}

	public String getHscode() {
		return hscode;
	}

	public void setHscode(String hscode) {
		this.hscode = hscode;
	}

	public String getHsname() {
		return hsname;
	}

	public void setHsname(String hsname) {
		this.hsname = hsname;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}

	public List<Jitin_Invoice_PkList_Data> getPackinglist() {
		return packinglist;
	}

	public void setPackinglist(List<Jitin_Invoice_PkList_Data> packinglist) {
		this.packinglist = packinglist;
	}

	public Date getInvoice_shipdateto() {
		return invoice_shipdateto;
	}

	public void setInvoice_shipdateto(Date invoice_shipdateto) {
		this.invoice_shipdateto = invoice_shipdateto;
	}

	public Float getMet() {
		return met;
	}

	public void setMet(Float met) {
		this.met = met;
	}
	
}
