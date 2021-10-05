package vn.gpay.gsmart.core.porder_grant_sku_plan;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Table(name="porder_grant_sku_plan")
@Entity
public class POrderGrant_SKU_Plan implements Serializable{
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "porder_grant_sku_plan_generator")
	@SequenceGenerator(name="porder_grant_sku_plan_generator", sequenceName = "porder_grant_sku_plan_id_seq", allocationSize=1)
	protected Long id;

	private Long porder_grant_skuid_link;
	private Date date;
	private Integer amount;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getPorder_grant_skuid_link() {
		return porder_grant_skuid_link;
	}
	public void setPorder_grant_skuid_link(Long porder_grant_skuid_link) {
		this.porder_grant_skuid_link = porder_grant_skuid_link;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Integer getAmount() {
		return amount;
	}
	public void setAmount(Integer amount) {
		this.amount = amount;
	}
	
}
