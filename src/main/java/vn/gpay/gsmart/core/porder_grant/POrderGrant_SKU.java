package vn.gpay.gsmart.core.porder_grant;
import java.io.Serializable;

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

import vn.gpay.gsmart.core.sku.SKU;


@Table(name="porder_grant_sku")
@Entity
public class POrderGrant_SKU implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="pordergrantid_link")
    private Long pordergrantid_link;
	
	@Column(name ="skuid_link")
    private Long skuid_link;
	
	@Column(name ="grantamount")
    private Integer grantamount;

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@Transient
	public String getMauSanPham() {
		if(sku!=null) {
			return sku.getMauSanPham();
		}
		return "";
	}
	@Transient
	public String getCoSanPham() {
		if(sku!=null) {
			return sku.getCoSanPham();
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

	public Long getPordergrantid_link() {
		return pordergrantid_link;
	}

	public void setPordergrantid_link(Long pordergrantid_link) {
		this.pordergrantid_link = pordergrantid_link;
	}

	public Long getSkuid_link() {
		return skuid_link;
	}

	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}

	public Integer getGrantamount() {
		return grantamount;
	}

	public void setGrantamount(Integer grantamount) {
		this.grantamount = grantamount;
	}	

	
}
