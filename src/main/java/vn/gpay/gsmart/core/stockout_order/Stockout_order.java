package vn.gpay.gsmart.core.stockout_order;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="stockout_order")
@Entity
public class Stockout_order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_order_generator")
	@SequenceGenerator(name="stockout_order_generator", sequenceName = "stockout_order_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private String ordercode;
	private Date orderdate;
	private Date stockoutdate;
	private Integer stockouttypeid_link;
	private Long orgid_from_link;
	private Long orgid_to_link;
	private String orderperson;
	private Integer totalpackage;
	private Float totalm3;
	private Float totalnetweight;
	private Float totalgrossweight;
	private Long p_skuid_link;
	private String extrainfo;
	private Integer status;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	private Long porderid_link;
	private String stockout_order_code;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_from_link",insertable=false,updatable =false)
    private Org orgFrom;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="orgid_to_link",insertable=false,updatable =false)
    private Org orgTo;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreateid_link",insertable=false,updatable =false)
    private GpayUser usercreate;
	
	@Transient
	public String getUsercreate_name() {
		if(usercreate!=null)
			return usercreate.getFullName();
		return "";
	}
	
	@Transient
	public String getOrg_from_name() {
		if(orgFrom!=null)
			return orgFrom.getName();
		return "";
	}
	
	@Transient
	public String getOrg_to_name() {
		if(orgTo!=null) 
			return orgTo.getName();
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public String getOrdercode() {
		return ordercode;
	}
	public Date getOrderdate() {
		return orderdate;
	}
	public Date getStockoutdate() {
		return stockoutdate;
	}
	public Integer getStockouttypeid_link() {
		return stockouttypeid_link;
	}
	public Long getOrgid_from_link() {
		return orgid_from_link;
	}
	public Long getOrgid_to_link() {
		return orgid_to_link;
	}
	public String getOrderperson() {
		return orderperson;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public Float getTotalm3() {
		return totalm3;
	}
	public Float getTotalnetweight() {
		return totalnetweight;
	}
	public Float getTotalgrossweight() {
		return totalgrossweight;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public String getExtrainfo() {
		return extrainfo;
	}
	public Integer getStatus() {
		return status;
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public Long getPorderid_link() {
		return porderid_link;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public void setOrdercode(String ordercode) {
		this.ordercode = ordercode;
	}
	public void setOrderdate(Date orderdate) {
		this.orderdate = orderdate;
	}
	public void setStockoutdate(Date stockoutdate) {
		this.stockoutdate = stockoutdate;
	}
	public void setStockouttypeid_link(Integer stockouttypeid_link) {
		this.stockouttypeid_link = stockouttypeid_link;
	}
	public void setOrgid_from_link(Long orgid_from_link) {
		this.orgid_from_link = orgid_from_link;
	}
	public void setOrgid_to_link(Long orgid_to_link) {
		this.orgid_to_link = orgid_to_link;
	}
	public void setOrderperson(String orderperson) {
		this.orderperson = orderperson;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public void setTotalm3(Float totalm3) {
		this.totalm3 = totalm3;
	}
	public void setTotalnetweight(Float totalnetweight) {
		this.totalnetweight = totalnetweight;
	}
	public void setTotalgrossweight(Float totalgrossweight) {
		this.totalgrossweight = totalgrossweight;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public void setExtrainfo(String extrainfo) {
		this.extrainfo = extrainfo;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	public void setPorderid_link(Long porderid_link) {
		this.porderid_link = porderid_link;
	}
	public String getStockout_order_code() {
		return stockout_order_code;
	}
	public void setStockout_order_code(String stockout_order_code) {
		this.stockout_order_code = stockout_order_code;
	}
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	
	
}