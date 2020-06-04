package vn.gpay.gsmart.core.porder;

import java.util.Date;

public class POrderSetReady {
	private Long porderid_link;
	private Long pprocesingid;
	private String ordercode;
	private Date productiondate;
	private Date material_date;
	private Date sample_date;
	private Date cut_date;
	private Date packing_date;
	private Date qc_date;
	private Date stockout_date;
	
	private String comment;
	
	public Long getPprocesingid() {
		return pprocesingid;
	}
	public void setPprocesingid(Long pprocesingid) {
		this.pprocesingid = pprocesingid;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public Date getProductiondate() {
		return productiondate;
	}
	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public Date getMaterial_date() {
		return material_date;
	}
	public void setMaterial_date(Date material_date) {
		this.material_date = material_date;
	}
	public Date getSample_date() {
		return sample_date;
	}
	public void setSample_date(Date sample_date) {
		this.sample_date = sample_date;
	}
	public Date getCut_date() {
		return cut_date;
	}
	public void setCut_date(Date cut_date) {
		this.cut_date = cut_date;
	}
	public Date getPacking_date() {
		return packing_date;
	}
	public void setPacking_date(Date packing_date) {
		this.packing_date = packing_date;
	}
	public Date getQc_date() {
		return qc_date;
	}
	public void setQc_date(Date qc_date) {
		this.qc_date = qc_date;
	}
	public Date getStockout_date() {
		return stockout_date;
	}
	public void setStockout_date(Date stockout_date) {
		this.stockout_date = stockout_date;
	}
	
}
