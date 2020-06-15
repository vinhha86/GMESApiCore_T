package vn.gpay.gsmart.core.pcontract_po;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import vn.gpay.gsmart.core.pcontract_price.PContract_Price;
import vn.gpay.gsmart.core.security.GpayUser;

@Table(name="pcontract_po")
@Entity
public class PContract_PO implements Serializable {/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_po_generator")
	@SequenceGenerator(name="pcontract_po_generator", sequenceName = "pcontract_po_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private String code;
	private String po_buyer;
	private String po_vendor;
	private Long productid_link;
	private Float po_quantity;
	private Long unitid_link;
	private Date shipdate;
	private Date matdate;
	private Float actual_quantity;
	private Date actual_shipdate;
	private Float price_cmp;
	private Float price_fob;
	private Float price_sweingtarget;
	private Float price_sweingfact;
	private Float price_add;
	private Float price_commission;
	private Float salaryfund;
	private Long currencyid_link;
	private Float exchangerate;
	private Date productiondate;
	private String packingnotice;
	private Long qcorgid_link;
	private String qcorgname;
	private Integer etm_from;
	private Integer etm_to;
	private Integer etm_avr;
	private Long usercreatedid_link;
	private Date datecreated;
	private Integer status;
	private String factories;
	private Integer productiondays;
	
	
	public Integer getProductiondays() {
		return productiondays;
	}

	public void setProductiondays(Integer productiondays) {
		this.productiondays = productiondays;
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany
    @JoinColumn(name="pcontract_poid_link",insertable=false,updatable =false)
    private List<PContract_Price> po_price = new ArrayList<PContract_Price>();

	public List<PContract_Price> getPoprice() {
		return po_price;
	}

	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="usercreatedid_link",insertable=false,updatable =false)
    private GpayUser usercreated;
	
	@Transient
	public String getUsercreatedName() {
		if(usercreated != null) {
			return usercreated.getFullName();
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getPo_buyer() {
		return po_buyer;
	}

	public void setPo_buyer(String po_buyer) {
		this.po_buyer = po_buyer;
	}

	public String getPo_vendor() {
		return po_vendor;
	}

	public void setPo_vendor(String po_vendor) {
		this.po_vendor = po_vendor;
	}

	public Long getProductid_link() {
		return productid_link;
	}

	public void setProductid_link(Long productid_link) {
		this.productid_link = productid_link;
	}

	public Float getPo_quantity() {
		return po_quantity;
	}

	public void setPo_quantity(Float po_quantity) {
		this.po_quantity = po_quantity;
	}

	public Long getUnitid_link() {
		return unitid_link;
	}

	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}

	public Date getShipdate() {
		return shipdate;
	}

	public void setShipdate(Date shipdate) {
		this.shipdate = shipdate;
	}

	public Date getMatdate() {
		return matdate;
	}

	public void setMatdate(Date matdate) {
		this.matdate = matdate;
	}

	public Float getActual_quantity() {
		return actual_quantity;
	}

	public void setActual_quantity(Float actual_quantity) {
		this.actual_quantity = actual_quantity;
	}

	public Date getActual_shipdate() {
		return actual_shipdate;
	}

	public void setActual_shipdate(Date actual_shipdate) {
		this.actual_shipdate = actual_shipdate;
	}

	public Float getPrice_cmp() {
		return price_cmp;
	}

	public void setPrice_cmp(Float price_cmp) {
		this.price_cmp = price_cmp;
	}

	public Float getPrice_fob() {
		return price_fob;
	}

	public void setPrice_fob(Float price_fob) {
		this.price_fob = price_fob;
	}

	public Float getPrice_sweingtarget() {
		return price_sweingtarget;
	}

	public void setPrice_sweingtarget(Float price_sweingtarget) {
		this.price_sweingtarget = price_sweingtarget;
	}

	public Float getPrice_sweingfact() {
		return price_sweingfact;
	}

	public void setPrice_sweingfact(Float price_sweingfact) {
		this.price_sweingfact = price_sweingfact;
	}

	public Float getPrice_add() {
		return price_add;
	}

	public void setPrice_add(Float price_add) {
		this.price_add = price_add;
	}

	public Float getPrice_commission() {
		return price_commission;
	}

	public void setPrice_commission(Float price_commission) {
		this.price_commission = price_commission;
	}

	public Float getSalaryfund() {
		return salaryfund;
	}

	public void setSalaryfund(Float salaryfund) {
		this.salaryfund = salaryfund;
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

	public Date getProductiondate() {
		return productiondate;
	}

	public void setProductiondate(Date productiondate) {
		this.productiondate = productiondate;
	}

	public String getPackingnotice() {
		return packingnotice;
	}

	public void setPackingnotice(String packingnotice) {
		this.packingnotice = packingnotice;
	}

	public Long getQcorgid_link() {
		return qcorgid_link;
	}

	public void setQcorgid_link(Long qcorgid_link) {
		this.qcorgid_link = qcorgid_link;
	}

	public Integer getEtm_from() {
		return etm_from;
	}

	public void setEtm_from(Integer etm_from) {
		this.etm_from = etm_from;
	}

	public Integer getEtm_to() {
		return etm_to;
	}

	public void setEtm_to(Integer etm_to) {
		this.etm_to = etm_to;
	}

	public Integer getEtm_avr() {
		return etm_avr;
	}

	public void setEtm_avr(Integer etm_avr) {
		this.etm_avr = etm_avr;
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

	public String getFactories() {
		return factories;
	}

	public void setFactories(String factories) {
		this.factories = factories;
	}

	public String getQcorgname() {
		return qcorgname;
	}

	public void setQcorgname(String qcorgname) {
		this.qcorgname = qcorgname;
	}
	
}
