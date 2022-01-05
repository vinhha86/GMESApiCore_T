package vn.gpay.gsmart.core.porder_grant;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.org.Org;

@Table(name = "porder_grant")
@Entity
public class POrderGrant_NoLink implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;

	@Column(name = "orgrootid_link")
	private Long orgrootid_link;

	@Column(name = "porderid_link")
	private Long porderid_link;

	@Column(name = "ordercode", length = 10)
	private String ordercode;

	@Column(name = "granttoorgid_link")
	private Long granttoorgid_link;

	@Column(name = "grantdate")
	private Date grantdate;

	@Column(name = "grantamount")
	private Integer grantamount;

	@Column(name = "amountcutsum")
	private Integer amountcutsum;

	@Column(name = "status")
	private Integer status;

	@Column(name = "usercreatedid_link")
	private Long usercreatedid_link;

	@Column(name = "timecreated")
	private Date timecreated;

	private Date start_date_plan;
	private Date finish_date_plan;

	private Integer productivity;
	private Integer duration;
	private Integer totalamount_tt;
	private String reason_change;
	private Integer type;
	private Boolean is_show_image;
	private Boolean ismap;
	public String lineinfo;
	
	

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getReason_change() {
		return reason_change;
	}

	public void setReason_change(String reason_change) {
		this.reason_change = reason_change;
	}

	public Integer getProductivity() {
		return productivity;
	}

	public void setProductivity(Integer productivity) {
		this.productivity = productivity;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "granttoorgid_link", insertable = false, updatable = false)
	private Org org;

	@Transient
	public String getXuongTo() {
		if(org != null)
			return org.getName();
		return "";
	}

	@Transient
	public String getXuongSX() {
		if(org != null)
			return org.getParentcode();
		return "";
	}
	
	@Transient
	public Long getXuongSX_ID() {
		if(org != null)
			return org.getParentid_link();
		return null;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}

	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}

	public Long getPorderid_link() {
		return porderid_link;
	}

	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}

	public Long getGranttoorgid_link() {
		return granttoorgid_link;
	}

	public void setGranttoorgid_link(Long granttoorgid_link) {
		this.granttoorgid_link = granttoorgid_link;
	}

	public Date getGrantdate() {
		return grantdate;
	}

	public void setGrantdate(Date grantdate) {
		this.grantdate = grantdate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getTimecreated() {
		return timecreated;
	}

	public void setTimecreated(Date timecreated) {
		this.timecreated = timecreated;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Integer getGrantamount() {
		return grantamount;
	}

	public void setGrantamount(Integer grantamount) {
		this.grantamount = grantamount;
	}

	public Integer getAmountcutsum() {
		return amountcutsum;
	}

	public void setAmountcutsum(Integer amountcutsum) {
		this.amountcutsum = amountcutsum;
	}

	public String getOrdercode() {
		return ordercode;
	}

	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}

	public Date getStart_date_plan() {
		return start_date_plan;
	}

	public Date getFinish_date_plan() {
		return finish_date_plan;
	}

	public void setStart_date_plan(Date start_date_plan) {
		this.start_date_plan = start_date_plan;
	}

	public void setFinish_date_plan(Date finish_date_plan) {
		this.finish_date_plan = finish_date_plan;
	}

	public Integer getTotalamount_tt() {
		return totalamount_tt;
	}

	public void setTotalamount_tt(Integer totalamount_tt) {
		this.totalamount_tt = totalamount_tt;
	}

	public Boolean getIs_show_image() {
		return is_show_image == null ? false : is_show_image;
	}

	public void setIs_show_image(Boolean is_show_image) {
		this.is_show_image = is_show_image;
	}

	public Boolean getIsmap() {
		return ismap;
	}

	public void setIsmap(Boolean ismap) {
		this.ismap = ismap;
	}

	public String getLineinfo() {
		return lineinfo;
	}

	public void setLineinfo(String lineinfo) {
		this.lineinfo = lineinfo;
	}

}
