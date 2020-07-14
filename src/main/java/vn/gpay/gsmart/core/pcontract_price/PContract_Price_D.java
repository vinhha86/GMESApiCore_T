package vn.gpay.gsmart.core.pcontract_price;

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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.fob_price.FOBPrice;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="pcontract_price_d")
@Entity
public class PContract_Price_D implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_price_d_generator")
	@SequenceGenerator(name="pcontract_price_d_generator", sequenceName = "pcontract_price_d_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private Long pcontract_poid_link;
	private Long productid_link;
	private Float price; //giá chào
	private Float cost; // giá vốn
	private Boolean isfob;
	private Long currencyid_link;
	private Float exchangerate;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer status;
	private Long fobpriceid_link;
	private Long sizesetid_link;
	private Long pcontractpriceid_link;
	private Float quota;
	private Float unitprice;
	private Long unitid_link;
	
	public Long getFobpriceid_link() {
		return fobpriceid_link;
	}

	public void setFobpriceid_link(Long fobpriceid_link) {
		this.fobpriceid_link = fobpriceid_link;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreatedid_link",insertable=false,updatable =false)
    private GpayUser usercreated;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="fobpriceid_link",insertable=false,updatable =false)
    private FOBPrice fobprice;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getFobprice_name() {
		if(fobprice != null) {
			return fobprice.getName();
		}
		return "";
	}
	
	@Transient
	public String getUsercreatedName() {
		if(usercreated != null) {
			return usercreated.getFullName();
		}
		return "";
	}
	
	@Transient
	public String getUnitcode() {
		if(unit != null) {
			return unit.getCode();
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

	public Long getPcontractid_link() {
		return pcontractid_link;
	}

	public void setPcontractid_link(Long pcontractid_link) {
		this.pcontractid_link = pcontractid_link;
	}

	public Long getPcontract_poid_link() {
		return pcontract_poid_link;
	}

	public void setPcontract_poid_link(Long pcontract_poid_link) {
		this.pcontract_poid_link = pcontract_poid_link;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}


	public Long getCurrencyid_link() {
		return currencyid_link;
	}

	public void setCurrencyid_link(Long currencyid_link) {
		this.currencyid_link = currencyid_link;
	}

	public Float getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(Float exchangerate) {
		this.exchangerate = exchangerate;
	}

	public Long getUsercreatedid_link() {
		return usercreatedid_link;
	}

	public void setUsercreatedid_link(Long usercreatedid_link) {
		this.usercreatedid_link = usercreatedid_link;
	}

	public Date getDatecreated() {
		return datecreated;
	}

	public void setDatecreated(Date datecreated) {
		this.datecreated = datecreated;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Float getPrice() {
		return price;
	}

	public void setPrice(Float price) {
		this.price = price;
	}

	public Float getCost() {
		return cost;
	}

	public void setCost(Float cost) {
		this.cost = cost;
	}

	public Boolean getIsfob() {
		return isfob;
	}

	public void setIsfob(Boolean isfob) {
		this.isfob = isfob;
	}

	public Long getSizesetid_link() {
		return sizesetid_link;
	}

	public void setSizesetid_link(Long sizesetid_link) {
		this.sizesetid_link = sizesetid_link;
	}

	public Long getPcontractpriceid_link() {
		return pcontractpriceid_link;
	}

	public void setPcontractpriceid_link(Long pcontractpriceid_link) {
		this.pcontractpriceid_link = pcontractpriceid_link;
	}

	public Float getQuota() {
		return quota;
	}

	public void setQuota(Float quota) {
		this.quota = quota;
	}

	public Float getUnitprice() {
		return unitprice;
	}

	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

}
