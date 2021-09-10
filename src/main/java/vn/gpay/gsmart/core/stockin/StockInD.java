package vn.gpay.gsmart.core.stockin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
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

import vn.gpay.gsmart.core.attributevalue.Attributevalue;
import vn.gpay.gsmart.core.category.Color;
import vn.gpay.gsmart.core.category.Unit;
import vn.gpay.gsmart.core.sku.SKU;

@Table(name="Stockin_d")
@Entity
public class StockInD implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
	//@GeneratedValue(strategy=GenerationType.AUTO)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "stockind_generator")
	@SequenceGenerator(name="stockind_generator", sequenceName = "stock_in_d_id_seq", allocationSize=1)
	protected Long id;
	
	@Column(name ="orgrootid_link")
    private Long orgrootid_link;
	
	@Column(name ="stockinid_link")
    private Long stockinid_link;
	
	@Column(name ="skucode")
	private String skucode;
	
	public void setAttCo(Attributevalue attCo) {
		this.attCo = attCo;
	}

	@Column(name ="skuid_link")
    private Long skuid_link;
	
	private Integer skutypeid_link;
	
	private Integer porder_year;
	
	@Column(name ="colorid_link")
    private Long colorid_link;
	
	private Long sizeid_link;
	
	@Column(name ="unitid_link")
    private Integer unitid_link;
	
	@Column(name ="totalpackage")
    private Integer totalpackage;
	
	@Column(name ="totalydsorigin")
    private Float totalydsorigin;
	
	@Column(name ="foc")
    private Float foc;
	
	@Column(name ="totalpackagecheck")
    private Integer totalpackagecheck;
	
	@Column(name ="totalydscheck")
    private Float totalydscheck;
	
	@Column(name ="unitprice")
    private Float unitprice;
	
	@Column(name = "p_skuid_link")
    private Long p_skuid_link;
	
	@Column(name="usercreateid_link")
	private Long usercreateid_link;
	
	@Column(name ="timecreate")
	private Date timecreate;
	
	@Column(name="lastuserupdateid_link")
	private Long lastuserupdateid_link;
	
	@Column(name ="lasttimeupdate")
	private Date lasttimeupdate;
	
	@Column(name = "status")
    private Integer status;
	
	private Integer totalpackage_order;
    private Float netweight;
    private Float grossweight;
    private Float m3;
    private Float totalmet_origin;
    private Float totalmet_check;
	private Float netweight_lbs;
	private Float grossweight_lbs;
    
	
	@NotFound(action = NotFoundAction.IGNORE)
	@OneToMany(cascade =  CascadeType.ALL , orphanRemoval=true )//fetch = FetchType.LAZY load tu tu
	@JoinColumn( name="stockindid_link", referencedColumnName="id")
	private List<StockInPklist>  stockin_packinglist  = new ArrayList<StockInPklist>();
	
//	@NotFound(action = NotFoundAction.IGNORE)
//	@OneToMany(cascade =  CascadeType.ALL , orphanRemoval=true )//fetch = FetchType.LAZY load tu tu
//	@JoinColumn( name="stockindid_link", referencedColumnName="id")
//	private List<Warehouse>  stockin_warehouse  = new ArrayList<Warehouse>();
	
	@Transient
	public String getSkuname() {
		if(sku!=null) {
			return sku.getName();
		}
		return "";
	}
	@Transient
	public String getSkuCode() {
		if(sku!=null) {
			return sku.getCode();
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
	public String getUnit_name() {
		if(unit!=null) {
			return unit.getName();
		}
		return "";
	}
	@Transient
	public String getColorcode() {
		if(color!=null) {
			return color.getCode();
		}
		return "";
	}
	@Transient
	public String getColorname() {
		if(color!=null) {
			return color.getName();
		}
		return "";
	}
	@Transient
	public String getColorRGB() {
		if(color!=null) {
			return color.getRgbvalue();
		}
		return "";
	}
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="skuid_link",insertable=false,updatable =false)
    private SKU sku;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="colorid_link",insertable=false,updatable =false)
    private Color color;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="colorid_link",insertable=false,updatable =false)
    private Attributevalue attMau;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="sizeid_link",insertable=false,updatable =false)
    private Attributevalue attCo;
	
	@NotFound(action = NotFoundAction.IGNORE)
	@ManyToOne
    @JoinColumn(name="unitid_link",insertable=false,updatable =false)
    private Unit unit;
	
	@Transient
	public String getColor_name() {
		if(attMau != null) {
			return attMau.getValue();
		}
		return "";
	}
	
	@Transient
	public String getSku_product_code() {
		if(sku != null) {
			return sku.getProduct_code();
		}
		return "";
	}
	
	@Transient
	public String getSize_name() {
		if(attCo != null) {
			return attCo.getValue();
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
	public Long getStockinid_link() {
		return stockinid_link;
	}
	public void setStockinid_link(Long stockinid_link) {
		this.stockinid_link = stockinid_link;
	}
	public Long getSkuid_link() {
		return skuid_link;
	}
	public void setSkuid_link(Long skuid_link) {
		this.skuid_link = skuid_link;
	}
	public Long getColorid_link() {
		return colorid_link;
	}
	public void setColorid_link(Long colorid_link) {
		this.colorid_link = colorid_link;
	}
	public Integer getUnitid_link() {
		return unitid_link;
	}
	public void setUnitid_link(Integer unitid_link) {
		this.unitid_link = unitid_link;
	}
	public Integer getTotalpackage() {
		return totalpackage;
	}
	public void setTotalpackage(Integer totalpackage) {
		this.totalpackage = totalpackage;
	}

	public Float getTotalydsorigin() {
		return totalydsorigin;
	}
	public void setTotalydsorigin(Float totalydsorigin) {
		this.totalydsorigin = totalydsorigin;
	}
	public Float getFoc() {
		return foc;
	}
	public void setFoc(Float foc) {
		this.foc = foc;
	}
	public Integer getTotalpackagecheck() {
		return totalpackagecheck;
	}
	public void setTotalpackagecheck(Integer totalpackagecheck) {
		this.totalpackagecheck = totalpackagecheck;
	}
	public Float getTotalydscheck() {
		return totalydscheck;
	}
	public void setTotalydscheck(Float totalydscheck) {
		this.totalydscheck = totalydscheck;
	}
	public Float getUnitprice() {
		return unitprice;
	}
	public void setUnitprice(Float unitprice) {
		this.unitprice = unitprice;
	}
	public Long getP_skuid_link() {
		return p_skuid_link;
	}
	public void setP_skuid_link(Long p_skuid_link) {
		this.p_skuid_link = p_skuid_link;
	}
	public Long getUsercreateid_link() {
		return usercreateid_link;
	}
	public void setUsercreateid_link(Long usercreateid_link) {
		this.usercreateid_link = usercreateid_link;
	}
	public Date getTimecreate() {
		return timecreate;
	}
	public void setTimecreate(Date timecreate) {
		this.timecreate = timecreate;
	}
	public Long getLastuserupdateid_link() {
		return lastuserupdateid_link;
	}
	public void setLastuserupdateid_link(Long lastuserupdateid_link) {
		this.lastuserupdateid_link = lastuserupdateid_link;
	}
	public Date getLasttimeupdate() {
		return lasttimeupdate;
	}
	public void setLasttimeupdate(Date lasttimeupdate) {
		this.lasttimeupdate = lasttimeupdate;
	}
	public List<StockInPklist> getStockin_packinglist() {
		return stockin_packinglist;
	}
	public void setStockin_packinglist(List<StockInPklist> stockin_packinglist) {
		this.stockin_packinglist = stockin_packinglist;
	}
	public void setSku(SKU sku) {
		this.sku = sku;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	public String getSkucode() {
		return skucode;
	}
	public void setSkucode(String skucode) {
		this.skucode = skucode;
	}
	public Integer getTotalpackage_order() {
		return totalpackage_order;
	}
	public void setTotalpackage_order(Integer totalpackage_order) {
		this.totalpackage_order = totalpackage_order;
	}
	public Integer getSkutypeid_link() {
		return skutypeid_link;
	}
	public void setSkutypeid_link(Integer skutypeid_link) {
		this.skutypeid_link = skutypeid_link;
	}
//	public List<Warehouse> getStockin_warehouse() {
//		return stockin_warehouse;
//	}
//	public void setStockin_warehouse(List<Warehouse> stockin_warehouse) {
//		this.stockin_warehouse = stockin_warehouse;
//	}
	public Long getSizeid_link() {
		return sizeid_link == null ? 0: sizeid_link;
	}
	public void setSizeid_link(Long sizeid_link) {
		this.sizeid_link = sizeid_link;
	}
	public Integer getPorder_year() {
		return porder_year;
	}
	public void setPorder_year(Integer porder_year) {
		this.porder_year = porder_year;
	}
	public Attributevalue getAttMau() {
		return attMau;
	}
	public void setAttMau(Attributevalue attMau) {
		this.attMau = attMau;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Float getNetweight() {
		return netweight;
	}
	public void setNetweight(Float netweight) {
		this.netweight = netweight;
	}
	public Float getGrossweight() {
		return grossweight;
	}
	public void setGrossweight(Float grossweight) {
		this.grossweight = grossweight;
	}
	public Float getM3() {
		return m3;
	}
	public void setM3(Float m3) {
		this.m3 = m3;
	}
	public Float getTotalmet_origin() {
		return totalmet_origin;
	}
	public void setTotalmet_origin(Float totalmet_origin) {
		this.totalmet_origin = totalmet_origin;
	}
	public Float getTotalmet_check() {
		return totalmet_check;
	}
	public void setTotalmet_check(Float totalmet_check) {
		this.totalmet_check = totalmet_check;
	}
	public Float getNetweight_lbs() {
		return netweight_lbs;
	}
	public void setNetweight_lbs(Float netweight_lbs) {
		this.netweight_lbs = netweight_lbs;
	}
	public Float getGrossweight_lbs() {
		return grossweight_lbs;
	}
	public void setGrossweight_lbs(Float grossweight_lbs) {
		this.grossweight_lbs = grossweight_lbs;
	}
	
	
	/*
	@ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinTable(name = "StockInD_Sku",joinColumns = { 
			@JoinColumn(name = "stockind_id", nullable = false, updatable = false) }, 
			inverseJoinColumns = { @JoinColumn(name = "sku_id", 
					nullable = false, updatable = false) })
	private List<Sku> skus = new ArrayList<>();*/
}
