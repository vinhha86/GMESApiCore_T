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

import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="stockout_order_d")
@Entity
public class Stockout_order_d implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockout_order_d_generator")
	@SequenceGenerator(name="stockout_order_d_generator", sequenceName = "stockout_order_d_id_seq", allocationSize=1)
	private Long id;
	private Long orgrootid_link;
	private Long stockoutorderid_link;
	private Long material_skuid_link;
	private Long colorid_link;
	private Long unitid_link;
	private Integer totalpackage;
	private Float totalyds;
	private Float totalpackagecheck;
	private Float totalydscheck;
	private Float unitprice;
	private Long p_skuid_link;
	private Long usercreateid_link;
	private Date timecreate;
	private Long lastuserupdateid_link;
	private Date lasttimeupdate;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="material_skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getUnitName() {
		if(unit!=null)
			return unit.getName();
		return "";
	}
	
	@Transient
	public String getMaterialCode() {
		if(sku!=null)
			return sku.getProduct_code();
		return "";
	}
	
	@Transient
	public String getMaterialName() {
		if(sku!=null)
			return sku.getProduct_name();
		return "";
	}
	
	@Transient
	public String getTenMauNPL() {
		if(sku!=null)
			return sku.getColor_name();
		return "";
	}
	
	@Transient
	public String getCoKho() {
		if(sku!=null)
			return sku.getSize_name();
		return "";
	}
	
	public Long getId() {
		return id;
	}
	public Long getStockoutorderid_link() {
		return stockoutorderid_link;
	}
	public Long getMaterial_skuid_link() {
		return material_skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public Long getUnitid_link() {
		return unitid_link;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public Float getTotalyds() {
		return totalyds;
	}
	public Float getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
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
	public void setId(Long id) {
		this.id = id;
	}
	public void setStockoutorderid_link(Long stockoutorderid_link) {
		this.stockoutorderid_link = stockoutorderid_link;
	}
	public void setMaterial_skuid_link(Long material_skuid_link) {
		this.material_skuid_link = material_skuid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public void setUnitid_link(Long unitid_link) {
		this.unitid_link = unitid_link;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}
	public void setTotalyds(Float totalyds) {
		this.totalyds = totalyds;
	}
	public void setTotalpackagecheck(Float totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
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
	public Long getOrgrootid_link() {
		return orgrootid_link;
	}
	public void setOrgrootid_link(Long orgrootid_link) {
		this.orgrootid_link = orgrootid_link;
	}
	
	
}