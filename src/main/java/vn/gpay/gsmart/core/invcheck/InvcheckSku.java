package vn.gpay.gsmart.core.invcheck;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="invcheck_sku")
@Entity
@IdClass(InvcheckSkuID.class)
public class InvcheckSku implements Serializable {
	private static final long serialVersionUID = 1L;
	
	//@EmbeddedId
   // private InvcheckSkuID invchecksku_pk;
	@Id
	@Column(name = "orgrootid_link")
    private Long orgrootid_link;
	@Id
    @Column(name = "invcheckid_link")
    private Long invcheckid_link;
	@Id
    @Column(name = "skuid_link")
    private Long skuid_link;
	
	@Column(name = "unitid_link")
    private Integer unitid_link;
	
	@Column(name ="ydsorigin")
    private Float ydsorigin;
	
	@Column(name ="ydscheck")
    private Float ydscheck;
	
	@Column(name ="totalpackage")
    private Integer totalpackage;
	
	@Column(name ="totalpackagecheck")
    private Integer totalpackagecheck;
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name ="totalamount")
    private Float totalamount;
	
	/*
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;*/
	
	/*
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany( cascade =  CascadeType.REFRESH , orphanRemoval=true )
	@JoinColumns({
		@JoinColumn(name = "orgid_link", referencedColumnName = "orgid_link"),
		@JoinColumn(name = "invcheckid_link", referencedColumnName = "invcheckid_link"),
		@JoinColumn(name = "skuid_link", referencedColumnName = "skuid_link")
	})
	private List<InvcheckEpc>  epcs  = new ArrayList<>();
	*/

	@Transient
	public String getSkucode() {
		if(sku!=null) {
			return sku.getCode();
		}
		return "";
	}
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	@Transient
	public String getHscode() {
		if(sku!=null) {
			return sku.getHscode();
		}
		return "";
	}
	@Transient
	public String getHsname() {
		if(sku!=null) {
			return sku.getHsname();
		}
		return "";
	}
	
	@Transient
	public String getUnitname() {
		if(unit!=null) {
			return unit.getName();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;

	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	public Long getInvcheckid_link() {
		return invcheckid_link;
	}
	public void setInvcheckid_link(Long invcheckid_link) {
		this.invcheckid_link = invcheckid_link;
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
	public Float getYdsorigin() {
		return ydsorigin;
	}
	public void setYdsorigin(Float ydsorigin) {
		this.ydsorigin = ydsorigin;
	}
	public Float getYdscheck() {
		return ydscheck;
	}
	public void setYdscheck(Float ydscheck) {
		this.ydscheck = ydscheck;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
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
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public SKU getSku() {
		return sku;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
}
