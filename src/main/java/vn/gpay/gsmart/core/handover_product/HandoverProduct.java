package vn.gpay.gsmart.core.handover_product;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.product.Product;

@Table(name="handover_product")
@Entity
public class HandoverProduct  implements Serializable{

	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "handover_product_generator")
	@SequenceGenerator(name="handover_product_generator", sequenceName = "handover_product_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
	private Long orgrootid_link;
	
	@Column(name ="handoverid_link")
	private Long handoverid_link;
	
	@Column(name ="productid_link")
	private Long productid_link;
	
	@Column(name ="unitid_link")
	private Long unitid_link;
	
	@Column(name ="totalpackage")
	private Integer totalpackage;
	
	@Column(name ="totalpackagecheck")
	private Integer totalpackagecheck;
	
	@Column(name ="status")
	private Integer status;
	
	@Column(name ="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="productid_link",insertable=false,updatable =false)
    private Product product;
	
	@Transient
	public String getBuyercode() {
		if(product!=null) {
			if(product.getBuyercode() != null)
				return product.getBuyercode();
		}
		return "";
	}
	
	@Transient
	public String getBuyername() {
		if(product!=null) {
			if(product.getBuyername() != null)
				return product.getBuyername();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
	private Unit unit;
	
	@Transient
	public String getUnitName() {
		if(unit!=null) {
			if(unit.getName() != null)
				return unit.getName();
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

	public Long getHandoverid_link() {
		return handoverid_link;
	}

	public void setHandoverid_link(Long handoverid_link) {
		this.handoverid_link = handoverid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
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

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getUsercreateid_link() {
		return usercreateid_link;
	}

	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}

	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}

	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}

	public Date getTimecreate() {
		return timecreate;
	}

	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}

	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}

	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	
	
}
