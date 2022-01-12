package vn.gpay.gsmart.core.pcontract_po;

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

import vn.gpay.gsmart.core.product.Product;

@Table(name = "pcontract_po")
@Entity
public class PContract_PO_NoLink implements Serializable {
	/**
	* 
	*/
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pcontract_po_generator")
	@SequenceGenerator(name = "pcontract_po_generator", sequenceName = "pcontract_po_id_seq", allocationSize = 1)
	private Long id;
	private Long orgrootid_link;
	private Long pcontractid_link;
	private String code;
	private String po_buyer;
	private String po_vendor;
	private Long productid_link;
	private Integer po_quantity;
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
	private Integer productiondays;
	private Integer productiondays_ns;
	private Long orgmerchandiseid_link;
	private Long merchandiserid_link;
	private Long parentpoid_link;
	private Boolean is_tbd;
	private Float sewtarget_percent;
	private Long portfromid_link;
	private Long porttoid_link;
	private Boolean isauto_calculate;
	private Long shipmodeid_link;
	private Date date_importdata;
	private Integer plan_productivity;
	private Float plan_linerequired;
	private Integer po_typeid_link;
	private String comment;
	private Boolean ismap;
	private String dc;

	@Transient
	private int po_quantity_sp;
	@Transient
	private int totalpair;

	public Integer getPo_typeid_link() {
		return po_typeid_link;
	}

	public void setPo_typeid_link(Integer po_typeid_link) {
		this.po_typeid_link = po_typeid_link;
	}

	@Transient
	private String productionlines;


	public Integer getPlan_productivity() {
		return plan_productivity;
	}

	public void setPlan_productivity(Integer plan_productivity) {
		this.plan_productivity = plan_productivity;
	}

	public Float getPlan_linerequired() {
		return plan_linerequired;
	}

	public void setPlan_linerequired(Float plan_linerequired) {
		this.plan_linerequired = plan_linerequired;
	}

	public Long getShipmodeid_link() {
		return shipmodeid_link;
	}

	public void setShipmodeid_link(Long shipmodeid_link) {
		this.shipmodeid_link = shipmodeid_link;
	}

	public Integer getProductiondays() {
		return productiondays;
	}

	public void setProductiondays(Integer productiondays) {
		this.productiondays = productiondays;
	}


	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
	@JoinColumn(name = "productid_link", insertable = false, updatable = false)
	private Product product;

	@Transient
	public int getProduct_typeid_link() {
		if (product != null) {
			return product.getProducttypeid_link();
		}
		return 0;
	}


	@Transient
	public String getProductbuyercode() {
		if (product != null) {
			return product.getBuyercode();
		}
		return "";
	}


	@Transient
	public String getProductvendorcode() {
		if (product != null) {
			return product.getVendorcode();
		}
		return "";
	}

	@Transient
	public String getProduct_code() {
		if (product != null) {
			return product.getBuyercode();
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

	public Integer getPo_quantity() {
		return po_quantity;
	}

	public void setPo_quantity(Integer po_quantity) {
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

	public String getQcorgname() {
		return qcorgname;
	}

	public void setQcorgname(String qcorgname) {
		this.qcorgname = qcorgname;
	}

	public Long getOrgmerchandiseid_link() {
		return orgmerchandiseid_link;
	}

	public Long getMerchandiserid_link() {
		return merchandiserid_link;
	}

	public void setOrgmerchandiseid_link(Long orgmerchandiseid_link) {
		this.orgmerchandiseid_link = orgmerchandiseid_link;
	}

	public void setMerchandiserid_link(Long merchandiserid_link) {
		this.merchandiserid_link = merchandiserid_link;
	}

	public Long getParentpoid_link() {
		return parentpoid_link;
	}

	public void setParentpoid_link(Long parentpoid_link) {
		this.parentpoid_link = parentpoid_link;
	}

	public Boolean getIs_tbd() {
		return is_tbd;
	}

	public void setIs_tbd(Boolean is_tbd) {
		this.is_tbd = is_tbd;
	}

	public Float getSewtarget_percent() {
		return sewtarget_percent;
	}

	public void setSewtarget_percent(Float sewtarget_percent) {
		this.sewtarget_percent = sewtarget_percent;
	}

	public Long getPortfromid_link() {
		return portfromid_link;
	}

	public void setPortfromid_link(Long portfromid_link) {
		this.portfromid_link = portfromid_link;
	}

	public Long getPorttoid_link() {
		return porttoid_link;
	}

	public void setPorttoid_link(Long porttoid_link) {
		this.porttoid_link = porttoid_link;
	}

	public Boolean getIsauto_calculate() {
		return isauto_calculate;
	}

	public void setIsauto_calculate(Boolean isauto_calculate) {
		this.isauto_calculate = isauto_calculate;
	}

	public Date getDate_importdata() {
		return date_importdata;
	}

	public void setDate_importdata(Date date_importdata) {
		this.date_importdata = date_importdata;
	}

	public Integer getProductiondays_ns() {
		return productiondays_ns;
	}

	public void setProductiondays_ns(Integer productiondays_ns) {
		this.productiondays_ns = productiondays_ns;
	}

	public String getProductionlines() {
		return productionlines;
	}

	public void setProductionlines(String productionlines) {
		this.productionlines = productionlines;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Boolean getIsmap() {
		return ismap;
	}

	public void setIsmap(Boolean ismap) {
		this.ismap = ismap;
	}

	public String getDc() {
		return dc;
	}

	public void setDc(String dc) {
		this.dc = dc;
	}

	public int getPo_quantity_sp() {
		return po_quantity_sp;
	}

	public void setPo_quantity_sp(int po_quantity_sp) {
		this.po_quantity_sp = po_quantity_sp;
	}

	public int getTotalpair() {
		return totalpair;
	}

	public void setTotalpair(int totalpair) {
		this.totalpair = totalpair;
	}
}
