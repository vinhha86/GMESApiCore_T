package vn.gpay.gsmart.core.porder_grant;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
//import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
//import javax.persistence.JoinColumn;
//import javax.persistence.ManyToOne;
import javax.persistence.Table;
//import javax.persistence.Transient;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.org.Org;
import vn.gpay.gsmart.core.porder.POrder;

@Table(name="porder_grant")
@Entity
public class POrderGrant implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="porderid_link")
    private Long porderid_link;
	
	@Column(name ="ordercode", length=10)
    private String ordercode;	

	@Column(name ="granttoorgid_link")
    private Long granttoorgid_link;
	
	@Column(name ="grantdate")
    private Date grantdate;

	@Column(name ="grantamount")
    private Integer grantamount;	
	
	@Column(name ="amountcutsum")
    private Integer amountcutsum;	
	
	@Column(name ="status")
    private Integer status;	
	
	@Column(name ="usercreatedid_link")
    private Long usercreatedid_link;	
	
	@Column(name ="timecreated")
    private Date timecreated;

	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.ALL , orphanRemoval=true )
	@JoinColumn( name="pordergrantid_link", referencedColumnName="id")
	private List<POrderGrant_SKU>  porder_grant_sku  = new ArrayList<>();
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="porderid_link",insertable=false,updatable =false)
    private POrder porder;
	
	@Transient
	public String getMaHang() {
		if(porder!=null)
			return porder.getMaHang();
		return "";
	}
	@Transient
	public String getCls() {
		if(porder!=null) {
			return porder.getCls();
		}
		return "";
	}
	
	@Transient
	public int getTotalpackage() {
		if(porder != null) {
			return porder.getTotalorder();
		}
		return 0;
	}
	
	@Transient
	public Date getOrderdate() {
		if(porder != null) {
			return porder.getOrderdate();
		}
		return null;
	}
	@Transient
	public Date getProductiondate() {
		if(porder != null) {
			return porder.getProductiondate();
		}
		return null;
	}
	@Transient
	public Date getGolivedate() {
		if(porder != null) {
			return porder.getGolivedate();
		}
		return null;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="granttoorgid_link",insertable=false,updatable =false)
    private Org org;
	
	@Transient
	public String getGranttoorgname() {
		if(org != null) {
			return org.getName();
		}
		return "";
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
	public List<POrderGrant_SKU> getPorder_grant_sku() {
		return porder_grant_sku;
	}
	public void setPorder_grant_sku(List<POrderGrant_SKU> porder_grant_sku) {
		this.porder_grant_sku = porder_grant_sku;
	}

}
